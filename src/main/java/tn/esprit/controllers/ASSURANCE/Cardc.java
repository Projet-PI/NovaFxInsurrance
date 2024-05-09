package tn.esprit.controllers.ASSURANCE;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Contrat;

import java.io.IOException;
import java.sql.SQLException;

public class Cardc {
    @FXML
    private Label contratType;

    @FXML
    private Label contratDuration;

    @FXML
    private Label contratId;
    private Contrat contratData;
    private Contrat_s contratService;

    public Cardc() {
        contratService = new Contrat_s();
    }
    public void refreshContractData(ObservableList<Contrat> contratsList) {
        if (contratData != null && contratsList != null) {
            // Remove the deleted contract from the displayed data
            contratsList.remove(contratData);

            // Clear the displayed contract data
            contratData = null;
            refreshContractData();
        }
    }
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

                // After editing, refresh the displayed contract data
                refreshContractData();
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
    public void refreshContractData() {
        // Check if contratData is not null
        if (contratData != null) {
            // Refresh contrat data to corresponding UI elements
            contratId.setText(String.valueOf(contratData.getId()));
            contratType.setText(contratData.getType_couverture());
            contratDuration.setText(String.valueOf(contratData.getDuree()));
        } else {
            // Clear UI elements if contratData is null
            contratId.setText("");
            contratType.setText("");
            contratDuration.setText("");
        }
    }

    public void deleteContractButtonClicked(ActionEvent actionEvent) {
        if (contratData != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Contract");
            alert.setContentText("Are you sure you want to delete this contract?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Call the delete method on the service instance to delete the contract
                        contratService.delete(contratData.getId());

                        // Clear the displayed contract data
                        contratData = null;

                        // Refresh the UI by passing the ObservableList of contracts
                        refreshContractData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle the SQL exception as needed
                    }
                }
            });
        } else {
            System.out.println("No contract data available.");
        }
    }
    }




