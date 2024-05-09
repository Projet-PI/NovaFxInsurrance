package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUtilisateurs;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;

public class UserController implements Initializable {

    @FXML
    private TextField AdresseText;

    @FXML
    private TextField CinText;

    @FXML
    private TextField EmailText;

    @FXML
    private Button ModifierBack;

    @FXML
    private TextField NomText;

    @FXML
    private TextField NumtelText;

    @FXML
    private TextField ProfessionText;

    @FXML
    private TextField PrénomText;

    @FXML
    private Button RetourButton;

    private User currentUser;
    private ServiceUtilisateurs userService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new ServiceUtilisateurs();
        currentUser = SessionManager.getInstance().getUser();  // Assuming SessionManager holds the logged-in user
        loadUserProfile();
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            NomText.setText(currentUser.getNom());
            PrénomText.setText(currentUser.getPrenom());
            AdresseText.setText(currentUser.getAdresse());
            EmailText.setText((currentUser.getEmail()));
            NumtelText.setText(String.valueOf((currentUser.getNum_tel())));
            CinText.setText(String.valueOf((currentUser.getCin())));
            ProfessionText.setText((currentUser.getProfession()));
        }
    }

    @FXML
    private void HandleModification(javafx.event.ActionEvent actionEvent) {
        try {
            currentUser.setNom(NomText.getText());
            currentUser.setPrenom(PrénomText.getText());
            currentUser.setAdresse(AdresseText.getText());
            currentUser.setEmail((EmailText.getText()));
            currentUser.setNum_tel(Integer.parseInt((NumtelText.getText())));
            currentUser.setCin(Integer.parseInt((CinText.getText())));
            currentUser.setProfession((ProfessionText.getText()));
            userService.updateUser(currentUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Votre Profile à été modifié avec succès.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Echec de modification de profile: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void BackToMenuButton(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfacUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }





