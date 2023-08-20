package controller;

import java.io.IOException;

public class Route {
    public String path;
    public ServletMethod method;
    public ProtectedRoute protectedRoute;

    public enum ProtectedRoute {
        LOGGED_IN,
        LOGGED_OUT,
        NONE,
        ADMIN
    }

    public Route(String path, ServletMethod method, ProtectedRoute protectedRoute) {
        this.path = path;
        this.method = method;
        this.protectedRoute = protectedRoute;
    }

    public void call() throws IOException {
        method.f();
    }
}
