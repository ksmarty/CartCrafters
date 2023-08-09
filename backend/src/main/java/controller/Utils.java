package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    public static String getRequestedPath(StringBuffer url, String basePath) throws MalformedURLException {
        return new URL(url.toString()).getPath().replace(String.format("/%s/", basePath), "");
    }

    private static boolean isLoggedIn(HttpServletRequest req) {
        return (req.getSession().getAttribute("user") != null);
    }

    public static boolean authCheck(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (isLoggedIn(req)) return false;
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not logged in!");
        return true;
    }

    public static boolean notAuthCheck(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isLoggedIn(req)) return false;
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are already logged in!");
        return true;
    }
}
