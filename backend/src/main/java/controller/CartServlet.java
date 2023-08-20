package controller;


import dao.CartDAO;
import db.CartDB;
import db.OrderDB;
import model.Cart;
import model.Order;

import javax.servlet.annotation.WebServlet;
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

    private void checkout() throws IOException {
        final String ccNumber = req.getParameter("ccNumber");
        final String ccExpiryMonth = req.getParameter("ccExpiryMonth");
        final String ccExpiryYear = req.getParameter("ccExpiryYear");
        final String ccCVV = req.getParameter("ccCVV");

        final String shippingAddress = req.getParameter("shippingAddress");
        final String shippingName = req.getParameter("shippingName");

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(req.getCurrentUser());

        Order order = new OrderDB().create(cart);

        if (order == null) {
            res.sendError(HttpServletResponse.SC_CONFLICT, "Item(s) in cart no longer in stock! Please try and purchase again!");
            return;
        }

        // Add payment logic here
        // https://www.fakepay.io/

        res.println(order.toJson(true));
    }


    public void upsertItem() throws IOException {
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
        Cart cart = cartDB.getCart(req.getCurrentUser());

        if (path.equals("add"))
            cartDB.addItem(cart, item, quantity);
        else
            cartDB.updateQuantity(cart, item, quantity);

        res.println(cartDB.getItems(cart).toJson(true));
    }

    public void removeItem() throws IOException {
        final String item = req.getParameter("item");

        if (item == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item is missing!");
            return;
        }

        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(req.getCurrentUser());
        boolean removed = cartDB.removeItem(cart, item);

        if (!removed) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item not found in cart!");
            return;
        }

        res.println(cartDB.getItems(cart).toJson(true));
    }

    public void getItems() throws IOException {
        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(req.getCurrentUser());
        res.println(cartDB.getItems(cart).toJson(true));
    }
}
