package controller;

import db.RootDB;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.server.ServerAcl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.SQLException;

@WebListener
public class Setup implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Starting!");

        try {
            RootDB.init(event.getServletContext());
        } catch (SQLException | SqlToolError | IOException | ServerAcl.AclFormatException e) {
            e.printStackTrace();
        }

        System.out.println("Started!");
    }

    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    }
}
