package tn.esprit.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.media.MediaView;
import tn.esprit.entities.RapportClient;
import tn.esprit.services.ServiceRapport;
import tn.esprit.services.ServiceUtilisateurs;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsRapportController {

    @FXML
    private ComboBox<Integer> clientIdComboBox;

    @FXML
    private ComboBox<String> isFautifComboBox;

    @FXML
    private TextField pourcentageTextField;

    @FXML
    private CodeArea markdownEditor;

    private RapportClient rapportClient;
    @FXML
    private ServiceUtilisateurs userService;
    @FXML
    private Label PourcentageerrorLabel;

    @FXML
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private final List<String> paths = new ArrayList<>();
    private final SimpleObjectProperty<MediaPlayer> mediaPlayerProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        //markdownEditor = new CodeArea();
        MarkdownEditor.enableMarkdownEditing(markdownEditor);
        userService = new ServiceUtilisateurs();
        paths.add("/images/Caraccident.mp4");
        paths.add("/images/Untitled.mp4");
        paths.add("/images/POVCRASH.mp4");
        paths.add("/images/CarCrash240.mp4");

        String randomPath = getRandomPath();
        playMedia(randomPath);

}

    private String getRandomPath() {
        Random random = new Random();
        int index = random.nextInt(paths.size());
        return paths.get(index);
    }

    @FXML
    private void playMedia(String mediaFile) {
        Media media = new Media(getClass().getResource(mediaFile).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        });

        mediaPlayerProperty.set(mediaPlayer);
        mediaView.setMediaPlayer(mediaPlayer);
    }
    public void cleanup() {
        MediaPlayer mediaPlayer = mediaPlayerProperty.get();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void initData(RapportClient rapportClient) {

        this.rapportClient = rapportClient;
        loadRapportDataForEdit(rapportClient);



    }

    @FXML
    public void loadRapportDataForEdit(RapportClient rapportClient) {
        // Set ComboBox value to match current is_fautif value
        isFautifComboBox.setValue(rapportClient.getisFautif() == 1 ? "Fautif" : "Non Fautif");
        pourcentageTextField.setText(String.valueOf(rapportClient.getPourcentage()));

        // You may also want to bind the ComboBox value property to the is_fautif property
        // This ensures that any changes in the ComboBox are reflected in the is_fautif property
        isFautifComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Update is_fautif property based on ComboBox value
            rapportClient.setIsFautif(newValue.equals("Fautif") ? 1 : 0);
        });
        markdownEditor.replaceText(rapportClient.getRapport());
    }

    @FXML
    private void handleSaveButton() {
        if (validateClientPourcentage()) {
            try {
                if (rapportClient != null) {
                    rapportClient.setPourcentage(Integer.parseInt(pourcentageTextField.getText()));
                    rapportClient.setRapport(markdownEditor.getText());

                    // Set isFautif based on the selected value in the ComboBox
                    String isFautifString = isFautifComboBox.getValue();
                    System.out.println(isFautifString + " précédent");

                    // Create an instance of ServiceRapport

                    ServiceRapport serviceRapport = new ServiceRapport();
                    System.out.println("créer instance");

                    serviceRapport.modifier(rapportClient);
                    System.out.println(isFautifString + " new");

                    afficherAlerte("Succès", "Le Rapport a été modifié avec succès !!");

                    try {
                        // Load the FXML file for the affichage Rapport page
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageRapport.fxml"));
                        Parent affichageRapportParent = fxmlLoader.load();
                        AffichageRapportController controller = fxmlLoader.getController();
                        // Perform any initialization if needed

                        // Get the current stage
                        Stage currentStage = (Stage) pourcentageTextField.getScene().getWindow();

                        // Switch the scene to the affichage Rapport page
                        Scene affichageRapportScene = new Scene(affichageRapportParent, 1000, 600); // Adjust size as needed
                        currentStage.setScene(affichageRapportScene);
                        currentStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                afficherAlerte("Erreur", "Échec de la mise à jour du Rapport.");
            }
        } else {
            afficherAlerte("erreur", "user null!");
        }
    }






    private boolean validateClientPourcentage() {
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
            PdfWriter.getInstance(document, new FileOutputStream("Rapport"+ rapportClient.getRapportClientId() +".pdf"));
            document.open();
            Font font = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Rapport Numero: " + rapportClient.getRapportClientId(), font);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Is Fautif: " + (rapportClient.getisFautif()==1 ? "Fautif" : "Non Fautif")));
            document.add(new Paragraph("Pourcentage: " + rapportClient.getPourcentage()));
            document.add(new Paragraph("Rapport: " + rapportClient.getRapport()));
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
            // Load the FXML file for the affichage Rapport page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AffichageRapport.fxml"));
            Parent affichageRapportParent = fxmlLoader.load();

            // Get the controller of the affichage Rapport page
            AffichageRapportController controller = fxmlLoader.getController();
            // Perform any initialization if needed

            // Get the current stage
            Stage currentStage = (Stage) pourcentageTextField.getScene().getWindow();

            // Switch the scene to the affichage Rapport page
            Scene affichageRapportScene = new Scene(affichageRapportParent, 1000, 600); // Adjust size as needed
            currentStage.setScene(affichageRapportScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
