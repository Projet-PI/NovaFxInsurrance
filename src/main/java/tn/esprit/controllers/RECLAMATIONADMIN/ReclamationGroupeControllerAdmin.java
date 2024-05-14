package tn.esprit.controllers.RECLAMATIONADMIN;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationEntryServiceAdmin;
import tn.esprit.services.ReclamationGroupeServiceAdmin;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReclamationGroupeControllerAdmin {

    @FXML
    private ListView<reclamation_groupe> listView;

    @FXML
    private TextField searchField;

    // Define pagination parameters
    private int pageNumber = 1;
    private final int pageSize = 2; // Number of items per page

    private String filter = null;

    public void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadListView(int pageNumber, int pageSize, String searchQuery, String filter) {
        listView.getItems().clear(); // Clear existing items from ListView

        ReclamationGroupeServiceAdmin service = new ReclamationGroupeServiceAdmin();
        ResultSet rs = service.GetAll(pageNumber, pageSize, searchQuery, filter);

        try {
            while (rs.next()) {
                listView.getItems().add(new reclamation_groupe(rs.getInt("id"), rs.getInt("user_id_id"), rs.getInt("reclamation_agent_id_id"), rs.getString("name"), rs.getString("day_time"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(param -> new ListCell<reclamation_groupe>() {
            @Override
            protected void updateItem(reclamation_groupe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox card = new VBox(10);
                    card.setPadding(new Insets(10));
                    card.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

                    Label nameLabel = new Label(item.getName());
                    nameLabel.setStyle("-fx-font-weight: bold");

                    Label dayTimeLabel = new Label("Day Time: " + item.getDay_time());
                    Label statusLabel = new Label("Status: " + item.getStatus());

                    HBox buttonBox = new HBox(10);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);
                    Button editButton = new Button("Edit");
                    Button deleteButton = new Button("Delete");
                    Button viewButton = new Button("View");
                    // add style to the buttons
                    editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                    viewButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    // add padding to the buttons
                    editButton.setPadding(new Insets(5, 10, 5, 10));
                    deleteButton.setPadding(new Insets(5, 10, 5, 10));
                    // add hover effect to the buttons
                    editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
                    editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
                    deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #f77777; -fx-text-fill: white;"));
                    deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #f77736; -fx-text-fill: white;"));
                    viewButton.setOnMouseEntered(e -> viewButton.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white;"));
                    viewButton.setOnMouseExited(e -> viewButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));
                    buttonBox.getChildren().addAll(editButton, deleteButton, viewButton);

                    card.getChildren().addAll(nameLabel, dayTimeLabel, statusLabel, buttonBox);

                    setGraphic(card);

                    editButton.setOnAction(event -> handleEditButton(item));
                    deleteButton.setOnAction(event -> handleDeleteButton(item));
                    viewButton.setOnAction(event -> handleViewButton(item));
                }
            }
        });

        loadListView(pageNumber, pageSize, null, null); // Load initial data

        // Listen for changes in the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pageNumber = 1; // Reset page number when search criteria changes
            loadListView(pageNumber, pageSize, newValue, filter); // Reload list with search criteria
        });
    }

    public void previousPage(ActionEvent actionEvent) {
        if (pageNumber > 1) {
            pageNumber--;
            loadListView(pageNumber, pageSize, searchField.getText(),filter); // Reload list for previous page
        }
    }

    // Next page button handler
    public void nextPage(ActionEvent actionEvent) {
        System.out.println("Next page");
        pageNumber++;
        loadListView(pageNumber, pageSize, searchField.getText(),filter); // Reload list for next page
    }


    private void handleEditButton(reclamation_groupe item) {
        // Perform edit action here
        System.out.println("Edit: " + item.getName());
        try {
            // Load the FXML file for the edit page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation_groupe_form.fxml"));
            Parent editPageParent = fxmlLoader.load();

            // Get the controller of the edit page
            ReclamationGroupeFormController controller = fxmlLoader.getController();
            controller.setReclamationGroupe(item);

            // Get the current stage
            Stage currentStage = (Stage) listView.getScene().getWindow();

            // Switch the scene to the edit page
            Scene editPageScene = new Scene(editPageParent, 870, 600);
            currentStage.setScene(editPageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButton(reclamation_groupe item) {
        // Perform delete action here
        System.out.println("Delete: " + item.getName());
        ReclamationGroupeServiceAdmin service = new ReclamationGroupeServiceAdmin();
        service.DeleteReclamationGroupe(item.id);
        showAlert("Do you want to delete " + item.getName(), Alert.AlertType.WARNING);
        // refresh the list view
        loadListView(pageNumber, pageSize, null,filter); // Load initial data

        // Listen for changes in the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pageNumber = 1; // Reset page number when search criteria changes
            loadListView(pageNumber, pageSize, newValue, filter); // Reload list with search criteria
        });
    }

    private void handleViewButton(reclamation_groupe item) {
        // Perform delete action here
        System.out.println("View: " + item.getName());
        ReclamationEntryServiceAdmin service1 = new ReclamationEntryServiceAdmin();
        ResultSet rs1 = service1.GetAll(item);
        // print all reclamation entries
        try {
            while (rs1.next()) {
                System.out.println("Reclamation Entry ID: " + rs1.getInt("id"));
                System.out.println("Prompt: " + rs1.getString("prompt"));
                System.out.println("Day Time: " + rs1.getString("day_time"));
                System.out.println("Response: " + rs1.getString("response"));
                System.out.println("Status: " + rs1.getString("status"));
                System.out.println("Response Type: " + rs1.getString("response_type"));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // redirect to the reclamation entry page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-entry-admin.fxml"));
            Parent reclamationEntryParent = fxmlLoader.load();

            ReclamationEntryControllerAdmin controller = fxmlLoader.getController();
            controller.setReclamationGroupe(item);

            Stage currentStage = (Stage) listView.getScene().getWindow();

            Scene reclamationEntryScene = new Scene(reclamationEntryParent, 870, 600);
            currentStage.setScene(reclamationEntryScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filterProcessed(ActionEvent event) {
        System.out.println("Filter processed");
        filter = "completed";
        loadListView(pageNumber, pageSize, searchField.getText(),filter);
    }

    public void filterPending(ActionEvent event) {
        System.out.println("Filter ddd");
        filter = "pending";
        loadListView(pageNumber, pageSize, searchField.getText(),filter);
    }
}
