package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.database.ProductDatabase;
import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;

public class FakeDatabaseProductsTest {
    private static final String DEFAULT_DATABASE_CONNECTION_STRING = "jdbc:sqlite:fake-test.db";
    protected final Database<Product> database;

    protected StringWriter writer = new StringWriter();
    @Mock
    protected HttpServletResponse response;
    @Mock
    protected HttpServletRequest request;

    public FakeDatabaseProductsTest() {
        this(DEFAULT_DATABASE_CONNECTION_STRING);
    }

    public FakeDatabaseProductsTest(String connectionString) {
        this.database = new ProductDatabase(connectionString);
    }

    @Before
    public void setUpMocks() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Before
    public void createTestDatabase() {
        database.createIfNotExist();
    }

    @After
    public void deleteTestDatabase() {
        database.dropIfExist();
    }

    protected void stripAndCheck(String result, String actual) {
        Assert.assertEquals(actual.strip(), result.strip());
    }

}
