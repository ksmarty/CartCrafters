package controller;

import db.UserDB;
import org.javalite.activejdbc.Base;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static controller.Route.ProtectedRoute.NONE;

@WebServlet(value = "/")
public class BaseServlet extends HttpServlet {

    String basePath;
    List<Route> routes;

    RequestWrapper req;
    ResponseWrapper res;

    public BaseServlet() {}

    public void addPaths(String basePath, List<Route> routes) {
        this.basePath = basePath;
        this.routes = routes;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        req = new RequestWrapper(request);
        res = new ResponseWrapper(response);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        final String path = getRequestedPath(req.getRequestURL(), basePath);

        if (routes == null) routes = new ArrayList<>();

        Route r = path.equals("/")
                ? new Route("", this::root, NONE)
                : routes.stream()
                .filter(e -> Objects.equals(e.path, path))
                .findFirst()
                .orElse(new Route("", this::fallback, NONE));

        switch (r.protectedRoute) {
            case LOGGED_IN -> {
                if (req.isLoggedOut()) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not logged in!");
                    return;
                }
            }
            case LOGGED_OUT -> {
                if (req.isLoggedIn()) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are already logged in!");
                    return;
                }
            }
            case NONE -> {
                if (req.isLoggedOut() && !req.isGuest()) {
                    req.getSession().setAttribute("user", new UserDB().createGuest());
                }
            }
            case ADMIN -> {
                if (!req.getCurrentUser().getBoolean("isAdmin")) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an admin!");
                    return;
                }
            }
        }

        Base.open("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:cartcrafters", "sa", "");
        r.call();
        Base.close();
    }

    private void fallback() {
        res.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad endpoint!");
    }

    private void root() {
        res.println("Welcome to the CartCrafters API!");
    }

    public static String getRequestedPath(StringBuffer url, String basePath) throws MalformedURLException {
        return new URL(url.toString()).getPath().replace(String.format("/%s/", basePath), "");
    }
}
