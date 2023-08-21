package dao;

import model.Cart;
import model.CartItem;
import model.User;
import org.javalite.activejdbc.LazyList;

import java.util.Optional;

public interface CartDAO {
    LazyList<CartItem> getItems(Cart cart);

    Cart getCart(User user);

    Optional<CartItem> getItem(Cart cart, Integer itemId);

    Double getTotal(Cart cart);

    void addItem(Cart cart, Integer itemId, int quantity);

    boolean updateQuantity(Cart cart, Integer itemId, int quantity);

    boolean removeItem(Cart cart, Integer itemId);
}
