package tn.esprit.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import tn.esprit.entities.Sinistre;
import tn.esprit.services.ServiceSinistre;
import tn.esprit.services.ServiceUtilisateurs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterSinistreController {

    @FXML
    private TextField pourcentageTextField;

    @FXML
    private ComboBox<String> isFautifComboBox;

    @FXML
    private CodeArea markdownEditor;

    @FXML
    private ComboBox<Integer> clientIdComboBox;

    @FXML
    private ComboBox<Integer> expertIdComboBox;

    @FXML
    private Label ClienterrorLabel;

    @FXML
    private Label ExperterrorLabel;

    @FXML
    private Label PourcentageerrorLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label RapporterrorLabel;

    private ServiceUtilisateurs userService;

    public void initialize() {
        userService = new ServiceUtilisateurs();
        MarkdownEditor.enableMarkdownEditing(markdownEditor);
        applyCustomStyleCombo(clientIdComboBox);
        applyCustomStyleCombo(expertIdComboBox);
        applyCustomStyle(pourcentageTextField);
        clientIdComboBox.setItems(getClientIdsWithRole("[\"ROLE_CLIENT\"]"));
        expertIdComboBox.setItems(  getExpertIdsWithRole("[\"ROLE_EXPERT\"]"));

    }

    private ObservableList<Integer> getClientIdsWithRole(String role) {
        List<Integer> userIds = userService.getUserIdsWithRole("[\"ROLE_CLIENT\"]");
        return FXCollections.observableArrayList(userIds);
    }

    private ObservableList<Integer> getExpertIdsWithRole(String role) {
        List<Integer> userIds = userService.getUserIdsWithRole("[\"ROLE_EXPERT\"]");
        return FXCollections.observableArrayList(userIds);
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

    private void applyCustomStyle(TextField textField) {
        textField.getStyleClass().add("custom-text-field");
    }

    private void applyCustomStyleCombo(ComboBox<Integer> comboBox) {
        comboBox.getStyleClass().add("custom-text-field");
    }

    @FXML
    private void clicKAddSinistre(ActionEvent event) {
        if (validateClientID() && validateExpertID() && validatePourcentage()) {
            // All fields are valid, proceed with adding sinistre
            ServiceSinistre serviceSinistre = new ServiceSinistre();
            Sinistre sinistre = new Sinistre();
            sinistre.setSinistre_client_id(clientIdComboBox.getValue());
            sinistre.setSinistre_expert_id(expertIdComboBox.getValue());
            sinistre.setPourcentage(Integer.parseInt(pourcentageTextField.getText()));
            sinistre.setRapport(markdownEditor.getText());
            try {
                serviceSinistre.ajouter(sinistre);
                showAlert("Succès", "Le sinistre a été ajouté avec succès !");
                try {
                    // Load the FXML file for the affichage sinistre page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageSinistre.fxml"));
                    Parent affichageSinistreParent = fxmlLoader.load();

                    // Get the controller of the affichage sinistre page
                    AffichageSinistreController controller = fxmlLoader.getController();
                    // Perform any initialization if needed

                    // Get the current stage
                    Stage currentStage = (Stage) clientIdComboBox.getScene().getWindow();

                    // Switch the scene to the affichage sinistre page
                    Scene affichageSinistreScene = new Scene(affichageSinistreParent, 800, 600); // Adjust size as needed
                    currentStage.setScene(affichageSinistreScene);
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de l'ajout du sinistre !");
            }
        }
    }

    private boolean validateClientID() {
        if (clientIdComboBox.getValue() == null) {
            setInvalid(ClienterrorLabel, "Veuillez sélectionner un ID Client.");
            return false;
        } else {
            setValid(ClienterrorLabel);
            return true;
        }
    }

    private boolean validateExpertID() {
        if (expertIdComboBox.getValue() == null) {
            setInvalid(ExperterrorLabel, "Veuillez sélectionner un ID Expert.");
            return false;
        } else {
            setValid(ExperterrorLabel);
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
        errorLabel.setText(""); // Clear error message
        errorLabel.setTextFill(Color.BLACK); // Reset text color
        errorLabel.setVisible(false);

    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
