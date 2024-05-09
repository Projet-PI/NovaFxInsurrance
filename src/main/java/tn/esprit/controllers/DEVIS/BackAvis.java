package tn.esprit.controllers.DEVIS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.AvisRestau;
import tn.esprit.services.ServiceDevis;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BackAvis implements Initializable {

    @FXML
    private TableColumn<AvisRestau,String> commentR;

    @FXML
    private TableColumn<AvisRestau, String> nomR;
   @FXML
   private TableColumn<AvisRestau, String> responseR;

    @FXML
    private Button supprimer;


    @FXML
    private TableView<AvisRestau> tabAvis;
    @FXML
    private TextField TFadresse;

    @FXML
    private TextField TFadresse1;
    @FXML
    Button  supprimer1, pdf;
    @FXML
    private Button BackAvis;
    @FXML
    private Button BackAvis1;
    @FXML
    private Button Ajout;

    int selectedId;
    @FXML
    TextField TFcommentR;


    ServiceDevis sa=new ServiceDevis();








    private void loadTableData() {
        ObservableList<AvisRestau> data = FXCollections.observableArrayList(sa.getAll());

        tabAvis.setItems(data);
    }

    public void updateTable_r() {
        List<AvisRestau> response = sa.getAll();
        tabAvis.getItems().setAll(response);
    }




    // Assurez-vous que cette ligne est bien présente dans votre classe

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomR.setCellValueFactory(new PropertyValueFactory<>("id"));
        commentR.setCellValueFactory(new PropertyValueFactory<AvisRestau, String>("response"));
        responseR.setCellValueFactory(new PropertyValueFactory<AvisRestau, String>("status"));


        loadTableData();

        tabAvis.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                AvisRestau selectedAvis = tabAvis.getSelectionModel().getSelectedItem();
                selectedId = selectedAvis.getId();
                String selectedStatus = selectedAvis.getStatus();
                TFadresse.setText(selectedAvis.getResponse());
                TFadresse1.setText(selectedAvis.getStatus());



            }
        });

        supprimer1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AvisRestau devis = new AvisRestau();
                devis.setId(selectedId);
                devis.setResponse(TFadresse1.getText());
                devis.setStatus((TFadresse.getText()));
                devis.setDevis_id_id(2);
                sa.modifier(devis);
                updateTable_r();
            //    clearFields();
            }
        });

        supprimer.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                sa.supprimer(selectedId);
                updateTable_r();
            }


        });


    }
    // Methode de Navigation


    public void goback() throws IOException {
        Stage stage = (Stage) BackAvis.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/back.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log IN");
        stage.show();
    }
    //Methode OPENAI Not Used // Probléme Solde
 /*   public static String sendMessage() throws IOException {
        JsonObject requestBody = new JsonObject();
    requestBody.addProperty("model", "gpt-3.5-turbo-instruct");
    requestBody.addProperty("prompt", "who are you ?");
    requestBody.addProperty("max_tokens", 300);

    Request request = new Request.Builder()
            .url(API_URL)
            .header("Authorization", "Bearer " + API_KEY)
            .post(RequestBody.create(JSON, gson.toJson(requestBody)))
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        JsonObject responseBody = gson.fromJson(response.body().string(), JsonObject.class);
        return responseBody.getAsJsonArray("choices").get(0).getAsJsonObject().get("text").getAsString();
    }
}*/
    // Methode de Navigation

    public void BackAvis(ActionEvent event)
    {
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
    // Methode de Navigation

    public void Dashboard(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) BackAvis1.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // Methode de Navigation
    public void Dashboard_Back_Avis(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Ajout.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}