package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import java.io.IOException;


public class GetProductsServletTest extends FakeDatabaseProductsTest {
    @Test
    public void emptyTest() throws IOException {
        new GetProductsServlet().doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\n</body></html>");
    }

    @Test
    public void nonEmptyTest() throws IOException {
        execSql("INSERT INTO PRODUCT(NAME, PRICE) VALUES ('name1', 1), ('name2', 2)");
        new GetProductsServlet().doGet(request, response);
        stripAndCheck(writer.toString(), "<html><body>\nname1\t1</br>\nname2\t2</br>\n</body></html>");
    }
}