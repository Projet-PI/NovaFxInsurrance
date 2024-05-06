package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUtilisateurs;
import tn.esprit.utils.DataBase;
import tn.esprit.utils.PasswordUtil;
import tn.esprit.utils.SessionManager;

import javax.mail.MessagingException;

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
        System.out.println("test1");
        String qry = "SELECT * FROM `user` WHERE `email`=? AND `password`=?";
        conx = DataBase.getInstance().getConx();
        try {
            PreparedStatement stm = conx.prepareStatement(qry);
            stm.setString(1, mailFieldLogin.getText());
            stm.setString(2, tempPasswordField.getText());
            System.out.println("password field" + tempPasswordField.getText());
            String HashedPassword = PasswordUtil.hashPassword(String.valueOf(tempPasswordField));

            System.out.println("hashed password" + HashedPassword);
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
                    System.out.println("test 2");
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

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Réinitialisation Mot de Passe");
        emailDialog.setHeaderText("Demande de réinitialisation");
        emailDialog.setContentText("Veuillez entrer votre Email:");

        Optional<String> emailResult = emailDialog.showAndWait();
        emailResult.ifPresent(email -> {
            try {
                ServiceUtilisateurs service = new ServiceUtilisateurs();
                service.requestPasswordReset(email);
                showTokenEntryDialog(email);
            } catch (SQLException | MessagingException ex) {
                showAlert("Error", "Failed to send reset token: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

    }



    private void showTokenEntryDialog(String email) {
        TextInputDialog tokenDialog = new TextInputDialog();
        tokenDialog.setTitle("Réinitialisation Mot de Passe");
        tokenDialog.setHeaderText("Veuillez entrer le Token:");
        tokenDialog.setContentText("Token:");

        Optional<String> tokenResult = tokenDialog.showAndWait();
        tokenResult.ifPresent(token -> verifyToken(email, token));
    }

    public void verifyToken(String email, String tokenInput) {
        if (conx == null) {
            conx = DataBase.getInstance().getConx();
            if (conx == null) {
                showAlert("Database Error", "Unable to establish a connection to the database.", Alert.AlertType.ERROR);
                return;
            }
        }

        String sql = "SELECT reset_token, reset_token_expiration FROM user WHERE email = ?";
        try (PreparedStatement stmt = conx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String token = rs.getString("reset_token");
                Timestamp expiration = rs.getTimestamp("reset_token_expiration");
                long currentTime = System.currentTimeMillis();
                long tokenExpiration = expiration != null ? expiration.getTime() : 0;
                if (token != null && token.equals(tokenInput) && currentTime < tokenExpiration) {
                    Platform.runLater(() -> showPasswordResetDialog(email));
                } else {
                    Platform.runLater(() -> showAlert("Reset Error", "The reset token is invalid or has expired.", Alert.AlertType.ERROR));
                }
            } else {
                Platform.runLater(() -> showAlert("Error", "Email not found.", Alert.AlertType.ERROR));
            }
        } catch (SQLException e) {
            Platform.runLater(() -> showAlert("Database Error", "Error checking token: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }


    private void showPasswordResetDialog(String email) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Réinitialisation Mot de Passe");
        dialog.setHeaderText("Entrer votre nouveau Mot de Passe" + email);
        dialog.setContentText("Nouveau Mot de Passe:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newPassword -> resetUserPassword(email, newPassword));
    }

    private void resetUserPassword(String email, String newPassword) {
        String sql = "UPDATE user SET password = ?, reset_token = NULL, reset_token_expiration = NULL WHERE email = ?";
        try (PreparedStatement stmt = conx.prepareStatement(sql)) {
            String HashedPassword = PasswordUtil.hashPassword(newPassword);

            stmt.setString(1, HashedPassword);
            stmt.setString(2, email);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                Platform.runLater(() -> showAlert("Success", "Password has been reset successfully.", Alert.AlertType.INFORMATION));
            } else {
                Platform.runLater(() -> showAlert("Error", "Failed to reset password.", Alert.AlertType.ERROR));
            }
        } catch (SQLException e) {
            Platform.runLater(() -> showAlert("Database Error", "Error updating password: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }


}



