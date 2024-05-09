package tn.esprit.controllers.ASSURANCE;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Contrat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherFrontcontrat implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Pagination pagination;

    private List<Contrat> contratsList;
    private final Contrat_s contratService = new Contrat_s();
    private final int itemsPerPage = 3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contratsList = contratService.show(); // Use instance variable contratService
            setupPagination();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) contratsList.size() / itemsPerPage);

        // Configure pagination
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        // Create a new GridPane for each page
        GridPane pageGrid = new GridPane();
        pageGrid.setHgap(10);
        pageGrid.setVgap(10);

        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, contratsList.size());

        // Add contracts to the page horizontally
        for (int i = startIndex; i < endIndex; i++) {
            Contrat contrat = contratsList.get(i);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/cardc.fxml"));
            try {
                AnchorPane cardPane = loader.load();
                Cardc cardController = loader.getController();
                cardController.setData(contrat);

                int col = (i - startIndex) % itemsPerPage;
                pageGrid.addColumn(col, cardPane); // Add card to the column
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageGrid;
    }
}
