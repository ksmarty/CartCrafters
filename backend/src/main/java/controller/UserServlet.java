package controller;

import db.UserDB;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static controller.Utils.*;

@WebServlet(urlPatterns = "/user/*", name = "userServlet")
public class UserServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        switch (getRequestedPath(req.getRequestURL(), "user")) {
            case "create":
                if (notAuthCheck(req, res)) return;
                create(req, res);
                break;
            case "login":
                if (notAuthCheck(req, res)) return;
                login(req, res);
                break;
            case "logout":
                if (authCheck(req, res)) return;
                logout(req, res);
                break;
            case "details":
                if (authCheck(req, res)) return;
                getDetails(req, res);
                break;
            default:
                fallback(req, res);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        if (username == null || password == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and/or password is missing");
            return;
        }

        UserDB udb = new UserDB();

        if (udb.getByUsername(username) != null) {
            res.sendError(HttpServletResponse.SC_CONFLICT, String.format("User %s already exists!", username));
            return;
        }

        User user = udb.create(username, password);

        if (user.hasErrors()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, user.errors().toString());
            return;
        }

        req.getSession().setAttribute("user", user);
        res.getWriter().printf("User '%s' created successfully!", user.getString("username"));
    }

    private void login(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        UserDB udb = new UserDB();

        if (!udb.checkPassword(username, password)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username and/or password is incorrect!");
            return;
        }

        req.getSession().setAttribute("user", udb.getByUsername(username));
        res.getWriter().printf("Welcome back %s!", username);
        req.getSession().getId();
    }

    private void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.getSession().removeAttribute("user");
        res.getWriter().println("See ya!");
    }

    private void getDetails(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        res.getWriter().println(user.toString());
    }

    private void fallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
