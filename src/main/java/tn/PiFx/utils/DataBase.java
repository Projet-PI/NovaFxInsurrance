package tn.PiFx.utils;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase instance ;
    private final String URL ="jdbc:mysql://localhost:3306/basenova_3" ;
    private final String USERNAME ="root";
    private final String PWD ="";
    Connection conx;

    private DataBase(){
        try {
            conx = DriverManager.getConnection(URL, USERNAME, PWD);
            System.out.println("Connextion établie!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConx() {
        return conx;
    }
}
