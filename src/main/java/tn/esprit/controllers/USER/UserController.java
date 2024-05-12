package tn.esprit.controllers.USER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        currentUser = SessionManager.getInstance().getCurrentUser();
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
    private void HandleModification(javafx.event.ActionEvent event) {
        if (!validateInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez vérifier les données saisies. Certains champs contiennent des erreurs.");
            alert.showAndWait();
            return;
        }

        try {
            currentUser.setNom(NomText.getText().trim());
            currentUser.setPrenom(PrénomText.getText().trim());
            currentUser.setEmail(EmailText.getText().trim());
            currentUser.setNum_tel(Integer.parseInt(NumtelText.getText().trim()));
            currentUser.setAdresse(AdresseText.getText().trim());
            currentUser.setProfession(ProfessionText.getText().trim());
            currentUser.setCin(Integer.parseInt(CinText.getText().trim()));

            boolean updated = userService.updateWithoutPassword(currentUser);
            if (updated == true) {
                System.out.println("succes");
            } else {
                System.out.println("fail");
            }
            SessionManager.getInstance().setCurrentUser(currentUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Votre Profile à été modifié avec succès.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Les numéros de téléphone et CIN doivent contenir uniquement des chiffres.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Echec de modification de profile: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private boolean validateInput() {
        if (NomText.getText().isEmpty() || PrénomText.getText().isEmpty() || AdresseText.getText().isEmpty() ||
                EmailText.getText().isEmpty() || NumtelText.getText().isEmpty() || CinText.getText().isEmpty() ||
                ProfessionText.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    @FXML
    public void MainMenu(ActionEvent actionEvent) {
        try {
            System.out.println("test3");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUser.fxml"));
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

