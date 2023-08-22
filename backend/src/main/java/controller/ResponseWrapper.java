package controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseWrapper {

    private final HttpServletResponse res;

    public ResponseWrapper(HttpServletResponse res) {
        res.setContentType("application/json");

        this.res = res;
    }

    public void sendError(int code, String message) {
        res.setStatus(code);
        println(String.format("{\"error\": \"%s\"}", message));
    }

    public void println(String message) {
        try {
            res.getWriter().println(message);
        } catch (IOException e) {
            // Shhh nothing happened dw about it
        }
    }

    public void println(Object obj) {
        try {
            res.getWriter().println(obj);
        } catch (IOException e) {
            // Shhh nothing happened dw about it
        }
    }

    public void printf(String format, Object... args) {
        try {
            res.getWriter().printf(format, args);
        } catch (IOException e) {
            // Shhh nothing happened dw about it
        }
    }

    public void setContentType(String contentType) {
        res.setContentType(contentType);
    }

    public OutputStream getOutputStream() {
        try {
            return res.getOutputStream();
        } catch (IOException e) {
            return null;
            // Shhh nothing happened dw about it
        }
    }
}
