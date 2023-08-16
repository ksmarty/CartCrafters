package controller;


import dao.CartDAO;
import db.CartDB;
import model.Cart;

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
        addPaths("cart",
                List.of(
                        new Route("add", this::upsertItem, NONE),
                        new Route("update", this::upsertItem, NONE),
                        new Route("remove", this::removeItem, NONE),
                        new Route("get", this::getItems, NONE)
                ));
    }


    public void upsertItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String path = getRequestedPath(req.getRequestURL(), basePath);

        final String item = req.getParameter("item");
        final String quantityRaw = req.getParameter("qty");

        if (item == null || quantityRaw == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item and/or quantity is missing!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityRaw);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity value is invalid!");
            return;
        }

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(getCurrentUser(req));

        if (path.equals("add"))
            cartDB.addItem(cart, item, quantity);
        else
            cartDB.updateQuantity(cart, item, quantity);

        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }

    public void removeItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String item = req.getParameter("item");

        if (item == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item is missing!");
            return;
        }

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(getCurrentUser(req));
        boolean removed = cartDB.removeItem(cart, item);

        if (!removed) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item not found in cart!");
            return;
        }

        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }

    public void getItems(HttpServletRequest req, HttpServletResponse res) throws IOException {
        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(getCurrentUser(req));
        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }
}
