import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Setup implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Starting!");

        try {
            DB.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Started!");
    }

    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    }
}
