package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.when;


public class GetProductsServletTest {
    private static final String TEST_DATABASE_NAME = "test";
    private static final String DATABASE_CONNECTION_STRING = String.format("jdbc:sqlite:%s.db", TEST_DATABASE_NAME);
    private final StringWriter writer = new StringWriter();

    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;


    private void execSql(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(DATABASE_CONNECTION_STRING)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @Before
    public void createTestDatabase() throws SQLException {
        execSql("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @Before
    public void setUpMocks() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @After
    public void deleteTestDatabase() throws SQLException {
        execSql("DROP TABLE IF EXISTS PRODUCT");
    }

    private void stripAndCheck(String result, String actual) {
        Assert.assertEquals(actual.strip(), result.strip());
    }

    @Test
    public void emptyTest() throws IOException {
        new GetProductsServlet().doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\n</body></html>");
    }

    @Test
    public void nonEmptyTest() throws IOException, SQLException {
        execSql("INSERT INTO PRODUCT(NAME, PRICE) VALUES ('name1', 1), ('name2', 2)");
        new GetProductsServlet().doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\nname1\t1</br>\nname2\t2</br>\n</body></html>");
    }
}