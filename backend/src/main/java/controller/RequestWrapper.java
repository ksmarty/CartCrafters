package controller;

import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

public class RequestWrapper {
    private final HttpServletRequest req;

    public RequestWrapper(HttpServletRequest req) {
        this.req = req;
    }

    public boolean isLoggedIn() {
        User user = getCurrentUser();
        return (user != null && !user.getBoolean("isGuest"));
    }

    public boolean isLoggedOut() {
        return !isLoggedIn();
    }

    public boolean isGuest() {
        User user = getCurrentUser();
        return (user != null && user.getBoolean("isGuest"));
    }

    public User getCurrentUser() {
        return (User) getSession().getAttribute("user");
    }

    public Optional<String> getParameter(String query) {
        return Optional.ofNullable(req.getParameter(query));
    }

    public Optional<Integer> getParameterInt(String query) {
        String s = req.getParameter(query);
        try {
            Integer i = Integer.parseInt(s);
            return Optional.of(i);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public HttpSession getSession() {
        return req.getSession();
    }

    public StringBuffer getRequestURL() {
        return req.getRequestURL();
    }

    public Optional<Part> getPart(String part) {
        try {
            return Optional.ofNullable(req.getPart(part));
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
