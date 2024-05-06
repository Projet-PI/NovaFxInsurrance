    package tn.esprit.controllers;
    //--Imports--//

    import javafx.collections.FXCollections;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.geometry.Insets;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.Pane;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import org.apache.poi.ss.usermodel.Cell;
    import org.apache.poi.ss.usermodel.Row;
    import org.apache.poi.ss.usermodel.Sheet;
    import org.apache.poi.ss.usermodel.Workbook;
    import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    import org.w3c.dom.Text;
    import tn.esprit.entities.User;
    import tn.esprit.services.ServiceUtilisateurs;
    import tn.esprit.utils.DataBase;
    import tn.esprit.utils.SessionManager;

    import javax.mail.*;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeMessage;
    import java.awt.event.KeyEvent;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.net.URL;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Properties;
    import java.util.ResourceBundle;
    //import javafx.scene.input.KeyEvent;
    //--Imports End--//

    public class AdminUserController implements Initializable  {

        @FXML
        public TextField SearchBar;
        @FXML
        private ResourceBundle resources;
        @FXML
        private URL location;
        @FXML
        private TextField adresseTF;
        @FXML
        public TextField cinTF;
        @FXML
        public TextField emailTF;
        @FXML
        public TextField mdpTF;
        @FXML
        public TextField nomTF;
        @FXML
        public TextField numtelTF;
        @FXML
        public TextField prenomTF;
        @FXML
        public TextField professionTF;
        @FXML
        public ComboBox<String> roleCOMBOBOX;
        @FXML
        private TextField uinfolabel;
        @FXML
        public GridPane userContainer;
        @FXML
        private Text errorNom;
        @FXML
        private Text errorAdresse;
        @FXML
        private Text errorEmail;
        @FXML
        private Text errorPhone;
        @FXML
        private Label reginfo;


        private Connection conx;
        private final ServiceUtilisateurs UserS = new ServiceUtilisateurs();

        //Initialize Function
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            roleCOMBOBOX.setItems(FXCollections.observableArrayList("[\"ROLE_USER\"]", "[\"ROLE_ADMIN\"]"));
            load();
        }

        //Load Function
        public void load() {
            int column = 0;
            int row = 1;
            try {
                userContainer.getChildren().clear();
                for (User user : UserS.afficher()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/CardViewUser.fxml"));
                    Pane userBox = fxmlLoader.load();
                    CardviewUserController cardController = fxmlLoader.getController();
                    cardController.setData(user);
                    userBox.setUserData(user.getId());
                    cardController.setAdminUserController(this);
                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                    userContainer.add(userBox, column++, row);
                    GridPane.setMargin(userBox, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //PopulateForm Function
        public void populateEditForm(User user) {
            if (user == null) {
                showAlert("Error", "User data is not available.", Alert.AlertType.ERROR);
                return;
            }
            cinTF.setText(String.valueOf(user.getCin()));
            nomTF.setText(user.getNom());
            prenomTF.setText(user.getPrenom());
            emailTF.setText(user.getEmail());
            adresseTF.setText(user.getAdresse());
            numtelTF.setText(String.valueOf(user.getNum_tel()));
            professionTF.setText(user.getProfession());
            mdpTF.setText(user.getPassword());
        }

        //Email Functions
        private boolean emailExists(String email) throws SQLException{
            conx = DataBase.getInstance().getConx();
            String query = "SELECT * FROM `user` WHERE email=?";
            PreparedStatement statement = conx.prepareStatement(query);
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
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);
                System.out.println("Confirmation email sent successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
        //End Functions Email

        //Add Function
        @FXML
        void AjouterButton(ActionEvent event) {

            try {
                int CIN = Integer.parseInt(cinTF.getText());
                String NOM = nomTF.getText();
                String PRENOM = prenomTF.getText();
                String EMAIL = emailTF.getText();
                String ADRESSE = adresseTF.getText();
                String MDP = mdpTF.getText();
                int NUMTEL = Integer.parseInt(numtelTF.getText());
                String ROLE = roleCOMBOBOX.getValue();
                String PROFESSION = professionTF.getText();
                boolean isValid = true;
                String errorMessage = "";
                if (cinTF.getText().isEmpty() || cinTF.getText().length() != 8) {
                    errorMessage += "CIN doit être de 8 chiffres.\n";
                    isValid = false;
                }
                if (NOM.trim().isEmpty() || NOM.matches(".*\\d+.*")) {
                    errorMessage += "Nom est requis et ne doit pas contenir de chiffres.\n";
                    isValid = false;
                }
                if (PRENOM.trim().isEmpty() || PRENOM.matches(".*\\d+.*")) {
                    errorMessage += "Prénom est requis et ne doit pas contenir de chiffres.\n";
                    isValid = false;
                }
                if (ADRESSE.trim().isEmpty()) {
                    errorMessage += "Adresse est requise.\n";
                    isValid = false;
                }
                if (MDP.trim().isEmpty()) {
                    errorMessage += "Mot de passe est requis.\n";
                    isValid = false;
                }

                if (PROFESSION.trim().isEmpty()) {
                    errorMessage += "Profession est requise.\n";
                    isValid = false;
                }
                if (isValid) {
                    if (EMAIL.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(esprit\\.tn|gmail\\.com|outlook\\.(com|tn|fr)|yahoo\\.(com|tn|fr))$")) {
                        if (numtelTF.getText().matches("\\d{8}")) {
                            if (!emailExists(EMAIL)) {
                                UserS.Add(new User(CIN, NOM, PRENOM, EMAIL, ADRESSE, NUMTEL, MDP, ROLE, PROFESSION));
                                uinfolabel.setText("Ajout Effectué");
                                String subject = "Account confirmed !";
                                String body = String.format("Bonjour%s,\n\nVos informations ont bien été enregistrés.\n\nCordialement,", PRENOM);
                                sendEmailConfirmation(EMAIL, subject, body);
                            }
                            else {
                                uinfolabel.setText("Email existe déjà.");
                            }
                        } else {
                            uinfolabel.setText("N° Télèphone est invalide.");
                        }
                    } else {
                        uinfolabel.setText("Email est invalide.");
                    }
                } else {
                    uinfolabel.setText(errorMessage);
                }

            } catch (NumberFormatException e) {
                uinfolabel.setText("CIN et Numéro de Téléphone doivent être des chiffres.");
            } catch (SQLException e) {
                uinfolabel.setText("Erreur de base de données.");
            }
        }

        //Alert Function
        private void showAlert(String title, String message, Alert.AlertType type) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        //Excel Function
        @FXML
        void ExporterExcel(ActionEvent event) {
            List<User> users = UserS.afficher(); // This method should return a List of all users
            exportUsersToExcel(users);

        }
        private void exportUsersToExcel(List<User> users) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Users");
            Row headerRow = sheet.createRow(0);
            String[] columnNames = {"ID", "CIN", "Name", "Surname", "Email", "Address", "Phone", "Role", "Profession"};
            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames[i]);
            }

            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getCin());
                row.createCell(2).setCellValue(user.getNom());
                row.createCell(3).setCellValue(user.getPrenom());
                row.createCell(4).setCellValue(user.getEmail());
                row.createCell(5).setCellValue(user.getAdresse());
                row.createCell(6).setCellValue(user.getNum_tel());
                row.createCell(7).setCellValue(user.getRoles());
                row.createCell(8).setCellValue(user.getProfession());
            }

            for (int i = 0; i < columnNames.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("Users.xlsx")) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //End Functions Excel




        //Search Functions
        @FXML
        public void RechercheNom(ActionEvent actionEvent) {
            int column = 0;
            int row = 1;
            String recherche = SearchBar.getText();
            try {
                userContainer.getChildren().clear();
                for (User user : UserS.Rechreche(recherche)){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/CardUser.fxml"));
                    Pane userBox = fxmlLoader.load();
                    //cardC = fxmlLoader.getController();
                    userContainer.setUserData(user);
                    if (column == 3) {
                        column = 0;
                        ++row;
                    }
                    userContainer.add(userBox, column++, row);
                    GridPane.setMargin(userBox, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



