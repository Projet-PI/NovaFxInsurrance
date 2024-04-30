package tn.PiFx.controllers;

import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Optional;
import java.util.Random;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import tn.PiFx.entities.User;
import tn.PiFx.services.ServiceUtilisateurs;
import tn.PiFx.utils.GoogleUtil;
import tn.PiFx.utils.PasswordUtil;

import java.net.URI;
import java.math.BigDecimal;


public class RegistrationController {

    @FXML
    private TextField AdresseInscTf;

    @FXML
    private TextField CinInsTF;

    @FXML
    private TextField ProfessionInscTf;

    @FXML
    private TextField EmailInscTf;

    @FXML
    private TextField MdpInsTf;


    @FXML
    private TextField NomInscTf;

    @FXML
    private TextField NumTelInscTf;

    @FXML
    private TextField PrenomInscTf;

    @FXML
    private Label reginfo;

    private Connection cnx;
    private final ServiceUtilisateurs UserS = new ServiceUtilisateurs();

    // Api SMS Slim
    public static final String ACCOUNT_SID = "AC8e6265824899b900397db47f1bd6c4a1";
    public static final String AUTH_TOKEN = "7aa599dcfbb6c03c948741052eb7801a";
    public static final String TWILIO_PHONE_NUMBER = "+16812026037";
    public String verificationCode;

    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    private void sendVerificationCode(String toPhoneNumber, String code) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String fullPhoneNumber = "+216" + toPhoneNumber;
            Message.creator(
                    new PhoneNumber(fullPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    "Your verification code is: " + code
            ).create();
        } catch (com.twilio.exception.ApiException e) {
            e.printStackTrace();
            // Handle Twilio API exceptions here
        }
    }

    //Fin Api//


    @FXML
    void SeConnecterButtonInsc(ActionEvent event) {

    }


    @FXML
    public void ConfirmerInscButton(javafx.event.ActionEvent actionEvent)throws SQLException {
        try {

        int CIN = Integer.parseInt(CinInsTF.getText());
        String NOM = NomInscTf.getText();
        String PRENOM = PrenomInscTf.getText();
        String EMAIL = EmailInscTf.getText();
        String ADRESSE = AdresseInscTf.getText();
        String PROFESSION = ProfessionInscTf.getText();
        String MDP = MdpInsTf.getText();
        int NUMTEL = Integer.parseInt(NumTelInscTf.getText());
            if (!UserS.isValidEmail(EmailInscTf.getText())) {
                reginfo.setText("Email est invalide");
            } else if (!(UserS.isValidPhoneNumber(Integer.parseInt(NumTelInscTf.getText())))) {
                reginfo.setText("N° Telephone est invalide");
            } else if (UserS.checkUserExists(EMAIL)) {
                reginfo.setText("Email déjà existe");
            } else {
                System.out.println("CIN from TextField: " + CinInsTF.getText());

                this.verificationCode = generateVerificationCode();
                System.out.println("Gernerated Code: " + generateVerificationCode());
                sendVerificationCode(String.valueOf(NUMTEL), this.verificationCode);
                System.out.println("Test to checck");
                boolean isCodeVerified = false;
                while (!isCodeVerified) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Verification Code");
                    dialog.setHeaderText("Entrez le code de vérification envoyé à votre téléphone:");
                    dialog.setContentText("Code:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        String inputCode = result.get();
                        if (inputCode.equals(this.verificationCode)) {
                            isCodeVerified = true;
                            try{
                                if (MDP == null || MDP.isEmpty()){
                                    throw new IllegalArgumentException("Mot de Passe cannot be empty");
                                }
                                String HashedPassword = PasswordUtil.hashPassword(MDP);
                                UserS.Add(new User(0,CIN, NOM, PRENOM, EMAIL, ADRESSE, NUMTEL, HashedPassword, PROFESSION,"[\"ROLE_USER\"]"));
                                System.out.println("user added");

                            }catch (IllegalArgumentException e) {
                                System.out.println("utilisateur ne peut pas etre ajouté");

                            }
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.setTitle("Nova");
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace(); // This will print the full stack trace to the console
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("SQL Exception");
                                alert.setContentText(e.getMessage());
                                alert.showAndWait();
                            }
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setHeaderText("Code est incorrect");
                            errorAlert.setContentText("Le code de vérification que vous avez entré est incorrect. Veuillez réessayer.");
                            errorAlert.showAndWait();
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void SeConnecterButtonInsc(javafx.event.ActionEvent actionEvent) {
    }

    @FXML
    public void GoogleButton(javafx.event.ActionEvent actionEvent) {
        try{
            String url = GoogleUtil.getAuthorizationUrl();
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Google Sign-In");
            dialog.setHeaderText("Please paste the authorization code here:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()){
                Userinfo userInfo = GoogleUtil.getUserInfo(result.get());
                System.out.println("User Info" + userInfo.getName() + ","+ userInfo.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Authorization Erro", "Failed to authenticate with google ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}


