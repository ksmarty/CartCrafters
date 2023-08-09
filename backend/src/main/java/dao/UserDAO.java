package dao;

import model.User;

public interface UserDAO {
    User create(String username, String password);

    boolean update(User user);

    User getById(int id);

    User getByUsername(String username);

    Boolean checkPassword(String username, String password);
}
