package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final Database<Product> database;

    public GetProductsServlet(Database<Product> database) {
        this.database = database;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("<html><body>");
        List<Product> products = database.selectAll();
        for (Product product : products) {
            response.getWriter().print(product.toHtml());
            response.getWriter().println("</br>");
        }
        response.getWriter().println("</body></html>");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
