package tn.esprit.controllers.ASSURANCE;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import tn.esprit.services.Assurance_s;
import tn.esprit.entities.Assurance;

import java.io.IOException;

import javafx.util.Callback;

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

    private final int itemsPerPage = 3;
    private List<Assurance> coursList;


    private final Assurance_s assurances = new Assurance_s();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            coursList = Assurance_s.getInstance().show();
            setupPagination();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPagination() {

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


                int row = (i - startIndex) / 3;
                int col = (i - startIndex) % 3;


                GridPane.setConstraints(cardPane, col, row);
                pageGrid.getChildren().add(cardPane);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return pageGrid;
    }




}
