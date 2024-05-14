package tn.esprit.controllers.ASSURANCE;

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
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Assurance;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.FacebookException;

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
    private Button shareFbBtn;

    @FXML
    private void viewContractButtonClicked(ActionEvent event) {
        try {
            //            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/AjouterContrat.fxml"));
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


    public void setData(Assurance prodData) throws SQLException {
        this.prodData = prodData;


        int contratId = prodData.getContrat_id();

        Contrat_s contratService = new Contrat_s();
        String typeCouverture = contratService.getTypeCovertureByContratId(contratId);


        nomass.setText(prodData.getNom());
        type.setText(String.valueOf(typeCouverture));
        dated.setText(prodData.getDate_debut());
        datef.setText(prodData.getDate_fin());


    }


    @FXML
    private void partage(ActionEvent event) {

        String appId = "300308449736891";
        String appSecret = "4cf17d1c8ce3d0b4e57840c5504e611f";
        String accessTokenString = "EAAERIQJ4OLsBOwfLThlki7imZA30vmRUrlQzW1KMOuFjYdrL3F5zN7ZAaPdpSVruLZArNoJPM4sZAnM3GHNFGqDLpZAdZAY8ZAKo9bT1pK1ZADPtIID2k0hr1vRspjVNmjhMGWzL9WrAwZAhiFe06NIf2NjZCn7zW5YtJiMZBv15VFeeQBkMTNeiZCpzXNjWkw583l7mWSzEPESXZBb9b3mY1OII0YykZD";
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(appId, appSecret);
        facebook.setOAuthAccessToken(new AccessToken(accessTokenString, null));

        // Construire le message à partager sur Facebook
        String msg = "Nouveelle assurance  disponible maintenant ";
//

        try {
            facebook.postStatusMessage(msg);
            System.out.println("Post shared successfully.");
        } catch (FacebookException e) {
            throw new RuntimeException(e);
        }

    }
}




