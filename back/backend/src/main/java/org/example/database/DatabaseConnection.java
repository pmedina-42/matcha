package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.example.constants.Constants.*;

public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
            return connection;
    }

    public static void initializeDatabaseVariables(Properties properties) {
        url = properties.getProperty(DB_URL);
        user = properties.getProperty(DB_USER);
        password = properties.getProperty(DB_PASS);
    }

}
