package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Route {
    public String path;
    public ServletMethod method;
    public ProtectedRoute protectedRoute;

    public enum ProtectedRoute {
        LOGGED_IN,
        LOGGED_OUT,
        NONE
    }

    public Route(String path, ServletMethod method, ProtectedRoute protectedRoute) {
        this.path = path;
        this.method = method;
        this.protectedRoute = protectedRoute;
    }

    public void call(HttpServletRequest req, HttpServletResponse res) throws IOException {
        method.f(req, res);
    }
}
