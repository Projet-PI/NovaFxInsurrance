package tn.esprit.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import tn.esprit.entities.RapportClient;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceRapport;
import tn.esprit.services.ServiceSinistre;
import tn.esprit.services.ServiceUtilisateurs;
import tn.esprit.utils.SessionManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterRapportController implements Initializable {


    @FXML
    private Label PourcentageerrorLabel;
    @FXML
    private ComboBox<Integer> SinistreCombobox;
    @FXML
    private TextField pourcentageTextField;
    @FXML
    private ComboBox<String> isFautifComboBox;

    @FXML
    private ComboBox<Integer> expertIdComboBox;

    @FXML
    private Label ClienterrorLabel;

    @FXML
    private Label ExperterrorLabel;

    private SessionManager sessionManager;

    private ServiceUtilisateurs userService;
    private ServiceSinistre serviceSinistre;
    private RapportClient rapportClient;
    @FXML
    private Label fautiferrorLabel;

    @FXML
    private Label RapporterrorLabel;
    @FXML
    private TextField searchField;
    @FXML
    private Label userNameLabel;

    private ObservableList<RapportClient> rapportObservableList;
    private List<RapportClient> rapports;
    private ServiceRapport serviceRapport;
    @FXML
    private Label pourcentageLabel;
    @FXML
    private CodeArea markdownEditor;
    private String userName;
    private String email;

    public AjouterRapportController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set default user's name

        userService = new ServiceUtilisateurs();

        serviceSinistre = new ServiceSinistre();
        int userId =3; //sessionManager.getUser().getId();
        userName = getClientNameById(userId);
        email = getClientemailById(userId);
        userNameLabel.setText(userService.getUserNameWithId(userId));
        ObservableList<Integer> expertIds = getExpertIdsWithRole("[\"ROLE_EXPERT\"]");
        expertIdComboBox.setItems(expertIds);

        // Add an event handler to expertIdComboBox
        expertIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // If a new expert ID is selected, update the SinistreCombobox
                try {
                    List<Integer> sinistreIds = serviceSinistre.getSinistreIdsByUserExpert(userId, newValue);
                    ObservableList<Integer> sinistreIdList = FXCollections.observableArrayList(sinistreIds);
                    SinistreCombobox.setItems(sinistreIdList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add an event handler to SinistreCombobox
        SinistreCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // If a new sinistre ID is selected, update the pourcentageTextField
                try {
                    int pourcentage = serviceSinistre.getpourcentage(newValue);
                    pourcentageTextField.setText(Integer.toString(pourcentage));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
        // Add an event handler to SinistreCombobox
        SinistreCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // If a new sinistre ID is selected, update the isFautifComboBox
                int isFautif = serviceSinistre.getIsFautif(newValue);
                isFautifComboBox.setValue(isFautif == 1 ? "Fautif" : "Non Fautif");
            }
        });


    }







    private String getClientNameById(int id) {
        try {
            User user = userService.getUserById(id);
            return user != null ? user.getNom() : "";
        } catch (SQLException e) {
            e.printStackTrace();
            return ""; // Return empty string if there's an error
        }
    }
    private ObservableList<Integer> getExpertIdsWithRole(String role) {
        List<Integer> userIds = userService.getUserIdsWithRole("[\"ROLE_EXPERT\"]");
        return FXCollections.observableArrayList(userIds);
    }




    private ObservableList<Integer> getExpertIds() {
        List<Integer> expertUserIds = userService.getUserIdsWithRole("ROLE_EXPERT");
        return FXCollections.observableArrayList(expertUserIds);
    }

    private String getClientemailById(int id) {
        try {
            User user = userService.getUserById(id);
            return user != null ? user.getEmail() : "";
        } catch (SQLException e) {
            e.printStackTrace();
            return ""; // Return empty string if there's an error
        }
    }













    public class MarkdownEditor {
        private static final Pattern MARKDOWN_PATTERN = Pattern.compile(
                "(?<heading>^#{1,6}\\h.*)|" +  // Markdown headings
                        "(?<bold>\\*\\*(.*?)\\*\\*)|" + // Bold text
                        "(?<italic>\\*(.*?)\\*)"       // Italic text
        );

        public static void enableMarkdownEditing(CodeArea codeArea) {
            codeArea.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> {
                String text = codeArea.getText();
                codeArea.setStyleSpans(0, computeHighlighting(text));
            });

            codeArea.setParagraphGraphicFactory(org.fxmisc.richtext.LineNumberFactory.get(codeArea));
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        }

        private static org.fxmisc.richtext.model.StyleSpans<Collection<String>> computeHighlighting(String text) {
            Matcher matcher = MARKDOWN_PATTERN.matcher(text);
            int lastKwEnd = 0;
            org.fxmisc.richtext.model.StyleSpansBuilder<Collection<String>> spansBuilder = new org.fxmisc.richtext.model.StyleSpansBuilder<>();
            while (matcher.find()) {
                String styleClass =
                        matcher.group("heading") != null ? "markdown-heading" :
                                matcher.group("bold") != null ? "markdown-bold" :
                                        matcher.group("italic") != null ? "markdown-italic" :
                                                null;
                assert styleClass != null;
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
                spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                lastKwEnd = matcher.end();
            }
            spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
            return spansBuilder.create();
        }
    }


    @FXML
    private void handleSortButtonPourcentage(ActionEvent event) {
        if (rapports != null) {
            Collections.sort(rapports, Comparator.comparingInt(RapportClient::getPourcentage));
            updateListView();
        }
    }

    /*@FXML
    private void handleAddRapport(ActionEvent event) {
        if (validatePourcentage() && validateIsFautif()) {
            // All fields are valid, proceed with adding rapport
            ServiceRapport serviceRapport = new ServiceRapport();

            rapportClient.setPourcentage(Integer.parseInt(pourcentageTextField.getText()));
            rapportClient.setRapport(markdownEditor.getText());
            try {
                serviceRapport.ajouter(rapportClient);
                afficherMessageErreur("Succès", "Le rapport a été ajouté avec succès !");
                try {
                    // Load the FXML file for the affichage rapport page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AffichageRapport.fxml"));
                    Parent affichageRapportParent = fxmlLoader.load();

                    // Get the controller of the affichage rapport page
                    AffichageRapportController controller = fxmlLoader.getController();
                    // Perform any initialization if needed

                    // Get the current stage
                    Stage currentStage = (Stage) userNameLabel.getScene().getWindow();

                    // Switch the scene to the affichage rapport page
                    Scene affichageRapportScene = new Scene(affichageRapportParent, 800, 600); // Adjust size as needed
                    currentStage.setScene(affichageRapportScene);
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                afficherMessageErreur("Erreur", "Échec de l'ajout du rapport !");
            }
        }
    }*/


    /******************************/
    @FXML
    private void handleAddRapport(ActionEvent event) {
        if (validatePourcentage() && validateIsFautif()) {
            // All fields are valid, proceed with adding sinistre
            System.out.println(1);
            ServiceRapport serviceSinistre = new ServiceRapport();
            System.out.println(2);

            RapportClient sinistre = new RapportClient();
            System.out.println(3);

            sinistre.setPourcentage(Integer.parseInt(pourcentageTextField.getText()));
            sinistre.setRapport(markdownEditor.getText());
            sinistre.setClientPrenom(userName);
            sinistre.setClientNom(userName);
            String isFautifString = isFautifComboBox.getValue();
            System.out.println(4);

            if (isFautifString == "Fautif") {
                sinistre.setIsFautif(1);
            }else{
                sinistre.setIsFautif(0);

            }
            sinistre.setClientPhoneNumber((int) Math.random());
            sinistre.setClientEmail(email);
            System.out.println(5);

            try {
                System.out.println("try");
                serviceSinistre.ajouter(sinistre);
                afficherMessageSuccess("Success", "Le Rapport a été ajouté avec succès !");
                try {
                    // Load the FXML file for the affichage sinistre page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageRapport.fxml"));
                    Parent affichageSinistreParent = fxmlLoader.load();

                    // Get the controller of the affichage sinistre page
                    AffichageRapportController controller = fxmlLoader.getController();
                    // Perform any initialization if needed

                    // Get the current stage
                    Stage currentStage = (Stage) expertIdComboBox.getScene().getWindow();

                    // Switch the scene to the affichage sinistre page
                    Scene affichageSinistreScene = new Scene(affichageSinistreParent, 800, 600); // Adjust size as needed
                    currentStage.setScene(affichageSinistreScene);
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                afficherMessageErreur("Erreur", "Échec de l'ajout du rapport !");
            }
        }
    }

    /******************************/




    private boolean validateIsFautif() {
        if (isFautifComboBox.getValue() == null) {
            setInvalid(fautiferrorLabel, "Veuillez sélectionner si vous êtes fautif.");
            return false;
        } else {
            setValid(fautiferrorLabel);
            return true;
        }
    }

    private boolean validatePourcentage() {
        String pourcentageText = pourcentageTextField.getText().trim();
        if (pourcentageText.isEmpty()) {
            setInvalid(PourcentageerrorLabel, "Veuillez saisir un pourcentage.");
            return false;
        } else {
            try {
                int pourcentage = Integer.parseInt(pourcentageText);
                if (pourcentage < 0 || pourcentage > 100) {
                    setInvalid(PourcentageerrorLabel, "Le pourcentage doit être compris entre 0 et 100.");
                    return false;
                } else {
                    setValid(PourcentageerrorLabel);
                    return true;
                }
            } catch (NumberFormatException e) {
                setInvalid(PourcentageerrorLabel, "Veuillez saisir un nombre valide pour le pourcentage.");
                return false;
            }
        }
    }
    private void setInvalid(Label errorLabel, String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    private void setValid(Label errorLabel) {
        errorLabel.setText(" "); // Clear error message
        errorLabel.setTextFill(Color.BLACK); // Reset text color
        errorLabel.setVisible(false);

    }
    private void updateListView() {
        rapportObservableList.setAll(rapports);
    }

    private void afficherMessageErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }private void afficherMessageSuccess(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }






}
