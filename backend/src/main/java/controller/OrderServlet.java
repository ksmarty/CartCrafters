package controller;

import com.google.gson.Gson;
import dao.OrderDAO;
import db.OrderDB;
import model.Order;
import model.OrderItem;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                        new Route("getAll", this::getAllOrders, ADMIN)
                ));
    }


    private void getOrders(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = getCurrentUser(req);
        List<Order> orders = new OrderDB().getUserOrders(user);
        res.getWriter().println(new Gson().toJson(orders));
    }

    private void getOrderItems(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String orderNumber = req.getParameter("order");
        User user = getCurrentUser(req);
        OrderDAO odb = new OrderDB();

        Order order = odb.getUserOrder(user, orderNumber);
        List<OrderItem> orderItems = odb.getOrderItems(order);
        res.getWriter().println(new Gson().toJson(orderItems));
    }

    private void getAllOrders(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().println(new Gson().toJson(new OrderDB().getAllOrders()));
    }
}
