package dao;

import model.Cart;
import model.Order;
import model.OrderItem;
import model.User;

import java.util.List;

public interface OrderDAO {
    Order create(Cart cart);

    Order getUserOrder(User user, String orderId);

    List<Order> getUserOrders(User user);

    List<OrderItem> getOrderItems(Order order);

    List<Order> getAllOrders();
}
