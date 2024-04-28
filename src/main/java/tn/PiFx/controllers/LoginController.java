package tn.PiFx.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import javafx.stage.Window;
import tn.PiFx.entities.User;
import tn.PiFx.utils.DataBase;
import tn.PiFx.utils.SessionManager;

public class LoginController implements Initializable{
    private Connection conx;

    @FXML
    private Button loginButton;

    @FXML
    private TextField mailFieldLogin;

    @FXML
    private TextField tempPasswordField;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }






    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void connecter(javafx.event.ActionEvent event) throws SQLException {
        String query = "SELECT * FROM `user` WHERE `email`=? AND `password`=?";
        try (Connection conx = DataBase.getInstance().getConx();
             PreparedStatement stm = conx.prepareStatement(query)) {

            stm.setString(1, mailFieldLogin.getText()); // Assuming mailFieldLogin is your TextField for email
            stm.setString(2, tempPasswordField.getText()); // Assuming tempPasswordField is your TextField for password

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User curUser = new User(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("adresse"),
                            rs.getInt("num_tel"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("profession")
                            // Assuming you have these fields in your User constructor
                    );
                    // Process login success, update session state, navigate to the appropriate view, etc.
                } else {
                    // Process login failure, show alert, etc.
                    showAlert("Login Failed", "Incorrect email or password.", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "An error occurred while attempting to log in: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    @FXML
    public void creerCompteLog(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Waves - Inscription");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



