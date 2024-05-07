package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationGroupeService;

import java.io.IOException;

public class ReclamationGroupeFormAdd {

    @FXML
    private TextField nameField;
    @FXML
    private Label validationLabel;

    private reclamation_groupe reclamationGroupe;

    public void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void save() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            validationLabel.setText("Name cannot be empty");
            return; // Don't proceed with saving
        }

        // If name is not empty, proceed with saving
        System.out.println("Save button clicked");
        reclamation_groupe r = new reclamation_groupe();
        r.user_id_id = 1;
        r.reclamation_agent_id_id = 2;
        r.setName(name);
        r.setDay_time(java.time.LocalDateTime.now().toString());
        r.setStatus("pending");

        ReclamationGroupeService reclamationGroupeService = new ReclamationGroupeService();
        reclamationGroupeService.AddReclamationGroupe(r);
        showAlert("Adding " + r.getName(), Alert.AlertType.INFORMATION);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-groupe.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nameField.getScene().setRoot(root);
    }

    @FXML
    public void cancel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-groupe.fxml"));
        Parent root = fxmlLoader.load();
        nameField.getScene().setRoot(root);
    }
}
