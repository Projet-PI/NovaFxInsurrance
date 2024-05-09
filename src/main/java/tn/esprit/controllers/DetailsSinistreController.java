package tn.esprit.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import tn.esprit.entities.Sinistre;
import tn.esprit.services.ServiceSinistre;
import tn.esprit.services.ServiceUtilisateurs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.valueOf;

public class DetailsSinistreController {

    @FXML
    private ComboBox<Integer> clientIdComboBox;

    @FXML
    private ComboBox<Integer> expertIdComboBox;

    @FXML
    private TextField pourcentageTextField;

    @FXML
    private CodeArea markdownEditor;

    private Sinistre sinistre;
    @FXML
    private ServiceUtilisateurs userService;
    @FXML
    private Label ClienterrorLabel;

    @FXML
    private Label ExperterrorLabel;

    @FXML
    private Label PourcentageerrorLabel;
    @FXML
    private ComboBox isFautifComboBox;

    public void initialize() {
        MarkdownEditor.enableMarkdownEditing(markdownEditor);
        userService = new ServiceUtilisateurs();
        applyCustomStyleCombo(clientIdComboBox);
        applyCustomStyleCombo(expertIdComboBox);
    }
    private void applyCustomStyleCombo(ComboBox Combobox) {
        Combobox.getStyleClass().add("custom-text-field");
    }

    public void initData(Sinistre sinistre) {
        this.sinistre = sinistre;
        clientIdComboBox.setValue(sinistre.getSinistre_client_id());
        expertIdComboBox.setValue(sinistre.getSinistre_expert_id());
        pourcentageTextField.setText(String.valueOf(sinistre.getPourcentage()));
        markdownEditor.replaceText(sinistre.getRapport());
        clientIdComboBox.setItems(getClientIdsWithRole("[\"ROLE_CLIENT\"]"));

        // Populate the ComboBox for ID expert with user IDs having the role "ROLE_EXPERT"
        expertIdComboBox.setItems(getExpertIdsWithRole("[\"ROLE_EXPERT\"]"));
        loadSinistreDataForEdit(sinistre);
    }
    // When loading sinistre data for editing
    public void loadSinistreDataForEdit(Sinistre sinistre) {
        // Set ComboBox value to match current is_fautif value
        isFautifComboBox.setValue(sinistre.getIs_fautif() == 1 ? "Fautif" : "Non Fautif");

        // You may also want to bind the ComboBox value property to the is_fautif property
        // This ensures that any changes in the ComboBox are reflected in the is_fautif property
        isFautifComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Update is_fautif property based on ComboBox value
            sinistre.setIs_fautif(newValue.equals("Fautif") ? 1 : 0);
        });
    }

    private ObservableList<Integer> getClientIdsWithRole(String role) {
        // Call the UserService to get user IDs with the specified role
        List<Integer> userIds = userService.getUserIdsWithRole("[\"ROLE_CLIENT\"]");
        return FXCollections.observableArrayList(userIds);
    }
    private ObservableList<Integer> getExpertIdsWithRole(String role) {
        // Call the UserService to get user IDs with the specified role
        List<Integer> userIds = userService.getUserIdsWithRole("[\"ROLE_EXPERT\"]");
        return FXCollections.observableArrayList(userIds);
    }

    @FXML
    private void handleSaveButton() {
        if (validateClientID() && validateExpertID() && validatePourcentage()) {
            try {
                sinistre.setSinistre_client_id(Integer.parseInt(String.valueOf(clientIdComboBox.getValue())));
                sinistre.setSinistre_expert_id(Integer.parseInt(String.valueOf(expertIdComboBox.getValue())));
                sinistre.setPourcentage(Integer.parseInt(pourcentageTextField.getText()));
                sinistre.setRapport(markdownEditor.getText());

                // Create an instance of ServiceSinistre
                ServiceSinistre serviceSinistre = new ServiceSinistre();

                serviceSinistre.modifier(sinistre);

                afficherAlerte("Succès", "Le sinistre a été modifié avec succès !!");

                try {
                    // Load the FXML file for the affichage sinistre page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AffichageSinistre.fxml"));
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
                afficherAlerte("Erreur", "Échec de la mise à jour du sinistre.");
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

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class MarkdownEditor {
        private static final Pattern MARKDOWN_PATTERN = Pattern.compile(
                "(?<heading>^#{1,6}\\h.*)|" +  // Markdown headings
                        "(?<bold>\\*\\*(.*?)\\*\\*)|" + // Bold text
                        "(?<italic>\\*(.*?)\\*)"       // Italic text
        );

        public static void enableMarkdownEditing(CodeArea codeArea) {
            // Listen for key events to update Markdown syntax highlighting
            codeArea.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                String text = codeArea.getText();
                codeArea.setStyleSpans(0, computeHighlighting(text));
            });

            // Set text formatter to keep track of changes
            codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

            // Apply initial highlighting
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        }

        private static StyleSpans<Collection<String>> computeHighlighting(String text) {
            Matcher matcher = MARKDOWN_PATTERN.matcher(text);
            int lastKwEnd = 0;
            StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
            while (matcher.find()) {
                String styleClass =
                        matcher.group("heading") != null ? "markdown-heading" :
                                matcher.group("bold") != null ? "markdown-bold" :
                                        matcher.group("italic") != null ? "markdown-italic" :
                                                null; // Never happens
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
    private void handleDownloadPDFButton(ActionEvent event) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Sinistre"+sinistre.getId() +".pdf"));
            document.open();
            Font font = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Sinistre Numero: " + sinistre.getId(), font);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Client ID: " + sinistre.getSinistre_client_id()));
            document.add(new Paragraph("Expert ID: " + sinistre.getSinistre_expert_id()));
            document.add(new Paragraph("Is Fautif: " + (sinistre.getIs_fautif() == 1 ? "Fautif" : "Non Fautif")));
            document.add(new Paragraph("Pourcentage: " + sinistre.getPourcentage()));
            document.add(new Paragraph("Rapport: " + sinistre.getRapport()));
            document.close();

            afficherAlerte("Success", "PDF downloaded successfully!");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            afficherAlerte("Error", "Failed to generate PDF.");
        }
    }

    @FXML
    private void handleCancelButton() {
        // Close the window without saving any changes
        // You can use the Stage object to close the window
        // Stage stage = (Stage) clientIdTextField.getScene().getWindow();
        // stage.close();
        try {
            // Load the FXML file for the affichage sinistre page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AffichageSinistre.fxml"));
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
    }
}
