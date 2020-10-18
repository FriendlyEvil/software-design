package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Assert;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;


public class AddProductsServletTest extends FakeDatabaseProductsTest {

    @Test(expected = Exception.class)
    public void emptyParamsTest() throws IOException {
        new AddProductServlet().doGet(request, response);
    }

    @Test
    public void simpleTest() throws IOException {
        when(request.getParameter("name")).thenReturn("test name");
        when(request.getParameter("price")).thenReturn("10");
        new AddProductServlet().doGet(request, response);
        stripAndCheck("OK", writer.toString());
        List<Product> result = database.selectAll();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test name", result.get(0).getName());
        Assert.assertEquals(10, result.get(0).getPrice());
    }
}