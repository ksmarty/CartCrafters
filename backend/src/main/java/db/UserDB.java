package db;

import com.lambdaworks.crypto.SCryptUtil;
import dao.UserDAO;
import model.User;

import java.util.Optional;

import static org.javalite.activejdbc.Base.withDb;

public class UserDB implements UserDAO {
    @Override
    public User create(String username, String password) {
        return withDb(() -> User.createIt("username", username, "password", SCryptUtil.scrypt(password, 16384, 8, 1)));
    }

    @Override
    public User createGuest() {
        return withDb(() -> User.createIt("isGuest", true));
    }

    @Override
    public User getById(int id) {
        return withDb(() -> User.findFirst("userId = ?", id));
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return withDb(() -> Optional.ofNullable(User.findFirst("username = ?", username)));
    }

    @Override
    public Optional<Boolean> checkPassword(String username, String password) {
        return getByUsername(username).map(
                user -> SCryptUtil.check(password, user.getString("password")));
    }

    @Override
    public boolean update(User user) {
        return withDb(user::saveIt);
    }
}
