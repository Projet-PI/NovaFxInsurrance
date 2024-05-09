package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tn.esprit.services.MyListener;
import tn.esprit.services.ServiceR;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Afficher implements Initializable {

    @FXML
    private Button Stat;
    @FXML
    private TextField search;

    // Existing Java declarations...

    @FXML
    private ComboBox<String> roleFilter;
    @FXML
    private GridPane grid;

    @FXML
    private ImageView imageRestau;

    @FXML
    private Label nomRestau;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label adresse;

    @FXML
    private Label email;

    @FXML
    private Label phone;

    @FXML
    private Button butonAvis;

 

    private List<tn.esprit.entities.Devis> restaurants;
    private MyListener myListener;

    private ServiceR serviceR = new ServiceR();

    @FXML
    private Image image;

    public void Voir_stat(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chart.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Stat.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void setChosenRestau(tn.esprit.entities.Devis r) {
        nomRestau.setText(r.getName());

        if (r.getImageR() != null && !r.getImageR().isEmpty()) {
            Image image = new Image("file:" + r.getImageR()); // Use 'file:' prefix for local file paths
            imageRestau.setImage(image);
        } else {
            // You can set a default image here if you wish
            // imageRestau.setImage(defaultImage);
        }

        adresse.setText(r.getPrenom());
        email.setText(r.getEmail());


    }

   @FXML
    private void handleAvisButtonClick() {
        try {
            // Charger le fichier FXML de la nouvelle page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) butonAvis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions en conséquence
        }
    }


    @FXML
    private void handleSearch() {
        filterDisplayedRestaurants();
    }


    @FXML
    private void filterDisplayedRestaurants() {
        String searchText = search.getText().toLowerCase();
        List<tn.esprit.entities.Devis> filteredRestaurants = restaurants.stream()
                .filter(devis -> devis.getName().toLowerCase().contains(searchText)
                        || devis.getEmail().toLowerCase().contains(searchText)
                        || devis.getPrenom().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        loadGridData(filteredRestaurants);
    }


    private void loadGridData(List<tn.esprit.entities.Devis> filteredRestaurants) {
        grid.getChildren().clear(); // Clear existing content from the grid

        int column = 0;
        int row = 1;

        try {
            for (int i = 0; i < filteredRestaurants.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/restau.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();

                Devis itemController = fxmlLoader.getController();
                itemController.setData(filteredRestaurants.get(i), myListener);

                if (column == 2) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restaurants = serviceR.getAll();
        if (!restaurants.isEmpty()) {
            setChosenRestau(restaurants.get(0));
            myListener = r -> setChosenRestau(r);
        }
        myListener = new MyListener() {
            public void onClickListener(tn.esprit.entities.Devis r) {
                setChosenRestau(r);
            }
        };

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDisplayedRestaurants();
        });

        // Set up the role filter listener
        roleFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterDisplayedRestaurants();
        });

        int column = 0;
        int row = 1;
        try {
             for (int i = 0; i < restaurants.size(); i++) {
           // for (int i = 0; i < Math.min(12, restaurants.size()); i++)

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/restau.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();

                Devis itemController = fxmlLoader.getController();
                itemController.setData(restaurants.get(i), myListener);

                if (column == 2) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);


        }
    }
}
