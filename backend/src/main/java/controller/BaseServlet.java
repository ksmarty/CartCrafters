package controller;

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
            }
        }

        r.call(req, res);
    }

    private void fallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    public static String getRequestedPath(StringBuffer url, String basePath) throws MalformedURLException {
        return new URL(url.toString()).getPath().replace(String.format("/%s/", basePath), "");
    }

    private static boolean isLoggedOut(HttpServletRequest req) {
        return (req.getSession().getAttribute("user") == null);
    }

    private static boolean isLoggedIn(HttpServletRequest req) {
        return !isLoggedOut(req);
    }
}
