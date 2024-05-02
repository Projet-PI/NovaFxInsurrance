package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static volatile DataBase instance;
    private Connection conn;

    private final String url = "jdbc:mysql://localhost:3306/baseass";
    private final String user = "root";
    private final String pwd = "";

    private DataBase() {
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected");
        } catch (SQLException sqlException) {
            // Log or throw an exception instead of just printing
            System.err.println("Error connecting to the database: " + sqlException.getMessage());
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase();
                }
            }
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
