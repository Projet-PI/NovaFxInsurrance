package tn.PiFx.controllers;

import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Text;
import tn.PiFx.entities.User;
import tn.PiFx.services.ServiceUtilisateurs;
import tn.PiFx.utils.DataBase;
import tn.PiFx.utils.GoogleUtil;
import tn.PiFx.utils.PasswordUtil;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
    public static final String AUTH_TOKEN = "c6819259e8bc18e7346a0598d85be896";
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
            System.err.println("Error sending SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Fin Api//

    //Debut api mail//

    private boolean emailExists(String email) throws SQLException{
        cnx = DataBase.getInstance().getConx();
        String query = "SELECT * FROM `user` WHERE email=?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private void sendEmailConfirmation(String recipient, String subject, String body){
        final String senderEmail = "slim.bentanfous@esprit.tn";
        final String senderPassword = "Salamlam2002!";

        String host = "smtp-mail.outlook.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.stattls.enable","true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Confirmation email sent successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }











    @FXML
    public void SeConnecterButtonInsc(javafx.event.ActionEvent  event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Nova - Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                                String subject = "Account confirmed !";
                                String body = String.format("Bonjour%s,\n\nVotre compte a bien été créé.\n\nCordialement,", PRENOM);
                                sendEmailConfirmation(EMAIL, subject, body);
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



    @FXML
    public void GoogleButton(javafx.event.ActionEvent actionEvent) {
        try {
            String url = GoogleUtil.getAuthorizationUrl();
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            TextInputDialog authCodeDialog = new TextInputDialog();
            authCodeDialog.setTitle("Google Sign-In");
            authCodeDialog.setHeaderText("Please paste the authorization code here:");
            Optional<String> authCodeResult = authCodeDialog.showAndWait();

            if (authCodeResult.isPresent()) {
                Userinfo userInfo = GoogleUtil.getUserInfo(authCodeResult.get());

                // Collect additional information using dialogs
                String cin = showInputDialog("Enter CIN", "Please enter your CIN:");
                int phone = Integer.parseInt(showInputDialog("Enter Phone Number", "Please enter your phone number:"));
                String address = showInputDialog("Enter Address", "Please enter your address:");
                String profession = showInputDialog("Enter Profession", "Please enter your profession:");
                String password = showInputDialog("Set Password", "Please set your password:");

                // Use the Twilio API to send a verification code to the phone number
                sendVerificationCode(String.valueOf(phone), generateVerificationCode());



                // Assuming user verification is done, hash the password
                String hashedPassword = PasswordUtil.hashPassword(password);
                User newUser = new User(0, Integer.parseInt(cin), userInfo.getName(), "", userInfo.getEmail(), address, phone, hashedPassword, profession, "[\"ROLE_USER\"]");
                UserS.Add(newUser);
                System.out.println("User added to the database with email: " + userInfo.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to authenticate with Google or process data");
        }
    }


    private String showInputDialog(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(content);
        dialog.setContentText("Value:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }


}


