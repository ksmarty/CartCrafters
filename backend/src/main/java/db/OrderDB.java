package db;

import dao.CartDAO;
import dao.OrderDAO;
import model.*;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.javalite.activejdbc.Base.withDb;

public class OrderDB implements OrderDAO {
    @Override
    public Order create(Cart cart) {
        return withDb(() -> {
            CartDAO cdb = new CartDB();

            Order order = Order.createIt("totalAmount", cdb.getTotal(cart));

            new UserDB().getById(cart.getInteger("userId")).add(order);

            LazyList<CartItem> cartItems = cdb.getItems(cart);

            // Verify all items are in stock
            for (CartItem item : cartItems) {
                Product product = item.parent(Product.class);
                int productQuantity = product.getInteger("quantity");
                int purchaseQuantity = item.getInteger("quantity");
                int net = productQuantity - purchaseQuantity;

                if (net < 0) {
                    cart.remove(item);
                    return null;
                }

                product.setInteger("quantity", net);
            }

            // Update stock
            cartItems.forEach(e -> e.parent(Product.class).saveIt());

            // Create order items & add to order
            cartItems.stream().map(item -> {
                OrderItem orderItem = new OrderItem()
                        .set("productId", item.parent(Product.class).getId())
                        .set("quantity", item.getInteger("quantity"))
                        .set("amount", item.parent(Product.class).getDouble("price"));
                orderItem.saveIt();
                return orderItem;
            }).forEach(order::add);

            // Delete cart
            cart.deleteCascade();

            return order;
        });
    }

    @Override
    public Order getUserOrder(User user, int orderId) {
        return withDb(() -> user.get(Order.class, "orderId = ?", orderId).stream().findFirst().orElse(null));
    }

    @Override
    public Optional<Order> getOrder(int orderId) {
        return withDb(() -> Optional.ofNullable(Order.findById(orderId)));
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return withDb(() -> user.getAll(Order.class).load());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderItem> getOrderItems(Order order) {
        return withDb(() -> order.getAll(OrderItem.class).include(Product.class));
    }

    @Override
    public List<Order> getAllOrders() {
        return withDb(() -> Order.findAll().load());
    }

    @Override
    public String toJSON(List<? extends Model> orders) {
        return orders.stream()
                .map(m -> m.toJson(true))
                .collect(Collectors.joining(",", "[", "]"));
    }
}
