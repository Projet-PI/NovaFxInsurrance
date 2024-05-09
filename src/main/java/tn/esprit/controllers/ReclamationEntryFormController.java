package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.reclamation_entry;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationEntryService;

public class ReclamationEntryFormController {
    @FXML
    private ComboBox modeComboBox;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @FXML
    private void sendPrompt() {
        String prompt = inputField.getText();
        if (prompt != null && !prompt.isEmpty()) {
            // Implement this method to send the prompt
            // For example, you might create a new reclamation_entry and add it to the ListView
            reclamation_entry entry = new reclamation_entry();
            entry.setPrompt(prompt);

            // Clear the input field
            inputField.clear();
        }
    }

    public void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public reclamation_entry entryReclamationEntry;
    public reclamation_groupe reclamationGroupe;

    private ReclamationEntryService service;

    public ReclamationEntryFormController() {
        this.service = new ReclamationEntryService();
    }

    public void setReclamationGroupe(reclamation_entry item2, reclamation_groupe item1) {
        // Get all reclamation entries for the given reclamation group

        // set the reclamation group
        entryReclamationEntry = item2;
        reclamationGroupe = item1;

        // put the values in the form
        inputField.setText(entryReclamationEntry.getPrompt());


    }

    public void cancelAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-entry.fxml"));
            Parent reclamationEntryParent = fxmlLoader.load();

            ReclamationEntryController controller = fxmlLoader.getController();
            controller.setReclamationGroupe(reclamationGroupe);

            Stage currentStage = (Stage) inputField.getScene().getWindow();

            Scene reclamationEntryScene = new Scene(reclamationEntryParent, 870, 600);
            currentStage.setScene(reclamationEntryScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add action for send button
    public void sendPrompt(ActionEvent actionEvent) {
        String prompt = inputField.getText();

        if (prompt == null || prompt.isEmpty()) {
            System.out.println("Prompt is empty");
            showAlert("Prompt cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        if (prompt.contains("badword")) {
            System.out.println("Bad word detected");
            showAlert("Bad word detected", Alert.AlertType.ERROR);
            return;
        }

        String mode = (String) modeComboBox.getValue(); // Corrected line
        System.out.println("Mode: " + mode);
        entryReclamationEntry.setPrompt(prompt);
        entryReclamationEntry.setResponseType(mode);
        entryReclamationEntry.setDay_time(java.time.LocalDateTime.now().toString());
        System.out.println(entryReclamationEntry);

        ReclamationEntryService service = new ReclamationEntryService();
        service.updateReclamationEntry(entryReclamationEntry);

        // redirect to the reclamation entry page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-entry.fxml"));
            Parent reclamationEntryParent = fxmlLoader.load();

            ReclamationEntryController controller = fxmlLoader.getController();
            controller.setReclamationGroupe(reclamationGroupe);

            Stage currentStage = (Stage) inputField.getScene().getWindow();

            Scene reclamationEntryScene = new Scene(reclamationEntryParent, 870, 600);
            currentStage.setScene(reclamationEntryScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}