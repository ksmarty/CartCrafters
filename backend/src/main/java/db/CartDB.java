package db;

import dao.CartDAO;
import model.Cart;
import model.CartItem;
import model.Product;
import model.User;
import org.javalite.activejdbc.LazyList;

import static org.javalite.activejdbc.Base.withDb;

public class CartDB implements CartDAO {

    @SuppressWarnings("unchecked")
    @Override
    public LazyList<CartItem> getItems(Cart cart) {
        return withDb(() -> cart.getAll(CartItem.class).include(Product.class));
    }

    @Override
    public Cart getCart(User user) {
        return withDb(() -> {
            Cart cart = Cart.findFirst("userId = ?", user.getId());
            if (cart == null) {
                cart = Cart.createIt();
                user.add(cart);
            }
            return cart;
        });
    }

    @Override
    public CartItem getItem(Cart cart, String itemId) {
        return withDb(() -> cart.get(CartItem.class, "productId = ?", itemId).stream().findFirst().orElse(null));
    }

    @Override
    public Double getTotal(Cart cart) {
        return withDb(() -> getItems(cart).stream().reduce(0.0, (total, item) -> total + item.getDouble("quantity") * item.parent(Product.class).getDouble("price"), Double::sum));
    }

    @Override
    public boolean addItem(Cart cart, String itemId, int quantity) {
        return withDb(() -> {
            CartItem cartItem = getItem(cart, itemId);
            if (cartItem == null) {
                cartItem = CartItem.createIt("productId", itemId, "quantity", quantity);
                cart.add(cartItem);
            } else
                cartItem.set("quantity", cartItem.getInteger("quantity") + quantity).saveIt();
            return cartItem != null;
        });
    }

    @Override
    public boolean updateQuantity(Cart cart, String itemId, int quantity) {
        if (quantity == 0) return removeItem(cart, itemId);

        return withDb(() -> {
            CartItem cartItem = getItem(cart, itemId);
            if (cartItem == null) return false;
            return cartItem.set("quantity", quantity).saveIt();
        });
    }

    @Override
    public boolean removeItem(Cart cart, String itemId) {
        return withDb(() -> {
            CartItem cartItem = getItem(cart, itemId);
            if (cartItem == null) return false;
            return cartItem.delete();
        });
    }
}
