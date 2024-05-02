package tn.esprit.controles;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import tn.esprit.Service.Assurance_s;
import tn.esprit.entity.Assurance;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherBack {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Assurance> tableau;

    @FXML
    private TableColumn<Assurance, Number> idtv;

    @FXML
    private TableColumn<Assurance, String> nomtv;

    @FXML
    private TableColumn<Assurance, String> typetv;

    @FXML
    private TableColumn<Assurance, Number> montanttv;

    @FXML
    private TableColumn<Assurance, String> datedebuttv;

    @FXML
    private TableColumn<Assurance, String> datefintv;

    @FXML
    private TableColumn<Assurance, Assurance> deleteColumn;
    @FXML
    private TextField filterField;
    private Assurance_s assuranceService = Assurance_s.getInstance();
    private FilteredList<Assurance> filteredData;
    @FXML
    void initialize() {
        initializeTable();
        refreshAssuranceList();
        setupRowSelectListener();
        filteredData = new FilteredList<>(tableau.getItems(), p -> true);

        // Set the filter Predicate whenever the filter changes
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(assurance -> {
                // If filter text is empty, display all assurances
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare each assurance's attributes with the filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (assurance.getNom() != null && assurance.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches nom
                } else if (assurance.getType() != null && assurance.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches type
                }  else if (Float.toString(assurance.getMontant()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches montant
                }
                // Add more conditions if needed for other attributes

                return false; // Does not match any filter criteria
            });
        });
        // Wrap the FilteredList in a SortedList
        // SortedList will auto-update whenever the filter predicate changes
        // and ensure that the TableView is always sorted correctly
        tableau.setItems(filteredData);
    }


    private void setupRowSelectListener() {
        tableau.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Assuming you have TextField or Label components for each field
                nomtv.setText(newValue.getNom());
                typetv.setCellValueFactory(new PropertyValueFactory<>("type"));
                montanttv.setText(String.valueOf(newValue.getMontant()));
                datedebuttv.setText(newValue.getDate_debut().toString());
                datefintv.setText(newValue.getDate_fin().toString());
            }
        });
    }

    private void refreshAssuranceList() {
        try {
            ObservableList<Assurance> assuranceList = FXCollections.observableArrayList(assuranceService.show());
            tableau.setItems(assuranceList);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }
    }

    void initializeTable() {
        idtv.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomtv.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typetv.setCellValueFactory(new PropertyValueFactory<>("type"));
        montanttv.setCellValueFactory(new PropertyValueFactory<>("montant"));
        datedebuttv.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        datefintv.setCellValueFactory(new PropertyValueFactory<>("date_fin"));



        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(new Callback<TableColumn<Assurance, Assurance>, TableCell<Assurance, Assurance>>() {
            @Override
            public TableCell<Assurance, Assurance> call(TableColumn<Assurance, Assurance> param) {
                return new TableCell<Assurance, Assurance>() {
                    final Button deleteButton = new Button("Delete");

                    @Override
                    protected void updateItem(Assurance assurance, boolean empty) {
                        super.updateItem(assurance, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(deleteButton);
                            deleteButton.setOnAction(event -> {
                                try {
                                    assuranceService.delete(assurance.getId());
                                    refreshAssuranceList();
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText("Assurance supprimée avec succès");
                                    alert.show();
                                } catch (SQLException e) {
                                    e.printStackTrace(); // Handle the exception according to your application's error handling strategy
                                }
                            });
                        }
                    }
                };
            }
        });
    }

    public void navigateToAjouterAssurance(ActionEvent actionEvent) {
    }
}
