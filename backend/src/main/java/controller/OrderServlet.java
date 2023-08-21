package controller;

import dao.OrderDAO;
import db.OrderDB;
import model.Order;
import model.OrderItem;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static controller.Route.ProtectedRoute.ADMIN;
import static controller.Route.ProtectedRoute.LOGGED_IN;

@WebServlet(urlPatterns = "/order/*", name = "orderServlet")
public class OrderServlet extends BaseServlet {
    public OrderServlet() {
        super();
        addPaths("order",
                List.of(
                        new Route("get", this::getOrders, LOGGED_IN),
                        new Route("getItems", this::getOrderItems, LOGGED_IN),
                        new Route("getAll", this::getAllOrders, ADMIN),
                        new Route("getItemsAdmin", this::getOrderItemsAdmin, ADMIN)
                ));
    }


    private void getOrders() {
        User user = req.getCurrentUser();
        OrderDAO odb = new OrderDB();
        List<Order> orders = odb.getUserOrders(user);
        res.println(odb.toJSON(orders));
    }

    private void getOrderItems() {
        req.getParameterInt("order").ifPresentOrElse(
                orderNumber -> {
                    OrderDAO odb = new OrderDB();
                    Order order = odb.getUserOrder(req.getCurrentUser(), orderNumber);
                    if (order == null) {
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Order does not belong to user!");
                        return;
                    }

                    List<OrderItem> orderItems = odb.getOrderItems(order);
                    res.println(odb.toJSON(orderItems));
                },
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order number is not valid!")
        );
    }

    private void getAllOrders() {
        OrderDAO odb = new OrderDB();
        res.println(odb.toJSON(odb.getAllOrders()));
    }

    private void getOrderItemsAdmin() {
        req.getParameterInt("order").ifPresentOrElse(
                orderNumber -> {
                    OrderDAO odb = new OrderDB();
                    odb.getOrder(orderNumber).ifPresentOrElse(
                            order -> res.println(odb.toJSON(odb.getOrderItems(order))),
                            () -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Order does not exist!"));
                },
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order number is not valid!"));
    }
}
