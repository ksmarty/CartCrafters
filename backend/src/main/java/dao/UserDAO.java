package dao;

import model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> create(String username, String password);

    User createGuest();

    boolean update(User user);

    User getById(int id);

    Optional<User> getByUsername(String username);

    Optional<User> login(String username, String password);
}
