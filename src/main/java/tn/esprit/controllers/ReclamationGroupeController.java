package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationEntryService;
import tn.esprit.services.ReclamationGroupeService;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReclamationGroupeController {

    @FXML
    private ListView<reclamation_groupe> listView;

    public void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
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

        ReclamationGroupeService service = new ReclamationGroupeService();
        ResultSet rs = service.GetAll();

        try {
            while (rs.next()) {
                listView.getItems().add(new reclamation_groupe(rs.getInt("id"), rs.getInt("user_id_id"), rs.getInt("reclamation_agent_id_id"), rs.getString("name"), rs.getString("day_time"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        ReclamationGroupeService service = new ReclamationGroupeService();
        service.DeleteReclamationGroupe(item.id);
        showAlert("Do you want to delete " + item.getName(), Alert.AlertType.WARNING);
        // refresh the list view
        ResultSet rs = service.GetAll();
        listView.getItems().clear();
        try {
            while (rs.next()) {
                listView.getItems().add(new reclamation_groupe(rs.getInt("id"), rs.getInt("user_id_id"), rs.getInt("reclamation_agent_id_id"), rs.getString("name"), rs.getString("day_time"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleViewButton(reclamation_groupe item) {
        // Perform delete action here
        System.out.println("View: " + item.getName());
        ReclamationEntryService service1 = new ReclamationEntryService();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-entry.fxml"));
            Parent reclamationEntryParent = fxmlLoader.load();

            ReclamationEntryController controller = fxmlLoader.getController();
            controller.setReclamationGroupe(item);

            Stage currentStage = (Stage) listView.getScene().getWindow();

            Scene reclamationEntryScene = new Scene(reclamationEntryParent, 870, 600);
            currentStage.setScene(reclamationEntryScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openReclamationGroupAddForm(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation_groupe_form_add.fxml"));
            Parent addPageParent = fxmlLoader.load();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene addPageScene = new Scene(addPageParent, 870, 600);
            currentStage.setScene(addPageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
