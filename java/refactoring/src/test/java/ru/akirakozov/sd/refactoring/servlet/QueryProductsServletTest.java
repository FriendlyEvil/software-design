package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

public class QueryProductsServletTest extends FakeDatabaseProductsTest {
    private QueryProductsServlet servlet;

    @Before
    public void setUp() {
        servlet = new QueryProductsServlet(database);
    }

    private void testCommand(String command, String result) throws IOException {
        when(request.getParameter("command")).thenReturn(command);
        servlet.doGet(request, response);
        stripAndCheck(writer.toString(), result);
    }

    @Test
    public void emptyTest() throws IOException {
        servlet.doGet(request, response);
        stripAndCheck(writer.toString(), "parameter 'command' don't set");
    }

    @Test
    public void emptyIncorrectCommandTest() throws IOException {
        testCommand("test command", "Unknown command: test command");
    }

    private void maxTest(String result) throws IOException {
        testCommand("max", "<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                result +
                "</body></html>");
    }

    @Test
    public void emptyMaxCommandTest() throws IOException {
        maxTest("");
    }

    @Test
    public void simpleMaxCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2)));
        maxTest("name2\t2</br>\n");
    }

    @Test
    public void maxCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2), new Product("name3", 2)));
        maxTest("name2\t2</br>\n");
    }

    private void minTest(String result) throws IOException {
        testCommand("min", "<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                result +
                "</body></html>");
    }

    @Test
    public void emptyMinCommandTest() throws IOException {
        minTest("");
    }

    @Test
    public void simpleMinCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2)));
        minTest("name1\t1</br>\n");
    }

    @Test
    public void minCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2), new Product("name3", 1)));
        minTest("name1\t1</br>\n");
    }

    private void sumTest(String result) throws IOException {
        testCommand("sum", "<html><body>\n" +
                "Summary price: \n" +
                result +
                "</body></html>");
    }

    @Test
    public void emptySumCommandTest() throws IOException {
        sumTest("0\n");
    }

    @Test
    public void simpleSumCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2)));
        sumTest("3\n");
    }

    private void countTest(String result) throws IOException {
        testCommand("count", "<html><body>\n" +
                "Number of products: \n" +
                result +
                "</body></html>");
    }

    @Test
    public void emptyCountCommandTest() throws IOException {
        countTest("0\n");
    }

    @Test
    public void simpleCountCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2)));
        countTest("2\n");
    }

    @Test
    public void countCommandTest() throws IOException {
        database.insert(List.of(new Product("name1", 1), new Product("name2", 2), new Product("name3", 3)));
        countTest("3\n");
    }
}