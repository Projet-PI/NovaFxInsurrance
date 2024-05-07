package tn.esprit.controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.entity.Assurance;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.ImageView; // Importez la classe ImageView correcte depuis JavaFX
import javafx.scene.control.Label; // Importez la classe Label correcte depuis JavaFX
public class Card {
    private Assurance prodData;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label dated;

    @FXML
    private Label datef;

    @FXML
    private Label type;

    @FXML
    private Label nomass;
    @FXML
    private Button back;


    @FXML
    private void viewContractButtonClicked(ActionEvent event) {
         try {
        //            // Charger le fichier FXML
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
                   Parent root = loader.load();
        //
        //            // Obtenir le stage actuel
                   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //
        //            // Créer une nouvelle scène avec le contenu chargé depuis le fichier FXML
                    Scene scene = new Scene(root);
        //
        //            // Définir la nouvelle scène sur le stage
                  stage.setScene(scene);
                 stage.show();
              } catch (
    IOException e) {
                   e.printStackTrace();
               }
        //    }
    }








   public void setData(Assurance prodData) {
    this.prodData = prodData;



    nomass.setText(prodData.getNom());
    type.setText(prodData.getType());
    dated.setText(prodData.getDate_debut());
    datef.setText(prodData.getDate_fin());






}
}





