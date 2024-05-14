package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase instance;
    private final String URL = "jdbc:mysql://localhost:3306/basenova_3";
    private final String USERNAME = "root";
    private final String PWD = "";
    private Connection conx;

    private DataBase() {
        connect();
    }

    private void connect() {
        try {
            if (conx == null || conx.isClosed()) {
                conx = DriverManager.getConnection(URL, USERNAME, PWD);
                System.out.println("Connection established!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConx() {
        try {
            if (conx == null || conx.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Failed to check connection status: " + e.getMessage());
        }
        return conx;
    }
}
