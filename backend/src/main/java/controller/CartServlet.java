package controller;


import dao.CartDAO;
import db.CartDB;
import db.OrderDB;
import model.Cart;
import model.Order;

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
                        new Route("get", this::getItems, NONE),
                        new Route("checkout", this::checkout, NONE)
                ));
    }

    private void checkout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String ccNumber = rw.getParameter("ccNumber");
        final String ccExpiryMonth = rw.getParameter("ccExpiryMonth");
        final String ccExpiryYear = rw.getParameter("ccExpiryYear");
        final String ccCVV = rw.getParameter("ccCVV");

        final String shippingAddress = rw.getParameter("shippingAddress");
        final String shippingName = rw.getParameter("shippingName");

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(rw.getCurrentUser());

        Order order = new OrderDB().create(cart);

        if (order == null) {
            res.sendError(HttpServletResponse.SC_CONFLICT, "Item(s) in cart no longer in stock! Please try and purchase again!");
            return;
        }

        // Add payment logic here
        // https://www.fakepay.io/

        res.getWriter().println(order.toJson(true));
    }


    public void upsertItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String path = getRequestedPath(rw.getRequestURL(), basePath);

        final String item = rw.getParameter("item");
        final String quantityRaw = rw.getParameter("qty");

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
        Cart cart = cartDB.getCart(rw.getCurrentUser());

        if (path.equals("add"))
            cartDB.addItem(cart, item, quantity);
        else
            cartDB.updateQuantity(cart, item, quantity);

        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }

    public void removeItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String item = rw.getParameter("item");

        if (item == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item is missing!");
            return;
        }

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(rw.getCurrentUser());
        boolean removed = cartDB.removeItem(cart, item);

        if (!removed) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item not found in cart!");
            return;
        }

        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }

    public void getItems(HttpServletRequest req, HttpServletResponse res) throws IOException {
        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(rw.getCurrentUser());
        res.getWriter().println(cartDB.getItems(cart).toJson(true));
    }
}
