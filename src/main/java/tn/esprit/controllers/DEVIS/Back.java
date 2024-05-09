package tn.esprit.controllers.DEVIS;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
import tn.esprit.entities.Devis;
import tn.esprit.services.ServiceR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Back implements Initializable {
    @FXML
    private TableView<Devis> tab;
    @FXML
    private Button avis;
    @FXML
    private Button BackAvis;
    @FXML
    private Button Ajouter_devis;



    @FXML
    private TableColumn<Devis, String> prenom;

    @FXML
    private TableColumn<Devis, String> emailR;

    @FXML
    private TableColumn<Devis, String> imageR;

    @FXML
    private TableColumn<Devis, String> nomR;

    @FXML
    private TableColumn<Devis, Integer> phone;

    @FXML
    TextField TFnom, TFadresse, TFemail, TFphone, TFimage;

    @FXML
    Button modif, supprimer, pdf;

    @FXML
    //private Button avis;

    int selectedId;


    @FXML
    private Button Gotodash;

    @FXML
    private Button Stat;
    ServiceR s = new ServiceR();
    public Devis r = new Devis();

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

            List<Devis> restaurants = s.getAll();

            // Créer le tableau
            PdfPTable table = new PdfPTable(3); // 3 colonnes pour Nom, Prenom, Email
            table.setWidthPercentage(100);

            // Ajouter l'en-tête du tableau
            PdfPCell headerCell;
            headerCell = new PdfPCell(new Phrase("Nom"));
            headerCell.setBackgroundColor(BaseColor.GREEN); // Couleur verte pour l'en-tête
            table.addCell(headerCell);
            headerCell = new PdfPCell(new Phrase("Prenom"));
            headerCell.setBackgroundColor(BaseColor.GREEN); // Couleur verte pour l'en-tête
            table.addCell(headerCell);
            headerCell = new PdfPCell(new Phrase("Email"));
            headerCell.setBackgroundColor(BaseColor.GREEN); // Couleur verte pour l'en-tête
            table.addCell(headerCell);

            // Ajouter les informations des Devis dans le tableau
            for (Devis restaurant : restaurants) {
                PdfPCell cell;
                cell = new PdfPCell(new Phrase(restaurant.getName()));
                cell.setBackgroundColor(BaseColor.WHITE); // Couleur blanche pour le contenu
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(restaurant.getPrenom()));
                cell.setBackgroundColor(BaseColor.WHITE); // Couleur blanche pour le contenu
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(restaurant.getEmail()));
                cell.setBackgroundColor(BaseColor.WHITE); // Couleur blanche pour le contenu
                table.addCell(cell);
            }

            // Ajouter le tableau au document
            document.add(table);

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

        nomR.setCellValueFactory(new PropertyValueFactory<Devis, String>("name"));
        prenom.setCellValueFactory(new PropertyValueFactory<Devis, String>("prenom"));
        emailR.setCellValueFactory(new PropertyValueFactory<Devis, String>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<Devis, Integer>("id"));

        loadTableData();

        tab.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Devis selectedRestaurant = tab.getSelectionModel().getSelectedItem();
                selectedId = selectedRestaurant.getId();
                TFnom.setText(selectedRestaurant.getName());
                TFadresse.setText(selectedRestaurant.getPrenom());
                TFemail.setText(selectedRestaurant.getEmail());
                TFphone.setText(String.valueOf(selectedRestaurant.getId()));

            }
        });

        modif.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Devis devis = new Devis();
                devis.setId(selectedId);
                devis.setName(TFnom.getText());
               devis.setPrenom((TFadresse.getText()));
                devis.setEmailR(TFemail.getText());
                s.modifier(devis);
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
        ObservableList<Devis> data = FXCollections.observableArrayList(s.getAll());
        tab.setItems(data);
    }

    public void updateTable_r() {
        List<Devis>devis = s.getAll();
        tab.getItems().setAll(devis);
    }

    private void clearFields() {
        TFphone.clear();
        TFemail.clear();
        TFnom.clear();
        TFadresse.clear();
        TFimage.clear();
    }

    public void Ajouter_devis(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Ajouter_devis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Stat(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chart.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Stat.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void BackAvis(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backavis.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) BackAvis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }public void Gotodash(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminuser.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Gotodash.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}