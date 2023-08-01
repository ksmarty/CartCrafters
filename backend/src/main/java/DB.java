import model.User;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.jdbc.JDBCDataSource;
import org.javalite.activejdbc.connection_config.ConnectionJdbcConfig;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.javalite.activejdbc.Base.withDb;

public class DB {
    static final String DB_URL = "jdbc:hsqldb:mem:cartcrafters";

    static JDBCDataSource dataSource;

    public static void init() throws SQLException, SqlToolError, IOException {
        // Prevent re-initialization
        if (dataSource != null) return;

        dataSource = new JDBCDataSource();
        dataSource.setUrl(DB_URL);

        Statement stmt = dataSource.getConnection().createStatement();

        // Load initial from file
        SqlFile sf = new SqlFile(new File("resources/init.sql"));
        sf.setConnection(dataSource.getConnection());
        sf.execute();

        // Setup ActiveJDBC
        ConnectionJdbcConfig conf = new ConnectionJdbcConfig("org.hsqldb.jdbcDriver", DB_URL, null);
        conf.setEnvironment("development");
        org.javalite.activejdbc.connection_config.DBConfiguration.addConnectionConfig(conf);


        withDb(() -> {
            new User().set("name", "Steve Johnson").saveIt();
            return null;
        });
    }

    public static void test() {
        int x = withDb(() -> {
            List<User> users = User.findAll();
            for (User user : users) System.out.println(user.get("id") + ": " + user.get("name"));
            return users.size();
        });
        System.out.println("Entries: " + x);
    }
}
