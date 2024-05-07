package tn.esprit.controles;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.Service.Contrat_s;
import tn.esprit.entity.Contrat;

public class ModifierContrat {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField dureeField;

    @FXML
    private DatePicker datefield;

    @FXML
    private ComboBox<String> typefield;

    private Contrat contrat;
    private Contrat_s contrat_s;

    @FXML
    void modifierContrat(ActionEvent event) {
        // Mettre à jour les détails de l'emplacement avec les valeurs des champs de texte
        contrat.setDuree(Integer.parseInt(dureeField.getText()));
        contrat.setDate_de_souscription(datefield.getValue().toString());
        contrat.setType_couverture(typefield.getValue());

        // Appeler la méthode de service pour mettre à jour le contrat dans la base de données
        try {
            contrat_s.update(contrat);
            // Fermer la fenêtre de modification
            ((Stage) dureeField.getScene().getWindow()).close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon la stratégie de gestion des erreurs de votre application
        }
    }

    @FXML
    void initialize() {
        typefield.getItems().addAll("gold", "silver", "bronze");
        contrat_s = new Contrat_s();
    }

    public void initData(Contrat contrat) {
        this.contrat = contrat;
        dureeField.setText(String.valueOf(contrat.getDuree()));
        datefield.setValue(LocalDate.parse(contrat.getDate_de_souscription())); // Assuming date_c is a String in the format "yyyy-MM-dd"
        typefield.setValue(contrat.getType_couverture());
    }
}
