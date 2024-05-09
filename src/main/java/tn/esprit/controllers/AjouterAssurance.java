package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tn.esprit.services.Assurance_s;
import tn.esprit.entities.Assurance;
import tn.esprit.entities.Contrat;
import tn.esprit.utils.DataBase;

public class AjouterAssurance implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nomtf;

    @FXML
    private ComboBox<Contrat> combo;

    @FXML
    private TextField montanttf;

    @FXML
    private DatePicker datedebuttf;

    @FXML
    private DatePicker datefintf;

    @FXML
    private ComboBox<Contrat> comvoid;
    
    Connection conn= DataBase.getInstance().getConx() ;

    @FXML
    void ajouter(ActionEvent event) {
        if (champsValides()) {


            ComboBox<Contrat> combo = (ComboBox<Contrat>) ((Button) event.getSource()).getScene().lookup("#combo");
            Contrat selectedContrat = combo.getValue();
            System.out.println(selectedContrat.getId());
            if (selectedContrat != null) {
                Assurance assurance = new Assurance(
                        nomtf.getText(),
                        Float.parseFloat(montanttf.getText()),
                        datedebuttf.getValue().toString(),
                        datefintf.getValue().toString(),
                        selectedContrat.getId()

                );

                Assurance_s assurance_s = new Assurance_s();
                try {
                    assurance_s.add(assurance);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Assurance ajoutée avec succès");
                    alert.show();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(e.getMessage());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez sélectionner un contrat.");
                alert.show();
            }
        }
    }
    private boolean champsValides() {
        String errorMessage = "";

        if (nomtf.getText() == null || nomtf.getText().isEmpty()) {
            errorMessage += "Le nom de l'assurance ne peut pas être vide.\n";
        }
        if (montanttf.getText() == null || montanttf.getText().isEmpty()) {
            errorMessage += "Le montant de l'assurance ne peut pas être vide.\n";
        } else {
            try {
                float montant = Float.parseFloat(montanttf.getText());
                if (montant <= 0) {
                    errorMessage += "Le montant de l'assurance doit être positif.\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Le montant de l'assurance doit être un nombre valide.\n";
            }
        }
        if (datedebuttf.getValue() == null) {
            errorMessage += "La date de début ne peut pas être vide.\n";
        }
        if (datefintf.getValue() == null) {
            errorMessage += "La date de fin ne peut pas être vide.\n";
        }
        if (datedebuttf.getValue() != null && datefintf.getValue() != null) {
            if (datedebuttf.getValue().isAfter(datefintf.getValue())) {
                errorMessage += "La date de début doit être antérieure à la date de fin.\n";
            }
        }
        if (datedebuttf.getValue() != null && datedebuttf.getValue().isBefore(LocalDate.now())) {
            errorMessage += "La date de début doit être postérieure à la date actuelle.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBoxes
//       initializeComboBoxes();
        initializeComboBoxes() ;


    }


    public List<Contrat> getContrats() throws SQLException {
        List<Contrat> contrats = new ArrayList<>();

        // SQL query to select all ingredients
        String query = "SELECT * FROM contrat";


        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            // Iterate over the result set and create Ingredient objects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int duree = resultSet.getInt("duree");
                String date_de_souscription = resultSet.getString("date_de_souscription");
                String type_couverture = resultSet.getString("type_couverture");
                // Add the ingredient to the list
                System.out.println("id: " + id + ", duree: " + duree + ", date_de_souscription: " + date_de_souscription + ", type_couverture: " + type_couverture);

                contrats.add(new Contrat(id, duree, date_de_souscription , type_couverture));
            }
        }

        return contrats;
    }
    private void initializeComboBoxes() {
        // Initialize combo box with contracts
        try {

            List<Contrat> contrats = getContrats();
            ObservableList<Contrat> contratsObservableList = FXCollections.observableArrayList(contrats);

            combo.setItems(contratsObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
