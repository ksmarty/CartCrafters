package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static controller.Utils.*;

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
            case LOGGED_IN:
                if (authCheck(req, res)) return;
            case LOGGED_OUT:
                if (notAuthCheck(req, res)) return;
            case NONE:
                r.call(req, res);
        }
    }

    private void fallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
