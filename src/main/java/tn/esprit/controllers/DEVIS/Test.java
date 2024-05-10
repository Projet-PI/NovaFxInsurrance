package tn.esprit.controllers.DEVIS;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.AvisRestau;
import tn.esprit.services.ServiceDevis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Test implements Initializable {
    @FXML
    private TableView<AvisRestau> tab;

    @FXML
    private TableColumn<AvisRestau, String> prenom;

    @FXML
    private TableColumn<AvisRestau, String> emailR;




    @FXML
    private TableColumn<AvisRestau, Integer> phone;

    @FXML
    TextField TFnom, TFadresse, TFemail, TFphone, TFimage;

    @FXML
    Button modif, supprimer, pdf;

    @FXML
    private Button avis;

    int selectedId;

    ServiceDevis s = new ServiceDevis();
    public AvisRestau r = new AvisRestau();

    public void generatePdf(String filePath) {
        try {
            File file = new File(filePath);
            File parentDirectory = file.getParentFile();
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs(); // Create the directory if it doesn't exist
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            List<AvisRestau> response_devis = s.getAll();
            document.add(new Paragraph("Devis Information\n\n"));
            for (AvisRestau response : response_devis) {
                String restaurantInfo = String.format("Name: %s\nPrenom: %s\nEmail: \n\n",
                        response.getId(), response.getStatus(), response.getResponse());
                document.add(new Paragraph(restaurantInfo));
            }

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void AvisButton() {
        try {
            // Charger le fichier FXML de la nouvelle page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backavis.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) avis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions en conséquence
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        prenom.setCellValueFactory(new PropertyValueFactory<AvisRestau, String>("response"));
        emailR.setCellValueFactory(new PropertyValueFactory<AvisRestau, String>("status"));
        phone.setCellValueFactory(new PropertyValueFactory<AvisRestau, Integer>("id"));

        loadTableData();

        tab.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                AvisRestau selectedRestaurant = tab.getSelectionModel().getSelectedItem();
                selectedId = selectedRestaurant.getId();
                TFnom.setText(selectedRestaurant.getResponse());
                TFadresse.setText(selectedRestaurant.getStatus());
                TFphone.setText(String.valueOf(selectedRestaurant.getId()));

            }
        });

        modif.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AvisRestau r = new AvisRestau();
                r.setId(selectedId);

                r.setStatus(TFnom.getText());
                r.setResponse((TFadresse.getText()));
                //AvisRestau.setEmailR(TFemail.getText());
                s.modifier(r);
                updateTable_r();
                clearFields();
            }
        });

        supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                s.supprimer(selectedId);
                updateTable_r();
                clearFields();
            }
        });

        pdf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    generatePdf(file.getAbsolutePath());
                }
            }
        });


        clearFields();
    }

    private void loadTableData() {
        ObservableList<AvisRestau> data = FXCollections.observableArrayList(s.getAll());
        tab.setItems(data);
    }

   public void updateTable_r() {
        List<AvisRestau>data = s.getAll();
        tab.getItems().setAll(data);
    }

    private void clearFields() {
        TFphone.clear();
        TFemail.clear();
        TFnom.clear();
        TFadresse.clear();
        TFimage.clear();
    }
}