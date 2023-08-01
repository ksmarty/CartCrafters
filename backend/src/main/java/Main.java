import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(value = "/", name = "mainServlet")
public class Main extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        res.getWriter().println("Wow " + java.time.LocalDateTime.now());
        System.out.println("------------------------");
        DB.test();
        System.out.println("------------------------");
    }
}
