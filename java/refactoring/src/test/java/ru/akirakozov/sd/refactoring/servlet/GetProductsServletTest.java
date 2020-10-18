package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.IOException;
import java.util.List;


public class GetProductsServletTest extends FakeDatabaseProductsTest {

    private GetProductsServlet servlet;

    @Before
    public void setUp() {
        servlet = new GetProductsServlet(database);
    }

    @Test
    public void emptyTest() throws IOException {
        servlet.doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\n</body></html>");
    }

    @Test
    public void nonEmptyTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2)));
        servlet.doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\nname1\t1</br>\nname2\t2</br>\n</body></html>");
    }
}