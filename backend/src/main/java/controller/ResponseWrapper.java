package controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWrapper {

    private final HttpServletResponse res;

    public ResponseWrapper(HttpServletResponse res) {
        res.setContentType("application/json");

        this.res = res;
    }

    public void sendError(int code, String message) throws IOException {
        res.sendError(code, message);
    }

    public void println(String message) throws IOException {
        res.getWriter().println(message);
    }

    public void println(Object obj) throws IOException {
        res.getWriter().println(obj);
    }

    public void printf(String format, Object... args) throws IOException {
        res.getWriter().printf(format, args);
    }
}
