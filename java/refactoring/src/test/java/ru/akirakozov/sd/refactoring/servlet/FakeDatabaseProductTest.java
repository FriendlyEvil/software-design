package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class FakeDatabaseProductTest {
    private static final String DATABASE_DEFAULT_NAME = "test";
    private static final String DATABASE_CONNECTION_TEMPLATE = "jdbc:sqlite:%s.db";
    private final String dataBaseConnectionString;

    protected StringWriter writer = new StringWriter();
    @Mock
    protected HttpServletResponse response;
    @Mock
    protected HttpServletRequest request;

    public FakeDatabaseProductTest() {
        this(DATABASE_DEFAULT_NAME);
    }

    public FakeDatabaseProductTest(String databaseName) {
        this.dataBaseConnectionString = String.format(DATABASE_CONNECTION_TEMPLATE, databaseName);
    }

    @Before
    public void setUpMocks() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Before
    public void createTestDatabase() {
        execSql("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @After
    public void deleteTestDatabase() {
        execSql("DROP TABLE IF EXISTS PRODUCT");
    }

    protected List<List<String>> selectSql(String sql, List<String> fields) {
        List<List<String>> result = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(dataBaseConnectionString)) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                List<String> tmp = new ArrayList<>();
                for (String field : fields) {
                    tmp.add(resultSet.getString(field));
                }
                result.add(tmp);
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected void execSql(String sql) {
        try (Connection c = DriverManager.getConnection(dataBaseConnectionString)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void stripAndCheck(String result, String actual) {
        Assert.assertEquals(actual.strip(), result.strip());
    }

}
