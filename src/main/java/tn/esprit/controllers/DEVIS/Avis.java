package tn.esprit.controllers.DEVIS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.entities.AvisRestau;
import tn.esprit.entities.Devis;
import tn.esprit.services.ServiceDevis;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Avis implements Initializable {
    @FXML
    private Button Gestiondevis;

    @FXML
    private Button ajouter;
    @FXML
    private Button BackAvis;

    @FXML
    private Button Gotodash;
    @FXML
    private TextField sasie;

    @FXML
    private  TextField sasie1;
    @FXML
    private TextField id;

    @FXML
    private VBox comentbox;

    @FXML
    private Label nbdislike;

    @FXML
    private Label nblike;
    @FXML
    private Text errorNom;

    @FXML
    private Text errorAdresse;

    @FXML
    private Text errorEmail;

    @FXML
    private Text errorPhone;


    private ServiceDevis sa = new ServiceDevis();
    private List<AvisRestau> la;
    private Devis r =new Devis() ;

    private AvisRestau avis=new  AvisRestau() ;




    public String bad_words(String badWord) {

        List<String> badListW = Arrays.asList("fuck", "bitch","motherfucker","zebi","zabour","9ahba","khahba");
        String badNew = "";
        List<String> newList = new ArrayList<>();
        for (String str : badListW) {
            if (badWord.contains(str)) {
                badNew += "" + str;
                if (str.length() >= 1) {
                    StringBuilder result = new StringBuilder();
                    result.append(str.charAt(0));
                    for (int i = 0; i < str.length() - 2; ++i) {
                        result.append("*");
                    }
                    result.append(str.charAt(str.length() - 1));
                    str = result.toString();
                    if (!str.isEmpty()) {
                        System.out.println("ATTENTION !! Vous avez écrit un gros mot  : " + result + " .C'est un avertissement ! Priére d'avoir un peu de respect ! Votre description sera envoyée comme suit :");
                        System.out.println(badWord.replace(badNew, "") + " ");
                    }
                }
                Notifications.create()
                        .darkStyle()
                        .title("ATTENTION !! Vous avez écrit un gros mot ")
                        .hideAfter(Duration.seconds(10))
                        .show();
            }
        }
        return (badWord.replace(badNew, "") + " ");
    }

    private boolean isInputValid() {
        boolean isValid = true;

        // Validate and display error messages
        //if (sasie.getText().isEmpty() || !sasie.getText().matches("^[a-zA-Z]+$")) {
        //CND d'acceptation toute type de caractere

        if (sasie.getText().isEmpty() || !sasie.getText().matches(".*")) {
            errorNom.setText("Response is required and should not contain numbers");
            isValid = false;
        } else {
            errorNom.setText("");
        }

       // if (sasie1.getText().isEmpty() || !sasie1.getText().matches("^[a-zA-Z]+$")) {
        //CND d'acceptation toute type de caractere
        if (sasie.getText().isEmpty() || !sasie.getText().matches(".*")) {

            errorAdresse.setText("Adresse is required and should not contain numbers ");
            isValid = false;
        } else {
            errorAdresse.setText("");
        }



        return isValid;
    }
// ajout de reponse sans envoie de SMS (Crud d'ajout )


  //  @FXML
/*
    void ajouterAvisAction(ActionEvent event) {
        // Retrieve values from text fields
        //if (isInputValid()) {
            String status = sasie.getText();
            String response = sasie1.getText();
            int devis_id_id= Integer.parseInt(id.getText());
            status = bad_words(status);

            // Set the ID of the devis
            int devisId = r.getId(); // Assuming r is the Devis object and getId() returns the ID of the devis

            // Create a new AvisRestau object
            AvisRestau avis = new AvisRestau();
            avis.setStatus(status);
            avis.setResponse(response);
            avis.setDevis_id_id(devis_id_id); // Set the ID of the devis

            // Call the ajouter method of ServiceDevis to add the avis to the database
            ServiceDevis service = new ServiceDevis();
            service.ajouter(avis);
       //ss }

        // Clear fields
        show();
        sasie.clear();
        sasie1.clear();
    }
*/

    // ajout de reponse Avec  envoie de SMS using Twilio (Crud d'ajout )


    @FXML
    void ajouterAvisAction(ActionEvent event) {
        // Récupérer les valeurs des champs de texte
        String status = sasie.getText();
        String response = sasie1.getText();
        int devis_id_id = Integer.parseInt(id.getText());
        status = bad_words(status);

        // Créer un nouvel objet AvisRestau
        AvisRestau avis = new AvisRestau();
        avis.setStatus(status);
        avis.setResponse(response);
        avis.setDevis_id_id(devis_id_id); // Définir l'ID du devis

        // Appeler la méthode pour ajouter l'avis à la base de données
        ServiceDevis service = new ServiceDevis();
        service.ajouter(avis);

        // Si le statut est "traite", envoyer un SMS
        if ("101".equals(status)) {
            // Construire le message à envoyer par SMS
            String message = "Merci d'avoir passe un devis Chez Nova Insurrance  : " + response;

            // Appeler la méthode pour envoyer le SMS
            Example example = new Example();
            example.send_sms(message);
        }

        // Effacer les champs de texte
        show();
        sasie.clear();
        sasie1.clear();
    }

    // No used Methode

 /*   @FXML
    private void clickLike(MouseEvent mouseEvent) {

        // Create a Restaurant object
        AvisRestau avis = new AvisRestau();

        //avis.setIdR(r.getIdR());
        avis.setIdR(34);
        avis.setId(30);
        avis.setStatus("a7sen devis");

        // Call the ajouter method of ServiceRestau to add the restaurant to the database
        ServiceDevis service = new ServiceDevis();
        service.modifier(avis);


        // Clear fields and reset selected image
        show();

    }*/
















    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        show();
    }


    public void show(){
       comentbox.getChildren().clear();
        la = sa.getAll();


        for (int i = 0; i < la.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/unAvis.fxml"));

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                UnAvis item = fxmlLoader.getController();
                item.setData(la.get(i));


                comentbox.getChildren().add(anchorPane);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void BackAvis(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) BackAvis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Gotodash(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Gotodash.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Gestiondevis(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Gestiondevis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


