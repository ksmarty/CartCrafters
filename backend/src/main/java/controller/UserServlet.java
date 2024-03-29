package controller;

import db.UserDB;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
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

    private void create() {
        req.getParameter("username").ifPresentOrElse(
                (username) ->
                        req.getParameter("password").ifPresentOrElse(
                                password -> {
                                    UserDB udb = new UserDB();

                                    udb.getByUsername(username).ifPresentOrElse(
                                            user -> res.sendError(HttpServletResponse.SC_CONFLICT, String.format("User %s already exists!", username)),
                                            () -> udb.create(username, password).ifPresent(
                                                    user -> {
                                                        if (user.hasErrors()) {
                                                            res.sendError(HttpServletResponse.SC_BAD_REQUEST, user.errors().toString());
                                                            return;
                                                        }

                                                        req.getSession().setAttribute("user", user);
                                                        res.sendResponse("User '%s' created successfully!", user.getString("username"));
                                                        log("User '%s' created successfully!", user.getString("username"));
                                                    }
                                            ));
                                },
                                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is missing!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is missing!"));
    }

    private void login() {
        req.getParameter("username").ifPresentOrElse(
                username -> req.getParameter("password").ifPresentOrElse(
                        password -> new UserDB().login(username, password).ifPresentOrElse(
                                user -> {
                                    req.getSession().setAttribute("user", user);
                                    res.sendResponse("Welcome back %s!", username);
                                    log("User %s logged in", user.getString("username"));
                                },
                                () -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username and/or password is incorrect!")),
                        () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is missing!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is missing!"));
    }

    private void logout() {
        log("User %s logged out", req.getCurrentUser().getString("username"));
        req.getSession().removeAttribute("user");
        res.sendResponse("See ya!");
    }

    private void getDetails() {
        User user = req.getCurrentUser();
        res.println(user.toJson(true));
    }
}
