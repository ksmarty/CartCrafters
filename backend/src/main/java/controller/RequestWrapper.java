package controller;

import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    public String getParameter(String query) {
        return req.getParameter(query);
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
}
