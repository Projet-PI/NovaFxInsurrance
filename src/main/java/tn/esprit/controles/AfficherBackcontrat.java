package tn.esprit.controles;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.Service.Contrat_s;
import tn.esprit.entity.Contrat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficherBackcontrat {

    @FXML
    private TableView<Contrat> tableau;

    @FXML
    private TableColumn<Contrat, Integer> idtv;

    @FXML
    private TableColumn<Contrat, Integer> dureetv;

    @FXML
    private TableColumn<Contrat, String> dateDeSouscriptiontv;

    @FXML
    private TableColumn<Contrat, String> typeCouverturetv;
    @FXML
    private TextField filterField;



    private Contrat_s contratService;
    private ObservableList<Contrat> contratsList;
    private FilteredList<Contrat> filteredData;


    Connection conn= DataBase.getInstance().getConn() ;
    @FXML
    void navigateToAjouterAssurance(ActionEvent event) {

    }

    @FXML
    void initialize() {
        // Initialize contratService
        contratService = new Contrat_s();

        // Load and show contrats
        loadAndShowContrats();

        // Initialize the filtered data with all contrats
        filteredData = new FilteredList<>(contratsList, p -> true);

        // Set the filter Predicate whenever the filter changes
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(contrat -> {
                // If filter text is empty, display all contrats
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare each contrat's attributes with the filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(contrat.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches id
                } else if (String.valueOf(contrat.getDuree()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches duree
                } else if (contrat.getDate_de_souscription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches date_de_souscription
                } else if (contrat.getType_couverture().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches type_couverture
                }

                return false; // Does not match any filter criteria
            });
        });

        // Wrap the filtered data in a SortedList
        // SortedList will auto-update whenever the filter predicate changes
        // and ensure that the TableView is always sorted correctly
        SortedList<Contrat> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableau.comparatorProperty());
        tableau.setItems(sortedData);
    }

    private void loadAndShowContrats() {
        try {
            contratsList = FXCollections.observableArrayList(loadContrats());
            showContrats();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }
    }



    ObservableList<Contrat> loadContrats() throws SQLException {
        ObservableList<Contrat> contrats = FXCollections.observableArrayList();

        // SQL query to select all ingredients
        String query = "SELECT * FROM contrat";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            // Iterate over the result set and create Ingredient objects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int duree = resultSet.getInt("duree");
                String date_de_souscription = resultSet.getString("date_de_souscription");
                String type_couverture = resultSet.getString("type_couverture");
                // Add the ingredient to the list
                System.out.println("id: " + id + ", duree: " + duree + ", date_de_souscription: " + date_de_souscription + ", type_couverture: " + type_couverture);

                contrats.add(new Contrat(id, duree, date_de_souscription , type_couverture));
            }
        }

        return contrats;
    }
    // Method to delete the selected contract
    private void deleteContract(Contrat contrat) {
        try {
            contratService.delete(contrat.getId());
            loadContrats(); // Reload contracts after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



public void showContrats ()
{
    ObservableList<Contrat> list = null;
    try {
        list = loadContrats();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    // Add columns for delete and update buttons
    TableColumn<Contrat, Void> deleteCol = new TableColumn<>("Delete");
    TableColumn<Contrat, Void> updateCol = new TableColumn<>("Update");

    // Set cell factories for delete and update columns
    deleteCol.setCellFactory(param -> new TableCell<Contrat, Void>() {
        private final Button deleteButton = new Button("Delete");

        {
            deleteButton.setOnAction(event -> {
                Contrat contrat = getTableView().getItems().get(getIndex());
                deleteContract(contrat);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(deleteButton);
            }
        }
    });

    updateCol.setCellFactory(param -> new TableCell<Contrat, Void>() {
        private final Button updateButton = new Button("Update");

        {
            updateButton.setOnAction(event -> {
                Contrat contrat = getTableView().getItems().get(getIndex());
                //handleUpdateAction(recette);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(updateButton);
            }
        }
    });

    // Add the delete and update columns to the TableView
    tableau.getColumns().addAll(deleteCol);

    idtv.setCellValueFactory(new PropertyValueFactory<>("id"));
    dureetv.setCellValueFactory(new PropertyValueFactory<>("duree"));
    dateDeSouscriptiontv.setCellValueFactory(new PropertyValueFactory<>("date_de_souscription"));
    typeCouverturetv.setCellValueFactory(new PropertyValueFactory<>("type_couverture"));



    tableau.setItems(list);



}


}