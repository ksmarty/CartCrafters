package dao;

import model.Cart;
import model.Order;
import model.OrderItem;
import model.User;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order create(Cart cart);

    Order getUserOrder(User user, int orderId);

    Optional<Order> getOrder(int orderId);

    List<Order> getUserOrders(User user);

    List<OrderItem> getOrderItems(Order order);

    List<Order> getAllOrders();

    String toJSON(List<? extends Model> models);
}
