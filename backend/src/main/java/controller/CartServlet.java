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

    private void checkout() {
        final String ccNumber = req.getParameter("ccNumber").orElse("");
        final String ccExpiryMonth = req.getParameter("ccExpiryMonth").orElse("");
        final String ccExpiryYear = req.getParameter("ccExpiryYear").orElse("");
        final String ccCVV = req.getParameter("ccCVV").orElse("");

        final String shippingAddress = req.getParameter("shippingAddress").orElse("");
        final String shippingName = req.getParameter("shippingName").orElse("");

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

        req.getParameterInt("item").ifPresentOrElse(
                item -> req.getParameterInt("qty").ifPresentOrElse(
                        quantity -> {
                            CartDAO cartDB = new CartDB();
                            Cart cart = cartDB.getCart(req.getCurrentUser());

                            if (path.equals("add"))
                                cartDB.addItem(cart, item, quantity);
                            else
                                cartDB.updateQuantity(cart, item, quantity);

                            res.println(cartDB.getItems(cart).toJson(true));
                        },
                        () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity is missing or not a number!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item number is missing or not a number!"));
    }

    public void removeItem() {
        req.getParameterInt("item").ifPresentOrElse(
                item -> {
                    CartDAO cartDB = new CartDB();
                    Cart cart = cartDB.getCart(req.getCurrentUser());
                    boolean removed = cartDB.removeItem(cart, item);

                    if (!removed) {
                        res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item not found in cart!");
                        return;
                    }

                    res.println(cartDB.getItems(cart).toJson(true));
                },
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item is missing or not a number!"));
    }

    public void getItems() {
        CartDAO cartDB = new CartDB();
        Cart cart = cartDB.getCart(req.getCurrentUser());
        res.println(cartDB.getItems(cart).toJson(true));
    }
}
