package tn.esprit.controllers.RECLAMATIONADMIN;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationGroupeService;

public class ReclamationGroupeFormController {

    @FXML
    private TextField idField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField agentIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField dayTimeField;

    @FXML
    private TextField statusField;

    private reclamation_groupe reclamationGroupe;

    public void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setReclamationGroupe(reclamation_groupe reclamationGroupe) {
        this.reclamationGroupe = reclamationGroupe;

        // Update the text fields with the properties of the reclamation_groupe object
        idField.setText(String.valueOf(reclamationGroupe.getId()));
        userIdField.setText(String.valueOf(reclamationGroupe.getUser_id_id()));
        agentIdField.setText(String.valueOf(reclamationGroupe.getReclamation_agent_id_id()));
        nameField.setText(reclamationGroupe.getName());
        dayTimeField.setText(reclamationGroupe.getDay_time());
        statusField.setText(reclamationGroupe.getStatus());
    }

    @FXML
    public void save() {

        // Update the reclamation_groupe object with the data from the text fields
        reclamationGroupe.setId(Integer.parseInt(idField.getText()));
        reclamationGroupe.setUser_id_id(Integer.parseInt(userIdField.getText()));
        reclamationGroupe.setReclamation_agent_id_id(Integer.parseInt(agentIdField.getText()));
        reclamationGroupe.setName(nameField.getText());
        reclamationGroupe.setDay_time(dayTimeField.getText());
        reclamationGroupe.setStatus(statusField.getText());

        showAlert("Editing " + reclamationGroupe.getName(), Alert.AlertType.INFORMATION);
        // Save the updated reclamation_groupe object
        ReclamationGroupeService reclamationGroupeService = new ReclamationGroupeService();
        reclamationGroupeService.UpdateReclamationGroupe(reclamationGroupe);

        // Close the form
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-groupe.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        idField.getScene().setRoot(root);
    }

    @FXML
    public void cancel() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-groupe.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        idField.getScene().setRoot(root);
    }
}