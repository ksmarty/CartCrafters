package controller;

import db.RootDB;
import db.UserDB;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Replace with your client's origin
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

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
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are already logged in!");
                    return;
                }
            }
            case NONE -> {
                if (req.isLoggedOut() && !req.isGuest()) {
                    req.getSession().setAttribute("user", new UserDB().createGuest());
                }
            }
            case ADMIN -> {
                if (req.isLoggedOut() || !req.getCurrentUser().getBoolean("isAdmin")) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not an admin!");
                    return;
                }
            }
        }


        try {
            Base.open("org.hsqldb.jdbcDriver", RootDB.DB_URL, "sa", "");
            r.call();
        } finally {
            Base.close();
        }
    }

    private void fallback() {
        res.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad endpoint!");
    }

    private void root() {
        res.sendResponse("Welcome to the CartCrafters API!");
    }

    public static String getRequestedPath(StringBuffer url, String basePath) throws MalformedURLException {
        return new URL(url.toString()).getPath().replace(String.format("/%s/", basePath), "");
    }

    public void log(String message) {
        System.out.printf("[%s] %s\n", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()), message);
    }

    public void log(String format, Object... args) {
        log(String.format(format, args));
    }

    public String toJSON(List<? extends Model> orders) {
        return orders.stream()
                .map(m -> m.toJson(true))
                .collect(Collectors.joining(",", "[", "]"));
    }
}


