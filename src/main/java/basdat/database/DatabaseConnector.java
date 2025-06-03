package basdat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=University;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "1234";
    private static Connection connection = null;

    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                System.err.println("Connection failed!");
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }
    
    public static Connection getConnection() throws SQLException {
        return connect();
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}