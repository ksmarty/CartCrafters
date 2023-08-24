package db;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.javalite.activejdbc.connection_config.ConnectionJdbcConfig;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RootDB {
    public static String DB_URL;
    static String DB_PATH;

    static private JDBCDataSource dataSource;

    public static void init(ServletContext ctx) throws SQLException, SqlToolError, IOException, ServerAcl.AclFormatException {
        // Prevent re-initialization
        if (dataSource != null) return;

        // DB_URL = "jdbc:hsqldb:mem:cartcrafters";
        DB_PATH = ctx.getRealPath("/storage/db/cartcrafters");
        DB_URL = "jdbc:hsqldb:file:" + DB_PATH;

        runHSQLDB();

        dataSource = new JDBCDataSource();
        dataSource.setUrl(DB_URL);

        // Load initial from file
        ResultSet rs = dataSource.getConnection()
                .createStatement()
                .executeQuery("SELECT COUNT(*) as table_count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'");
        if (rs.next() && rs.getInt("table_count") == 0) {
            SqlFile sf = new SqlFile(new File(ctx.getRealPath("/resources/init.sql")));
            sf.setConnection(dataSource.getConnection());
            sf.execute();
        }

        // Setup ActiveJDBC
        connect();
    }

    public static void connect() {
        ConnectionJdbcConfig conf = new ConnectionJdbcConfig("org.hsqldb.jdbcDriver", DB_URL, null);
        conf.setEnvironment("development");
        org.javalite.activejdbc.connection_config.DBConfiguration.addConnectionConfig(conf);
    }

    private static void runHSQLDB() throws ServerAcl.AclFormatException, IOException {
        HsqlProperties properties = new HsqlProperties();
        properties.setProperty("server.database.0", DB_PATH);
        properties.setProperty("server.dbname.0", "cartcrafters");
        Server server = new Server();
        server.setProperties(properties);
        server.start();
    }
}
