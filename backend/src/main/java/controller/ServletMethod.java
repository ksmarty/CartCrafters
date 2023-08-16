package controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface ServletMethod {
    void f(HttpServletRequest req, HttpServletResponse res) throws IOException;
}
