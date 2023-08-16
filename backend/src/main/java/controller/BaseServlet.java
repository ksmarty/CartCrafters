package controller;

import db.UserDB;
import model.User;
import org.javalite.activejdbc.Base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class BaseServlet extends HttpServlet {

    String basePath;
    List<Route> routes;

    public BaseServlet() {}

    public void addPaths(String basePath, List<Route> routes) {
        this.basePath = basePath;
        this.routes = routes;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String path = getRequestedPath(req.getRequestURL(), basePath);

        res.setContentType("application/json");

        Route r =
                routes.stream()
                        .filter(e -> Objects.equals(e.path, path))
                        .findFirst()
                        .orElse(new Route("", this::fallback, Route.ProtectedRoute.NONE));

        switch (r.protectedRoute) {
            case LOGGED_IN -> {
                if (isLoggedOut(req)) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not logged in!");
                    return;
                }
            }
            case LOGGED_OUT -> {
                if (isLoggedIn(req)) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are already logged in!");
                    return;
                }
            }
            case NONE -> {
                if (isLoggedOut(req) && !isGuest(req)) {
                    req.getSession().setAttribute("user", new UserDB().createGuest());
                }
            }
        }

        Base.open("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:cartcrafters", "sa", "");
        r.call(req, res);
        Base.close();
    }

    private void fallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad endpoint!");
    }

    public static String getRequestedPath(StringBuffer url, String basePath) throws MalformedURLException {
        return new URL(url.toString()).getPath().replace(String.format("/%s/", basePath), "");
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        User user = getCurrentUser(req);
        return (user != null && !user.getBoolean("isGuest"));
    }

    public static boolean isLoggedOut(HttpServletRequest req) {
        return !isLoggedIn(req);
    }

    public static boolean isGuest(HttpServletRequest req) {
        User user = getCurrentUser(req);
        return (user != null && user.getBoolean("isGuest"));
    }

    public static User getCurrentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }
}
