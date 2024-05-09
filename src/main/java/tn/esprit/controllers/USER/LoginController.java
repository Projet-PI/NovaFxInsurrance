package tn.esprit.controllers.USER;

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
    public void connecterUser(ActionEvent actionEvent) {
        System.out.println("Attempting to connect user...");
        String email = mailFieldLogin.getText();
        String password = tempPasswordField.getText();
        String qry = "SELECT * FROM `user` WHERE `email`=?";

        Task<User> userLoginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                try (Connection conx = DataBase.getInstance().getConx();
                     PreparedStatement stm = conx.prepareStatement(qry)) {
                    stm.setString(1, email);
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        String storedHash = rs.getString("password");
                        if (PasswordUtil.checkPassword(password, storedHash)) {
                            return new User(rs.getInt("id"),
                                    rs.getInt("cin"),
                                    rs.getString("nom"),
                                    rs.getString("prenom"),
                                    storedHash,
                                    rs.getString("email"),
                                    rs.getInt("num_tel"),

                                    rs.getString("role"),
                                    rs.getString("profession"));// Ensure role is trimmed if necessary
                        }
                    }
                    return null;
                }
            }
        };

        userLoginTask.setOnSucceeded(e -> {
            User curUser = userLoginTask.getValue();
            if (curUser != null) {
                SessionManager.getInstance().setCurrentUser(curUser); // Set user in session
                System.out.println("User logged in: " + curUser.getEmail());
                System.out.println("Role: " + curUser.getRoles()); // Debug: Print the role
                try {
                    navigateBasedOnRole(curUser.getRoles(), actionEvent);
                } catch (IOException ioException) {
                    System.err.println("Navigation failed: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            } else {
                System.out.println("Login failed: No valid user found or password does not match.");
            }
        });

        userLoginTask.setOnFailed(e -> {
            Throwable exc = userLoginTask.getException();
            System.err.println("Login failed: " + exc.getMessage());
            exc.printStackTrace();
        });

        new Thread(userLoginTask).start();
    }


    private void navigateBasedOnRole(String role, ActionEvent actionEvent) throws IOException {
        String fxmlFile = "";
        if ("[\"ROLE_ADMIN\"]".equals(role)) {
            fxmlFile = "/AdminUser.fxml";
        } else if ("[\"ROLE_USER\"]".equals(role)) {
            fxmlFile = "/InterfaceUser.fxml";
        }

        if (!fxmlFile.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("Role not recognized or fxml file not specified.");
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



