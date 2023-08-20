package controller;

import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestWrapper {
    HttpServletRequest req;

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

    public HttpSession getSession() {
        return req.getSession();
    }

    public StringBuffer getRequestURL() {
        return req.getRequestURL();
    }
}
