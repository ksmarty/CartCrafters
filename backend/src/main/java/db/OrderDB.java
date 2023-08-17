package db;

import dao.CartDAO;
import dao.OrderDAO;
import model.*;
import org.javalite.activejdbc.LazyList;

import java.util.List;

import static org.javalite.activejdbc.Base.withDb;

public class OrderDB implements OrderDAO {
    @Override
    public Order create(Cart cart) {
        return withDb(() -> {
            CartDAO cdb = new CartDB();

            Order order = Order.createIt("totalAmount", cdb.getTotal(cart));

            new UserDB().getByUsername(cart.getString("userId")).add(order);

            LazyList<CartItem> cartItems = cdb.getItems(cart);

            for (CartItem item : cartItems) {
                Product product = item.parent(Product.class);
                int productQuantity = product.getInteger("quantity");
                int purchaseQuantity = item.getInteger("quantity");
                int net = productQuantity - purchaseQuantity;

                if (net < 0) {
                    cart.remove(item);
                    return null;
                }

                product.setInteger("quantity", net).saveIt();
            }

            cartItems.stream().map(item -> {
                OrderItem orderItem = new OrderItem()
                        .set("productId", item.parent(Product.class).getId())
                        .set("quantity", item.getInteger("quantity"))
                        .set("amount", item.parent(Product.class).getDouble("price"));
                orderItem.saveIt();
                return orderItem;
            }).forEach(order::add);

            cart.deleteCascade();

            return order;
        });
    }

    @Override
    public Order getUserOrder(User user, String orderId) {
        return withDb(() -> user.get(Order.class, "orderId = ?", orderId).stream().findFirst().orElse(null));
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return withDb(() -> user.getAll(Order.class).load());
    }

    @Override
    public List<OrderItem> getOrderItems(Order order) {
        // return withDb(() -> order.getAll(OrderItem.class).include(Product.class));
        return withDb(() -> order.getAll(OrderItem.class).load());
    }

    @Override
    public List<Order> getAllOrders() {
        return withDb(() -> Order.findAll().load());
    }
}
