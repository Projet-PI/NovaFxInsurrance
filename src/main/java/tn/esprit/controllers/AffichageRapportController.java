package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.controllers.USER.AdminUserController;
import tn.esprit.entities.RapportClient;
import tn.esprit.services.ServiceRapport;
import tn.esprit.services.ServiceSinistre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AffichageRapportController {

    @FXML
    private ListView<RapportClient> RapportListView;
    @FXML
    private TextField searchField;

    private ObservableList<RapportClient> rapportObservableList;
    private List<RapportClient> rapports;

    private ServiceRapport rapportService;
    private ServiceSinistre sinistreService;

    @FXML
    private Pagination pagination;

    @FXML
    public void initialize() {
        rapportService = new ServiceRapport();
        sinistreService = new ServiceSinistre();
        populateListView();

        if (rapports != null && !rapports.isEmpty()) {
            int itemsPerPage = 5;
            int pageCount = (int) Math.ceil((double) rapports.size() / itemsPerPage);
            pagination.setPageCount(pageCount);

            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                int fromIndex = newIndex.intValue() * itemsPerPage;
                int toIndex = Math.min(fromIndex + itemsPerPage, rapports.size());
                RapportListView.setItems(FXCollections.observableArrayList(rapports.subList(fromIndex, toIndex)));
            });

            pagination.setCurrentPageIndex(0);
        }
    }

    private void populateListView() {
        try {

            rapports = rapportService.afficher();
            rapportObservableList = FXCollections.observableArrayList(rapports);

            RapportListView.setItems(rapportObservableList);

            RapportListView.setCellFactory(param -> new RapportListCell());

            setupSearchListener();
        } catch (SQLException e) {
            showErrorDialog("Erreur", "Une erreur est survenue lors de la récupération des rapports.");
        }
    }
    @FXML
    private void toRapports(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageRapport.fxml"));
            Parent ajoutRapportParent = fxmlLoader.load();
            AjouterRapportController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) searchField.getScene().getWindow();
            Scene ajoutRapportScene = new Scene(ajoutRapportParent, 800, 400);
            currentStage.setScene(ajoutRapportScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void toSinistre(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageSinistre.fxml"));
            Parent ajoutRapportParent = fxmlLoader.load();
            AjouterRapportController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) searchField.getScene().getWindow();
            Scene ajoutRapportScene = new Scene(ajoutRapportParent, 800, 400);
            currentStage.setScene(ajoutRapportScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                search(newValue.trim());
            }
        });
    }

    private void search(String query) {
        if (query.isEmpty()) {
            RapportListView.setItems(rapportObservableList);
        } else {
            List<RapportClient> filteredList = rapports.stream()
                    .filter(rapport -> rapportContainsQuery(rapport, query))
                    .collect(Collectors.toList());
            RapportListView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    private boolean rapportContainsQuery(RapportClient rapport, String query) {
        return String.valueOf(rapport.getisFautif()).contains(query) || String.valueOf(rapport.getRapport()).contains(query)
                || String.valueOf(rapport.getPourcentage()).contains(query) || String.valueOf(rapport.getClientNom()).contains(query)
                || String.valueOf(rapport.getClientPrenom()).contains(query);
    }

    @FXML
    private void handleSortButtonP(ActionEvent event) {
        if (rapports != null) {
            Collections.sort(rapports, Comparator.comparingInt(RapportClient::getPourcentage));
            updateListView();
        }
    }
    @FXML
    private void handleMenu(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageSinistre.fxml"));
            Parent menuParent = fxmlLoader.load();
            AffichageSinistreController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) searchField.getScene().getWindow();
            Scene menuScene = new Scene(menuParent, 1000, 600);
            currentStage.setScene(menuScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleSortButtonId(ActionEvent event) {
        if (rapports != null) {
            Collections.sort(rapports, Comparator.comparingInt(RapportClient::getRapportClientId));
            updateListView();
        }
    }

    private void updateListView() {
        rapportObservableList.setAll(rapports);
        pagination.setCurrentPageIndex(0);
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjoutRapport.fxml"));
            Parent ajoutRapportParent = fxmlLoader.load();
            AjouterRapportController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) RapportListView.getScene().getWindow();
            Scene ajoutRapportScene = new Scene(ajoutRapportParent, 1000, 600);
            currentStage.setScene(ajoutRapportScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AdminUser.fxml"));
            Parent AdminUserController = fxmlLoader.load();
            AdminUserController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) RapportListView.getScene().getWindow();
            Scene AdminUserScene = new Scene(AdminUserController, 1000, 600);
            currentStage.setScene(AdminUserScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class RapportListCell extends ListCell<RapportClient> {
        @Override
        protected void updateItem(RapportClient item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                VBox card = new VBox(10);
                card.setPadding(new Insets(10));
                card.setBackground(new Background(new BackgroundFill(Color.web("#007a80"), CornerRadii.EMPTY, Insets.EMPTY)));

                String isFautifText = item.getisFautif()==1 ? "Fautif" : "Non Fautif";
                Label isFautifLabel = new Label(isFautifText);
                Label pourcentageLabel = new Label("Pourcentage de remboursement: " + item.getPourcentage() + "%");
                Label rapportLabel = new Label("Rapport: " + item.getRapport());
                Label rapportCliientId = new Label("Id: " + item.getRapportClientId());
                Label clientNom = new Label("Nom: " + item.getClientNom());
                Label clientPrenom = new Label("Prenom: " + item.getClientPrenom());

                isFautifLabel.setTextFill(item.getisFautif()==1 ? Color.RED : Color.GREEN);

                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                Button editButton = new Button("Edit");
                Button deleteButton = new Button("Delete");

                editButton.setStyle("-fx-background-color: #A0A603; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                editButton.setPadding(new Insets(5, 10, 5, 10));
                deleteButton.setPadding(new Insets(5, 10, 5, 10));

                editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
                editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #f77777; -fx-text-fill: white;"));
                deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #f77736; -fx-text-fill: white;"));

                buttonBox.getChildren().addAll(editButton, deleteButton);
                isFautifLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");
                pourcentageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");
                rapportLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");
                rapportCliientId.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");
                clientNom.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");
                clientPrenom.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-text-fill: white;");

                card.getChildren().addAll(isFautifLabel,clientNom, clientPrenom, pourcentageLabel, rapportLabel,rapportCliientId, buttonBox);
                setGraphic(card);

                editButton.setOnAction(event -> handleEditButton(item));
                deleteButton.setOnAction(event -> handleDeleteButton(item));
            }
        }
    }

    private void handleEditButton(RapportClient item) {
        if (item == null) {
            System.err.println("Error: Rapport item is null.");
            return;
        }

        System.out.println("Edit: " + item);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DetailsRapport.fxml"));
            Parent editPageParent = fxmlLoader.load();
            DetailsRapportController controller = fxmlLoader.getController();
            controller.initData(item);

            Stage currentStage = (Stage) RapportListView.getScene().getWindow();
            Scene editPageScene = new Scene(editPageParent, 1000, 600);
            currentStage.setScene(editPageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButton(RapportClient rapportClient) {
        System.out.println("Delete: " + rapportClient);
        try {
            ServiceRapport serviceRapport = new ServiceRapport();
            serviceRapport.supprimer(rapportClient.getRapportClientId());
            System.out.println("Deleted");

            rapportObservableList.remove(rapportClient);
        } catch (SQLException e) {
            showErrorDialog("Erreur", "Une erreur est survenue lors de la suppression du rapport.");
        }
    }
}
