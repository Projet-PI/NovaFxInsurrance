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
import tn.esprit.entities.Sinistre;
import tn.esprit.services.ServiceSinistre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AffichageSinistreController {

    @FXML
    private ListView<Sinistre> sinistreListView;
    @FXML
    private TextField searchField;
    private ObservableList<Sinistre> sinistreObservableList;
    private List<Sinistre> sinistres;

    private ServiceSinistre serviceSinistre;

    @FXML
    private Pagination pagination;

    @FXML
    public void initialize() {
        serviceSinistre = new ServiceSinistre();
        populateListView();
        int itemsPerPage = 5;
        int pageCount = (int) Math.ceil((double) sinistres.size() / itemsPerPage);
        pagination.setPageCount(pageCount);

        // Set listener for page changes
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            int fromIndex = newIndex.intValue() * itemsPerPage;
            int toIndex = Math.min(fromIndex + itemsPerPage, sinistres.size());
            sinistreListView.setItems(FXCollections.observableArrayList(sinistres.subList(fromIndex, toIndex)));
        });

        // Initialize first page
        pagination.setCurrentPageIndex(0);
    }





    private void populateListView() {
        try {
            sinistres = serviceSinistre.afficher();
            sinistreObservableList = FXCollections.observableArrayList(sinistres);
            sinistreListView.setItems(sinistreObservableList);
            sinistreListView.setCellFactory(param -> new SinistreListCell());
            setupSearchListener(); // Call method to setup search functionality
        } catch (SQLException e) {
            afficherMessageErreur("Erreur", "Une erreur est survenue lors de la récupération des sinistres.");
        }

    }
    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                search(newValue.trim()); // Trim whitespace and perform search
            }
        });
    }
    private void search(String query) {
        if (query.isEmpty()) {
            sinistreListView.setItems(sinistreObservableList); // Reset ListView if search query is empty
        } else {
            List<Sinistre> filteredList = sinistres.stream()
                    .filter(sinistre -> sinistreContainsQuery(sinistre, query))
                    .collect(Collectors.toList());
            sinistreListView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    private boolean sinistreContainsQuery(Sinistre sinistre, String query) {
        return String.valueOf(sinistre.getId()).contains(query) ||
                String.valueOf(sinistre.getSinistre_client_id()).contains(query) ||
                String.valueOf(sinistre.getSinistre_expert_id()).contains(query) ||
                String.valueOf(sinistre.getIs_fautif()).contains(query) ||
                String.valueOf(sinistre.getPourcentage()).contains(query) ||
                sinistre.getRapport().contains(query);
    }

    @FXML
    private void handleMenu(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent menuParent = fxmlLoader.load();
            AfficheController controller = fxmlLoader.getController();

            Stage currentStage = (Stage) searchField.getScene().getWindow();
            Scene menuScene = new Scene(menuParent, 800, 400);
            currentStage.setScene(menuScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSortButtonpourcentage(ActionEvent event) {
        if (sinistres != null) {
            Collections.sort(sinistres, Comparator.comparingInt(Sinistre::getPourcentage));
            updateListView();
        }
    }
    /*private void handleEmailButton(Sinistre sinistre) {
        // Fetch user's email corresponding to the client ID and send email
        try {
            User user = UserService.getUserById(sinistre.getSinistre_client_id());
            if (user != null) {
                String userEmail = user.getEmail();
                sendEmail(userEmail);
            } else {
                afficherMessageErreur("Erreur", "Utilisateur non trouvé pour l'ID client: " + sinistre.getSinistre_client_id());
            }
        } catch (SQLException e) {
            afficherMessageErreur("Erreur", "Une erreur est survenue lors de la récupération de l'utilisateur.");
        }
    }
    private void sendEmail(String userEmail) {
        // Code to send email using Mailjet API
        try {
            MailjetRequest request;
            MailjetResponse response;

            ClientOptions options = ClientOptions.builder()
                    .apiKey("416f08cfee33285032ad91897f19a3b0")
                    .apiSecretKey("dd6f56c3810ecc8bd389a6e1511112b4")
                    .build();

            MailjetClient client = new MailjetClient(options);

            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", "aymeeen.gh@gmail.com")
                                            .put("Name", "Aymen"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", userEmail) // Use the fetched user's email
                                                    .put("Name", "Client")))
                                    .put(Emailv31.Message.SUBJECT, "Nouveau Sinistre")
                                    .put(Emailv31.Message.TEXTPART, "Vous avez un nouveau sinistre sous votre nom")
                                    .put(Emailv31.Message.HTMLPART, "<h3>Please check our website</h3>")));
            response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
            afficherMessageErreur("Erreur", "Une erreur est survenue lors de l'envoi de l'email.");
        }
    }*/






    @FXML
    private void handleSortButtonClientID(ActionEvent event) {
        if (sinistres != null) {
            Collections.sort(sinistres, Comparator.comparingInt(Sinistre::getSinistre_client_id));
            updateListView();
        }
    }

    private void updateListView() {
        sinistreObservableList.setAll(sinistres);
        pagination.setCurrentPageIndex(0); // Go to the first page after sorting
    }

    private void afficherMessageErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleAjouterButton() {
        try {
            // Récupérer la valeur sélectionnée de ComboBox isFautif


            // Load the FXML file for the ajout sinistre page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AjouterSinistre.fxml"));
            Parent ajoutSinistreParent = fxmlLoader.load();

            // Get the controller of the ajout sinistre page
            AjouterSinistreController controller = fxmlLoader.getController();
            // Initialize any data or perform any setup if needed

            // Get the current stage
            Stage currentStage = (Stage) sinistreListView.getScene().getWindow();

            // Switch the scene to the ajout sinistre page
            Scene ajoutSinistreScene = new Scene(ajoutSinistreParent, 800, 600);
            currentStage.setScene(ajoutSinistreScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private class SinistreListCell extends ListCell<Sinistre> {
        @Override
        protected void updateItem(Sinistre item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                VBox card = new VBox(10);
                card.setPadding(new Insets(10));
                card.setBackground(new Background(new BackgroundFill(Color.web("#F2E0C9"), CornerRadii.EMPTY, Insets.EMPTY)));

                Label clientIdLabel = new Label("Client ID: " + item.getSinistre_client_id());
                Label expertIdLabel = new Label("Expert ID: " + item.getSinistre_expert_id());
                String isFautifText = item.getIs_fautif() == 1 ? "Fautif" : "Non Fautif";
                Label isFautifLabel = new Label(isFautifText);
                Label pourcentageLabel = new Label("Pourcentage de remboursement: " + item.getPourcentage() + "%");
                Label rapportLabel = new Label("Rapport: " + item.getRapport());
                if (item.getIs_fautif() == 1) {
                    isFautifLabel.setTextFill(Color.RED);
                } else {
                    isFautifLabel.setTextFill(Color.GREEN);
                }
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                Button editButton = new Button("Edit");
                Button deleteButton = new Button("Delete");
                // Add style to the buttons
                editButton.setStyle("-fx-background-color: #A0A603; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                // Add padding to the buttons
                editButton.setPadding(new Insets(5, 10, 5, 10));
                deleteButton.setPadding(new Insets(5, 10, 5, 10));
                // Add hover effect to the buttons
                editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
                editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #f77777; -fx-text-fill: white;"));
                deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #f77736; -fx-text-fill: white;"));
                buttonBox.getChildren().addAll(editButton, deleteButton);
// Set font size and style for labels
                // Set font style for labels
                clientIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
                expertIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
                isFautifLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
                pourcentageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
                rapportLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");


                card.getChildren().addAll(clientIdLabel, expertIdLabel, isFautifLabel, pourcentageLabel, rapportLabel, buttonBox);

                setGraphic(card);

                editButton.setOnAction(event -> handleEditButton(item));
                deleteButton.setOnAction(event -> handleDeleteButton(item));

            }
        }
    }

    private void handleEditButton(Sinistre item) {
        // Implement the logic for edit action here
        if (item == null) {
            System.err.println("Error: Sinistre item is null.");
            return;
        }

        System.out.println("Edit: " + item);
        try {
            // Load the FXML file for the edit page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailsSinistre.fxml"));
            Parent editPageParent = fxmlLoader.load();

            // Get the controller of the edit page
            DetailsSinistreController controller = fxmlLoader.getController();
            controller.initData(item);

            // Get the current stage
            Stage currentStage = (Stage) sinistreListView.getScene().getWindow();

            // Switch the scene to the edit page
            Scene editPageScene = new Scene(editPageParent, 600, 400);
            currentStage.setScene(editPageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void handleDeleteButton(Sinistre sinistre) {
        // Implement the logic for delete action here
        System.out.println("Delete: " + sinistre);
        try {
            // Create an instance of ServiceSinistre
            ServiceSinistre serviceSinistre = new ServiceSinistre();

            // Call the supprimer method to delete the sinistre from the database
            serviceSinistre.supprimer(sinistre.getId());

            // Remove the sinistre from the observable list
            sinistreObservableList.remove(sinistre);
        } catch (SQLException e) {
            // Handle any potential exceptions
            afficherMessageErreur("Erreur", "Une erreur est survenue lors de la suppression du sinistre.");
        }
    }


}
