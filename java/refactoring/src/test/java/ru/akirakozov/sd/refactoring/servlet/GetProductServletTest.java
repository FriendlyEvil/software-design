package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;


public class GetProductServletTest extends FakeDatabaseProductTest {
    private final StringWriter writer = new StringWriter();
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;

    @Before
    public void setUpMocks() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

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