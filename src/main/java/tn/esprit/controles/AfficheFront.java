package tn.esprit.controles;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import tn.esprit.Service.Assurance_s;
import tn.esprit.entity.Assurance;

import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficheFront implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private TextField filterField;
    private FilteredList<Assurance> filteredAssurances;
    private ObservableList<Assurance> allAssurances;
    @FXML
    private Pagination pagination;

    private final int itemsPerPage = 3; // Nombre d'éléments par page
    private List<Assurance> coursList;


    private final Assurance_s assurances = new Assurance_s();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            coursList = Assurance_s.getInstance().show();
            setupPagination();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }

    private void setupPagination() {
        // Calculer le nombre total de pages
        int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);

        // Configurer la pagination
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }
    private Node createPage(int pageIndex) {
        // Créer un nouveau GridPane pour chaque page
        GridPane pageGrid = new GridPane();
        pageGrid.setHgap(10);
        pageGrid.setVgap(10);

        // Calculer l'indice de début et de fin pour cette page
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, coursList.size());

        // Ajouter les cartes à la page
        for (int i = startIndex; i < endIndex; i++) {
            Assurance cours = coursList.get(i);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Card.fxml"));
            try {
                AnchorPane cardPane = loader.load();
                Card cardController = loader.getController();
                cardController.setData(cours);

                // Calculer la position de la carte dans le GridPane
                int row = (i - startIndex) / 3; // Chaque ligne contient 3 cartes
                int col = (i - startIndex) % 3;

                // Définir les contraintes de positionnement de la carte dans le GridPane
                GridPane.setConstraints(cardPane, col, row);
                pageGrid.getChildren().add(cardPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageGrid;
    }



    @FXML
    void goToContratButton(ActionEvent event) {
        // Define the action to perform when the button is clicked
    }
}
