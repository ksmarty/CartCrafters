package controller;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static controller.Route.ProtectedRoute.NONE;

@WebServlet(urlPatterns = "/cart/*", name = "cartServlet")
public class CartServlet extends BaseServlet {
    public CartServlet() {
        super();
        addPaths("cart", List.of(new Route("add", this::addItem, NONE)));
    }

    public void addItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().println("Very cool!");
    }
}
