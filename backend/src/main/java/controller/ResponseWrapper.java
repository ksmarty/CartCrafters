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

    private void send(String key, String value) {
        println(String.format("{\"%s\": \"%s\"}", key, value));
    }

    public void sendError(int code, String message) {
        res.setStatus(code);
        send("error", message);
    }

    public void sendResponse(String message) {
        send("message", message);
    }

    public void sendResponse(String format, Object... args) {
        sendResponse(String.format(format, args));
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

    public void setHeader(String key, String value) {
        res.setHeader(key, value);
    }
}
