package dao;

import model.Cart;
import model.Order;
import model.OrderItem;
import model.User;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order create(Cart cart);

    Optional<Order> getUserOrder(User user, int orderId);

    Optional<Order> getOrder(int orderId);

    List<Order> getUserOrders(User user);

    List<OrderItem> getOrderItems(Order order);

    List<Order> getAllOrders();
}
