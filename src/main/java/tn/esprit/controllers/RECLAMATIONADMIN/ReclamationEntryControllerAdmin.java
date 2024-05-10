package tn.esprit.controllers.RECLAMATIONADMIN;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import tn.esprit.entities.reclamation_entry;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.services.ReclamationEntryService;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReclamationEntryControllerAdmin {
    @FXML
    private ComboBox modeComboBox;
    @FXML
    private ListView<reclamation_entry> listView;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @FXML
    private Label validationLabel;
    @FXML
    private void sendPrompt() {
        String prompt = inputField.getText();
        if (prompt != null && !prompt.isEmpty()) {
            // Implement this method to send the prompt
            // For example, you might create a new reclamation_entry and add it to the ListView
            reclamation_entry entry = new reclamation_entry();
            entry.setPrompt(prompt);
            listView.getItems().add(entry);

            // Clear the input field
            inputField.clear();
        }
    }

    public void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public reclamation_groupe entryReclamationGroupe;

    private ReclamationEntryService service;

    public ReclamationEntryControllerAdmin() {
        this.service = new ReclamationEntryService();
    }

    public void initialize() {
        Scene scene = listView.getScene();
        if (scene != null) {
            listView.maxWidthProperty().bind(scene.widthProperty());
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }
        modeComboBox.setValue("AI");
    }
 //C:\Users\farou\IdeaProjects\javafxreclamation\Roboto-Bold.ttf
    void PDF() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (.pdf)", ".pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName("ReponsesList.pdf");
        File file = fileChooser.showSaveDialog(null); // Remplacez 'null' par votre stage si disponible

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                PDFont font = PDType0Font.load(document, new File("C:/Users/farou/IdeaProjects/javafxreclamation/Roboto-Bold.ttf"));
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Liste des réponses");
                contentStream.endText();

                // Contenu
                ResultSet rs = service.GetAll(entryReclamationGroupe);
                try {
                    while (rs.next()) {
                        //prompt i want the prompt to be bold and the response to be normal and under it
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, 700);
                        contentStream.showText("Prompt: " + rs.getString("prompt"));
                        contentStream.endText();

                        String[] lines = rs.getString("response").split("\n");

                        List<String> modifiedLines = new ArrayList<>();

                        for (String line : lines) {
                            if (line.length() > 100) {
                                // Split the line into chunks of maximum 60 characters each
                                int length = line.length();
                                for (int i = 0; i < length; i += 100) {
                                    int end = Math.min(length, i + 100);
                                    modifiedLines.add(line.substring(i, end));
                                }
                            } else {
                                modifiedLines.add(line);
                            }
                        }

// Convert the modifiedLines list back to an array
                        lines = modifiedLines.toArray(new String[0]);




                        // Draw each line separately
                        float yOffset = 680; // Initial Y offset
                        for (String line : lines) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(100, yOffset);
                            contentStream.showText(line);
                            contentStream.endText();

                            // Update the Y offset for the next line
                            yOffset -= 20; // You can adjust this value based on your font size and line spacing
                        }


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                contentStream.close();
                document.save(file);
                System.out.println("PDF saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setReclamationGroupe(reclamation_groupe item) {
        // Get all reclamation entries for the given reclamation group
        ResultSet rs = service.GetAll(item);

        // set the reclamation group
        entryReclamationGroupe = item;

        try {
            while (rs.next()) {
                // Create a new reclamation_entry object for each row in the ResultSet
                reclamation_entry entry = new reclamation_entry();
                entry.setId(rs.getInt("id"));
                entry.setPrompt(rs.getString("prompt"));
                entry.setDay_time(rs.getString("day_time"));
                entry.setResponse(rs.getString("response"));
                entry.setStatus(rs.getString("status"));
                entry.setResponseType(rs.getString("response_type"));

                // Add the reclamation_entry object to the ListView
                listView.getItems().add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the cell factory for the ListView
        listView.setCellFactory(param -> new ListCell<reclamation_entry>() {
            @Override
            protected void updateItem(reclamation_entry item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create labels for the prompt, response, date, status, and status type
                    Label promptLabel = new Label(item.getPrompt());
                    promptLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                    Label responseLabel = new Label(item.getResponse());
                    responseLabel.setStyle("-fx-font-size: 14px;");

                    Label dateLabel = new Label(item.getDay_time());
                    Label statusLabel = new Label(item.getStatus());
                    Label statusTypeLabel = new Label(item.getResponseType());

                    // Create buttons for the update and delete operations
                    // add the PDF button
                    Button pdfButton = new Button("PDF");
                    pdfButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    pdfButton.setOnAction(event -> PDF());

                    Button updateButton = new Button("Update");
                    updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Add styling to the update button
                    updateButton.setOnAction(event -> {
                        try {
                            updateReclamationEntry(item, entryReclamationGroupe);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;"); // Add styling to the delete button
                    deleteButton.setOnAction(event -> deleteReclamationEntry(item));

                    // Convert Markdown to HTML
                    Parser parser = Parser.builder().build();
                    HtmlRenderer renderer = HtmlRenderer.builder().build();
                    Document document = parser.parse(item.getResponse());
                    String html = renderer.render(document);




// Add custom styles to the HTML
                    html = "<html><head>"
                            + "<style>"
                            + "@font-face {"
                            + "    font-family: 'arial';"
                            + "}"
                            + "body {"
                            + "    font-family: 'MyFont';"
                            + "    background-color: transparent;"
                            + "}"
                            + "@media (max-width: 600px) {"
                            + "    body {"
                            + "        font-size: 18px;"
                            + "    }"
                            + "}"
                            + "</style>"
                            + "</head><body>"
                            + html
                            + "</body></html>";

// Display HTML in a WebView
                    WebView webView = new WebView();
                    webView.getEngine().loadContent(html);

                    // Create a VBox to hold the labels and buttons
                    VBox vBox = new VBox(promptLabel, webView, dateLabel, statusLabel, statusTypeLabel, updateButton, deleteButton, pdfButton);
                    vBox.setSpacing(10); // Add some space between the elements in the VBox

                    // Create a HBox to hold the VBox and make it look like a card
                    HBox hBox = new HBox(vBox);
                    hBox.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

                    // Make the HBox take up all available horizontal space
                    HBox.setHgrow(hBox, Priority.ALWAYS);

                    // Make the ListView resize horizontally with the VBox
                    HBox.setHgrow(listView, Priority.ALWAYS);

                    setGraphic(hBox);
                }
            }
        });
    }

    private void updateReclamationEntry(reclamation_entry entry, reclamation_groupe groupe) throws IOException {
        // Implement this method to update a reclamation_entry
        // redirect to the reclamation entry form
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-entry-form.fxml"));
        Parent editPageParent = fxmlLoader.load();

        // Get the controller of the edit page
        ReclamationEntryFormController controller = fxmlLoader.getController();
        controller.setReclamationGroupe(entry, groupe);

        // Get the current stage
        Stage currentStage = (Stage) listView.getScene().getWindow();

        // Switch the scene to the edit page
        Scene editPageScene = new Scene(editPageParent, 870, 600);
        currentStage.setScene(editPageScene);
        currentStage.show();


    }

    private void deleteReclamationEntry(reclamation_entry entry) {
        showAlert("Do you want to delete "+ entry.getPrompt(), AlertType.WARNING);
        // Implement this method to delete a reclamation_entry
        service.DeleteReclamationEntry(entry.getId());
        // refresh the list view
        listView.getItems().clear();
        setReclamationGroupe(entryReclamationGroupe);
    }

    public void cancelAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation-groupe.fxml"));
        Parent editPageParent = fxmlLoader.load();

        // Get the controller of the edit page
        ReclamationGroupeControllerAdmin controller = fxmlLoader.getController();

        // Get the current stage
        Stage currentStage = (Stage) listView.getScene().getWindow();

        // Switch the scene to the edit page
        Scene editPageScene = new Scene(editPageParent, 870, 600);
        currentStage.setScene(editPageScene);
        currentStage.show();
    }

    // add action for send button
    public void sendPrompt(ActionEvent actionEvent) {
        String prompt = inputField.getText().trim(); // Trim whitespace from the input
        showAlert("Do you want to process your reclamation?", AlertType.CONFIRMATION);
        if (prompt.isEmpty()) {
            // Display validation message if input is empty
            validationLabel.setText("Prompt cannot be empty");
            return;
        }

        if (prompt.contains("badword")) {
            validationLabel.setText("Cannot use bad words in prompt");
            return;
        }

        // Clear validation message if input is not empty and proceed with sending prompt
        validationLabel.setText(""); // Clear validation message
        String mode = (String) modeComboBox.getValue();
        System.out.println("Mode: " + mode);
        reclamation_entry entry = new reclamation_entry();
        entry.setPrompt(prompt);
        entry.setResponseType(mode);
        entry.setDay_time(java.time.LocalDateTime.now().toString());
        service.addReclamationEntry(entry, entryReclamationGroupe);
        listView.getItems().clear();
        showAlert("Reclamation successfully added !", AlertType.INFORMATION);
        setReclamationGroupe(entryReclamationGroupe);
    }

}