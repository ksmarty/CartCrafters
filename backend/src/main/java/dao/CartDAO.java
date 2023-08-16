package dao;

import model.Cart;
import model.CartItem;
import model.User;
import org.javalite.activejdbc.LazyList;

public interface CartDAO {
    LazyList<CartItem> getItems(Cart cart);

    Cart getCart(User user);

    CartItem getItem(Cart cart, String itemId);

    boolean addItem(Cart cart, String itemId, int quantity);

    boolean updateQuantity(Cart cart, String itemId, int quantity);

    boolean removeItem(Cart cart, String itemId);
}
