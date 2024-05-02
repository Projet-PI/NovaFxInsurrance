package tn.PiFx.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import javafx.stage.Window;
import tn.PiFx.entities.User;
import tn.PiFx.services.ServiceUtilisateurs;
import tn.PiFx.utils.DataBase;
import tn.PiFx.utils.SessionManager;

public class LoginController implements Initializable {
    private Connection conx;

    @FXML
    private Button loginButton;

    @FXML
    private URL location;

    @FXML
    private TextField mailFieldLogin;

    @FXML
    private TextField tempPasswordField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    public void connecterUser(javafx.event.ActionEvent actionEvent) {
        String qry = "SELECT * FROM `user` WHERE `email`=? AND `password`=?";
        conx = DataBase.getInstance().getConx();
        try {
            PreparedStatement stm = conx.prepareStatement(qry);
            stm.setString(1, mailFieldLogin.getText());
            stm.setString(2, tempPasswordField.getText());
            ResultSet rs = stm.executeQuery();
            User CurUser;

            if (rs.next()) {
                CurUser = new User(rs.getInt("id"),
                        rs.getInt("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("num_tel"),
                        rs.getString("profession"),
                        rs.getString("role"));

                User.setCurrent_User(CurUser);
                SessionManager.getInstace(rs.getInt("id"),
                        rs.getInt("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("num_tel"),
                        rs.getString("profession"),
                        rs.getString("role"));
                String role = rs.getString("role");
                if (role.equals("[\"ROLE_ADMIN\"]")) {
                    try {
                        FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/loadingscene.fxml"));
                        Parent loadingRoot = loadingLoader.load();
                        Stage loadingStage = new Stage();
                        loadingStage.setScene(new Scene(loadingRoot));
                        loadingStage.setTitle("Loading...");
                        loadingStage.show();

                        Task<Parent> task = new Task<>() {
                            @Override
                            protected Parent call() throws Exception {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminUser.fxml"));
                                return loader.load();
                            }
                        };
                        task.setOnSucceeded(event -> {
                            loadingStage.close();
                            Parent root = task.getValue();
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            //scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                            stage.setScene(scene);
                            stage.setTitle("Nova - Dashboard");
                            stage.show();
                        });

                        new Thread(task).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (role.equals("[\"ROLE_USER\"]")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUser.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Nova Assurance");
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void ResetPassword(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Reset");
        dialog.setHeaderText("Enter your email to reset your password:");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            try {
                ServiceUtilisateurs userService = new ServiceUtilisateurs();
                userService.requestPasswordReset(email);
                showAlert("Reset Email Sent", "If the email you entered is registered, we've sent a reset link to it.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Failed to send reset email: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        });

    }
}



