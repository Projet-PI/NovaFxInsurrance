package tn.esprit.controllers.ASSURANCE;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Contrat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.utils.DataBase;

import java.io.IOException;
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
    private TableColumn<Contrat, Void> modifField;

    @FXML
    private TextField filterField;

    private Contrat_s contratService;
    private ObservableList<Contrat> contratsList;
    private FilteredList<Contrat> filteredData;
    private Connection conn = DataBase.getInstance().getConx();

    @FXML
    void navigateToAjouterAssurance(ActionEvent event) {

    }


    @FXML
    void initialize() {
        // Initialize contratService
        contratService = new Contrat_s();

        // Load and show contrats
        loadAndShowContrats();
        filteredData = new FilteredList<>(contratsList, p -> true);

// Set the filter Predicate whenever the filter changes
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Filter changed: " + newValue);
            filteredData.setPredicate(contract -> {

                // If filter text is empty, display all contracts
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare id, duration, subscription date, and coverage type of every contract with filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(contract.getId()).contains(lowerCaseFilter)) {
                    return true; // Filter matches id
                } else if (String.valueOf(contract.getDuree()).contains(lowerCaseFilter)) {
                    return true; // Filter matches duration
                } else if (contract.getDate_de_souscription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches subscription date
                } else if (contract.getType_couverture().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches coverage type
                }
                return false;
            });
        });

// Wrap the filtered data in a SortedList
        SortedList<Contrat> sortedData = new SortedList<>(filteredData);

// Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(tableau.comparatorProperty());

// Add the sorted (and filtered) data to the TableView
        tableau.setItems(sortedData);
        initModifierColumn();

        // Add columns for delete and update buttons
        TableColumn<Contrat, Void> deleteCol = new TableColumn<>("Delete");
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

        // Add the delete column to the TableView
        tableau.getColumns().add(deleteCol);

        // Set property value factory for other columns
        idtv.setCellValueFactory(new PropertyValueFactory<>("id"));
        dureetv.setCellValueFactory(new PropertyValueFactory<>("duree"));
        dateDeSouscriptiontv.setCellValueFactory(new PropertyValueFactory<>("date_de_souscription"));
        typeCouverturetv.setCellValueFactory(new PropertyValueFactory<>("type_couverture"));
    }


    private void openModifierContratDialog(Contrat contrat) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/ModifierContrat.fxml"));
        try {
            Parent root = loader.load();
            ModifierContrat controller = loader.getController();
            controller.initData(contrat);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadAndShowContrats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAndShowContrats() {
        try {
            contratsList = FXCollections.observableArrayList(loadContratsFromDatabase());
            showContrats();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }
    }

    private void initModifierColumn() {
        TableColumn<Contrat, Void> editCol = new TableColumn<>("Modifier");
        editCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setOnAction(event -> {
                    Contrat contrat = getTableView().getItems().get(getIndex());
                    openModifierContratDialog(contrat);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        // Add the edit column to the TableView
        tableau.getColumns().add(editCol);
    }

    private ObservableList<Contrat> loadContratsFromDatabase() throws SQLException {
        ObservableList<Contrat> contrats = FXCollections.observableArrayList();

        // SQL query to select all contracts
        String query = "SELECT * FROM contrat";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            // Iterate over the result set and create Contrat objects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int duree = resultSet.getInt("duree");
                String date_de_souscription = resultSet.getString("date_de_souscription");
                String type_couverture = resultSet.getString("type_couverture");

                contrats.add(new Contrat(id, duree, date_de_souscription, type_couverture));
            }
        }

        return contrats;
    }

    // Method to delete the selected contract
    private void deleteContract(Contrat contrat) {
        try {
            contratService.delete(contrat.getId());
            loadAndShowContrats(); // Reload contracts after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showContrats() {
        ObservableList<Contrat> list = null;
        try {
            list = loadContratsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableau.setItems(list);
    }


    public void goToBackUser(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminuser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error gracefully, like showing an alert or logging it
        }
    }

}
