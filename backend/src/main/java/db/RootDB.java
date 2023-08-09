package db;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.jdbc.JDBCDataSource;
import org.javalite.activejdbc.connection_config.ConnectionJdbcConfig;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class RootDB {
    static final String DB_URL = "jdbc:hsqldb:mem:cartcrafters";

    static JDBCDataSource dataSource;

    public static void init(ServletContext ctx) throws SQLException, SqlToolError, IOException {
        // Prevent re-initialization
        if (dataSource != null) return;

        dataSource = new JDBCDataSource();
        dataSource.setUrl(DB_URL);

        // Load initial from file
        SqlFile sf = new SqlFile(new File(ctx.getRealPath("/resources/init.sql")));
        sf.setConnection(dataSource.getConnection());
        sf.execute();

        // Setup ActiveJDBC
        connect();
    }

    public static void connect() {
        ConnectionJdbcConfig conf = new ConnectionJdbcConfig("org.hsqldb.jdbcDriver", DB_URL, null);
        conf.setEnvironment("development");
        org.javalite.activejdbc.connection_config.DBConfiguration.addConnectionConfig(conf);
    }
}
