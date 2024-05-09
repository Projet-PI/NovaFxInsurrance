package tn.esprit.controllers.USER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.SessionManager;
import tn.esprit.entities.User;

import java.io.IOException;

public class LandingPage {
    @FXML
    public void MyAccount(ActionEvent actionEvent) {
    }

    @FXML
    public void MyReclamation(ActionEvent actionEvent) {
    }

    @FXML
    public void MyDevis(ActionEvent actionEvent) {
    }

    @FXML
    public void MySinistre(ActionEvent actionEvent) {
    }
    @FXML
    public void MyAssurance(ActionEvent actionEvent) {
    }

    @FXML
    public void Logout(ActionEvent actionEvent) {
        User currentUser = SessionManager.getCurrentUser(); // Assuming SessionManager has this method

        if (currentUser != null) {
            System.out.println("User Email: " + currentUser.getEmail());
            System.out.println("User Name: " + currentUser.getNom() + " " + currentUser.getPrenom());
        }else {
            System.out.println("no user was logged in ");
        }

        System.out.println("Logging out...");
        SessionManager.logoutCurrentUser();

        try {
            System.out.println("test3");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
