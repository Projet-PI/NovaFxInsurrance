package tn.esprit.controles;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.Service.Assurance_s;
import tn.esprit.Service.Contrat_s;
import tn.esprit.entity.Assurance;
import tn.esprit.entity.Contrat;

public class ModifierAssurance {

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<String> contratidComboBox;
    @FXML
    private URL location;

    @FXML
    private TextField nomfiled;

    @FXML
    private TextField montantfield;

    @FXML
    private DatePicker datedfield;

    @FXML
    private DatePicker dateffield;

    // Assuming you have a TableView for displaying assurances
    @FXML
    private TableView<Assurance> tableau;

    private Assurance_s assurance_s;
    private Assurance assurance; // Assuming you have a variable to hold the selected assurance

    private List<Contrat> contrats;

    @FXML
    void initialize() throws SQLException {
        // Initialize UI components and service
        assurance_s = new Assurance_s();
        populateContratComboBox(); // Initialize the list of contracts
        // Initialize the list of contracts here
        Contrat_s contratService = new Contrat_s();
        contrats = contratService.show();
    }
    private void populateContratComboBox() throws SQLException {
        Contrat_s contratService = new Contrat_s();
        List<Contrat> contrats = contratService.show();

        contratidComboBox.setItems(FXCollections.observableArrayList(contrats.stream()
                .map(Contrat::getType_couverture)
                .collect(Collectors.toList())));
    }

    public void initData(Assurance assurance) {
        this.assurance = assurance;
        nomfiled.setText(assurance.getNom());
        montantfield.setText(String.valueOf(assurance.getMontant()));
        datedfield.setValue(LocalDate.parse(assurance.getDate_debut()));
        dateffield.setValue(LocalDate.parse(assurance.getDate_fin()));

        // Trouver le contrat correspondant à l'ID de contrat de l'assurance
        Contrat contrat = contrats.stream()
                .filter(c -> c.getId() == assurance.getContrat_id())
                .findFirst()
                .orElse(null);

        // Définir la valeur de la ComboBox sur le type de couverture du contrat
        if (contrat != null) {
            contratidComboBox.setValue(contrat.getType_couverture());
        } else {
            contratidComboBox.setValue(null);
        }
    }

    private boolean validateFields() {
        if (nomfiled.getText().isEmpty() || datedfield.getValue() == null ||
                dateffield.getValue() == null || montantfield.getText().isEmpty() ||
                contratidComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return false;
        }

        try {
            Float.parseFloat(montantfield.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La durée doit être un nombre .");
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate selectedDate = datedfield.getValue();
        if (selectedDate.isBefore(currentDate)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date doit être ultérieure à la date actuelle.");
            return false;
        }
        LocalDate selectedDate1 = dateffield.getValue();
        if (selectedDate1.isBefore(currentDate)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date doit être ultérieure à la date actuelle.");
            return false;
        }

        return true;
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void modifierAssurance(ActionEvent actionEvent) {
        if (!validateFields()) {
            return;
        }

        assurance.setNom(nomfiled.getText());
        assurance.setMontant(Float.parseFloat(montantfield.getText()));
        assurance.setDate_debut(datedfield.getValue().toString());
        assurance.setDate_fin(dateffield.getValue().toString());

        // Trouver le contrat correspondant au type de couverture sélectionné
        Contrat selectedContrat = contrats.stream()
                .filter(c -> c.getType_couverture().equals(contratidComboBox.getValue()))
                .findFirst()
                .orElse(null);

        if (selectedContrat == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contrat sélectionné est introuvable.");
            return;
        }

        assurance.setContrat_id(selectedContrat.getId());

        try {
            assurance_s.edit(assurance);
            nomfiled.getScene().getWindow().hide();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la modification de l'assurance.");
        }
    }
}

