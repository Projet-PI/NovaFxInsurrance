package tn.esprit.controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entity.Contrat;

import java.io.IOException;

public class Cardc {
    @FXML
    private Label contratType;

    @FXML
    private Label contratDuration;

    @FXML
    private Label contratId;
    private Contrat contratData;

    public void editContractButtonClicked(ActionEvent actionEvent) {
        if (contratData != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierContrat.fxml"));
                Parent root = loader.load();

                ModifierContrat modifierContratController = loader.getController();
                modifierContratController.initData(contratData); // Pass the current contract data

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        } else {
            System.out.println("No contract data available.");
        }
    }

    public void setData(Contrat contrat) {
        this.contratData = contrat; // Corrected variable assignment
        System.out.println("Setting data for contrat: " + contrat.getId()); // Debug output

        // Set contrat data to corresponding UI elements
        contratId.setText(String.valueOf(contrat.getId()));
        contratType.setText(contrat.getType_couverture());
        contratDuration.setText(String.valueOf(contrat.getDuree()));
    }
}
