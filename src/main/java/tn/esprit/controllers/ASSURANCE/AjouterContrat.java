package tn.esprit.controllers.ASSURANCE;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Contrat;
import tn.esprit.utils.DataBase;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

public class AjouterContrat {

    @FXML
    private TextField dureetf;

    @FXML
    private DatePicker datesouscriptointf;

    @FXML
    private ComboBox<String> combo;

    @FXML
    private TextField PhoneNumber;
    private Contrat_s contratService;
    private Connection conn;
    @FXML
    private ImageView generateCap;

    @FXML
    private TextField tfCaptcha;

    @FXML
    private Label captchaLabel;

    @FXML
    private Label checkrecaptcha;
    @FXML
    private Button button;

    @FXML
    void generateCaptcha(MouseEvent event) {
        if(event.getSource()==generateCap)
        {generateCaptcha();}


    }
    private void generateCaptcha() {

        String captcha = generateRandomString(6);
        captchaLabel.setText(captcha);
    }
    private String generateRandomString(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }
    public AjouterContrat() {
        contratService = new Contrat_s();
        conn = DataBase.getInstance().getConx();
    }

    @FXML
    void initialize() {
        combo.getItems().addAll("gold", "silver", "bronze");
        generateCaptcha();
    }

    @FXML
    void ajouter(ActionEvent event) {
        String duree = dureetf.getText();
        String dateSouscription = datesouscriptointf.getEditor().getText();
        String selectedType = combo.getValue();

        if (duree.isEmpty() || dateSouscription.isEmpty() || selectedType == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if(!tfCaptcha.getText().equals(captchaLabel.getText()))
        {
            checkrecaptcha.setVisible(true);
            checkrecaptcha.setText("incorrect code try again!");
            checkrecaptcha.setStyle("-fx-text-fill: red;");
        }
        else {
            checkrecaptcha.setVisible(false);
        }

        Contrat contrat = new Contrat();
        contrat.setDuree(Integer.parseInt(duree));
        contrat.setDate_de_souscription(dateSouscription);
        contrat.setType_couverture(selectedType);

        try {
            // Assuming PhoneNumber is a TextField defined in your FXML file and properly initialized
            String numTel = PhoneNumber.getText(); // Get the phone number from the TextField
            contratService.add(contrat, conn); // Pass the connection object to the service method
            this.verificationCode = generateVerificationCode();
            System.out.println("Generated Code: " + generateVerificationCode());

            // Send the verification code to the phone number obtained from the TextField
            sendVerificationCode(numTel, this.verificationCode);
            System.out.println("test to check");

            boolean isCodeVerified = false;
            while (!isCodeVerified){
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("verification code");
                dialog.setHeaderText("entrez le code de verification");
                dialog.setContentText("code");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    String inputCode = result.get();
                    if (inputCode.equals(this.verificationCode)){
                        isCodeVerified = true;
                    }
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le contrat a été ajouté avec succès.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du contrat : " + e.getMessage());
        }
    }


    public static final String ACCOUNT_SID = "AC116a6135b877fc1f4c5ca39fe83e9438";
    public static final String AUTH_TOKEN = "48dbb7f1f98c75e23bac800b1e0f2eb8";
    public static final String TWILIO_PHONE_NUMBER = "+15163505695";
    public String verificationCode;
    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    public void sendVerificationCode(String toPhoneNumber, String code) {
        try {
            String NumeroTel = PhoneNumber.getText();
            System.out.println("number retrieved " + NumeroTel);

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String fullPhoneNumber = "+216" + NumeroTel;
            Message.creator(
                    new PhoneNumber(fullPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    "Your verification code is: " + code
            ).create();
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        dureetf.clear();
        datesouscriptointf.getEditor().clear();
        combo.getSelectionModel().clearSelection();
    }

    public void goToAfficherFrontContrat(ActionEvent actionEvent) {
        try {
            // Load the FXML file for AfficherFrontcontrat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/AfficherFrontcontrat.fxml"));
            Parent root = loader.load();

            // Get the stage of the current scene
            Stage stage = (Stage) button.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}