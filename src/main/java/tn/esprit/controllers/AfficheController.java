package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficheController {



    @FXML
    public void initialize() {
    }

    @FXML
    private void sinistre(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AffichageSinistre.fxml"));
            Parent sinistreParent = fxmlLoader.load();
            // Assuming AffichageSinistreController is the correct controller for AffichageSinistre.fxml
            AffichageSinistreController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene sinistreScene = new Scene(sinistreParent, 800, 400);
            currentStage.setScene(sinistreScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void rapport(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AffichageRapport.fxml"));
            Parent rapportParent = fxmlLoader.load();
            // Assuming AffichageRapportController is the correct controller for AffichageRapport.fxml
            AffichageRapportController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene rapportScene = new Scene(rapportParent, 800, 400);
            currentStage.setScene(rapportScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
