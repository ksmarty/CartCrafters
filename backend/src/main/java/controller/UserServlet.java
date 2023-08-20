package controller;

import db.UserDB;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static controller.Route.ProtectedRoute.LOGGED_IN;
import static controller.Route.ProtectedRoute.LOGGED_OUT;

@WebServlet(urlPatterns = "/user/*", name = "userServlet")
public class UserServlet extends BaseServlet {

    public UserServlet() {
        super();
        addPaths("user", List.of(
                new Route("create", this::create, LOGGED_OUT),
                new Route("login", this::login, LOGGED_OUT),
                new Route("logout", this::logout, LOGGED_IN),
                new Route("details", this::getDetails, LOGGED_IN)
        ));
    }

    private void create() throws IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        if (username == null || password == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and/or password is missing");
            return;
        }

        UserDB udb = new UserDB();

        if (udb.getByUsername(username) != null) {
            res.sendError(HttpServletResponse.SC_CONFLICT, String.format("User %s already exists!", username));
            return;
        }

        User user = udb.create(username, password);

        if (user.hasErrors()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, user.errors().toString());
            return;
        }

        req.getSession().setAttribute("user", user);
        res.printf("User '%s' created successfully!", user.getString("username"));
    }

    private void login() throws IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        UserDB udb = new UserDB();

        if (!udb.checkPassword(username, password)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username and/or password is incorrect!");
            return;
        }

        req.getSession().setAttribute("user", udb.getByUsername(username));
        res.printf("Welcome back %s!", username);
        req.getSession().getId();
    }

    private void logout() throws IOException {
        req.getSession().removeAttribute("user");
        res.println("See ya!");
    }

    private void getDetails() throws IOException {
        User user = req.getCurrentUser();
        res.println(user.toString());
    }
}
