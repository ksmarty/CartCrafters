package db;

import dao.CartDAO;
import model.Cart;
import model.CartItem;
import model.Product;
import model.User;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.Optional;

import static org.javalite.activejdbc.Base.withDb;

public class CartDB implements CartDAO {

    @SuppressWarnings("unchecked")
    @Override
    public LazyList<CartItem> getItems(Cart cart) {
        return withDb(() -> cart.getAll(CartItem.class).include(Product.class));
    }

    @Override
    public Cart getCart(User user) {
        return withDb(() -> (Cart) Optional
                .ofNullable(Cart.findFirst("userId = ?", user.getId()))
                .orElseGet(() -> {
                    Cart c = Cart.createIt();
                    user.add(c);
                    return c;
                }));
    }

    @Override
    public Optional<CartItem> getItem(Cart cart, Integer itemId) {
        return withDb(() -> cart.get(CartItem.class, "productId = ?", itemId).stream().findFirst());
    }

    @Override
    public Double getTotal(Cart cart) {
        return withDb(() -> getItems(cart).stream().reduce(0.0, (total, item) -> total + item.getDouble("quantity") * item.parent(Product.class).getDouble("price"), Double::sum));
    }

    @Override
    public void addItem(Cart cart, Integer itemId, int quantity) {
        withDb(() -> {
            getItem(cart, itemId).ifPresentOrElse(
                    e -> e.set("quantity", e.getInteger("quantity") + quantity).saveIt(),
                    () -> cart.add(CartItem.createIt("productId", itemId, "quantity", quantity)));
            return null;
        });
    }

    @Override
    public boolean updateQuantity(Cart cart, Integer itemId, int quantity) {
        if (quantity == 0) return removeItem(cart, itemId);

        return withDb(() -> getItem(cart, itemId)
                .map(item -> item.set("quantity", quantity).saveIt())
                .orElse(false)
        );
    }

    @Override
    public boolean removeItem(Cart cart, Integer itemId) {
        return withDb(() -> getItem(cart, itemId)
                .map(Model::delete)
                .orElse(false)
        );
    }
}
