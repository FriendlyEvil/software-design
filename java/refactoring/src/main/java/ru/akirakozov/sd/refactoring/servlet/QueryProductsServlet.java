package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.writer.HtmlBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryProductsServlet extends HttpServlet {
    private final Database<Product> database;

    public QueryProductsServlet(Database<Product> database) {
        this.database = database;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if (command == null) {
            response.getWriter().println("parameter 'command' don't set");

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HtmlBuilder htmlBuilder = new HtmlBuilder();
        switch (command) {
            case "max":
                htmlBuilder.appendH1Tag("Product with max price: ");
                Product product = database.max();
                if (product != null) {
                    htmlBuilder.appendText(product.toHtml());
                }
                break;
            case "min":
                htmlBuilder.appendH1Tag("Product with min price: ");
                product = database.min();
                if (product != null) {
                    htmlBuilder.appendText(product.toHtml());
                }
                break;
            case "sum":
                htmlBuilder.appendText("Summary price: ");
                int sum = database.sum();
                htmlBuilder.appendText(String.valueOf(sum));
                break;
            case "count":
                htmlBuilder.appendText("Number of products: ");
                int count = database.count();
                htmlBuilder.appendText(String.valueOf(count));
                break;
            default:
                htmlBuilder.clearStartAndEnd();
                htmlBuilder.appendText("Unknown command: " + command);

        }
        response.getWriter().println(htmlBuilder.toString());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
