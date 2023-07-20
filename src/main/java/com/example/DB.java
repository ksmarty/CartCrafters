package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.jdbc.JDBCDataSource;

public class DB {
    static JDBCDataSource dataSource;

    public static void init() throws SQLException {
        if (dataSource != null)
            return;

        dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:testdb");

        Statement stmt = dataSource.getConnection().createStatement();

        // Tables
        stmt.executeUpdate("CREATE TABLE users (id INT, name VARCHAR(255))");

        // Dummy Values
        stmt.executeUpdate("INSERT INTO users VALUES (1, 'John Doe')");
    }

    public static void test() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                System.out.println(rs.getInt(1) + ": " + rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
