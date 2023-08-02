import model.User;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.jdbc.JDBCDataSource;
import org.javalite.activejdbc.connection_config.ConnectionJdbcConfig;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.javalite.activejdbc.Base.withDb;

public class DB {
    static final String DB_URL = "jdbc:hsqldb:mem:cartcrafters";

    static JDBCDataSource dataSource;

    public static void init(ServletContextEvent event) throws SQLException, SqlToolError, IOException {
        // Prevent re-initialization
        if (dataSource != null) return;

        dataSource = new JDBCDataSource();
        dataSource.setUrl(DB_URL);

        // Load initial from file
        SqlFile sf = new SqlFile(new File(event.getServletContext().getRealPath("/resources/init.sql")));
        sf.setConnection(dataSource.getConnection());
        sf.execute();

        // Setup ActiveJDBC
        ConnectionJdbcConfig conf = new ConnectionJdbcConfig("org.hsqldb.jdbcDriver", DB_URL, null);
        conf.setEnvironment("development");
        org.javalite.activejdbc.connection_config.DBConfiguration.addConnectionConfig(conf);
    }

    public static void test() {
        int x = withDb(() -> {
            List<User> users = User.findAll();
            for (User user : users) System.out.println(user.get("userId") + ": " + user.get("firstName"));
            return users.size();
        });
        System.out.println("Entries: " + x);
    }
}
