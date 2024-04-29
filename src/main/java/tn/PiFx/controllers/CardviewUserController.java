package tn.PiFx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import tn.PiFx.entities.User;
import tn.PiFx.services.ServiceUtilisateurs;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class CardviewUserController implements Initializable {
    private final ServiceUtilisateurs UserS = new ServiceUtilisateurs();

    @FXML
    private TextField idModifTF;
    @FXML
    private TextField CinModifTF;

    @FXML
    private TextField AdresseModifTF;
    @FXML
    public GridPane userContainer;

    @FXML
    private TextField EmailModifTf;

    @FXML
    private TextField NomModifTf;

    @FXML
    private TextField NumTelModifTf;

    @FXML
    private TextField PrenomModifTf;

    @FXML
    public Button ModifyButton;

    @FXML

    private TextField ProfessionModifTf;

    @FXML
    private ComboBox<String> RolesModifCB;
    @FXML
    private Pane Card;
    private String[] colors = {"#CDB4DB", "#FFC8DD", "#FFAFCC", "#BDE0FE", "#A2D2FF",
            "#F4C2D7", "#FFD4E2", "#FFB7D0", "#A6D9FF", "#8BC8FF",
            "#E6A9CB", "#FFBFD3", "#FFA7C1", "#9AC2FF", "#74AFFA",
            "#D8B6D8", "#FFC9D7", "#FFB3C8", "#B0E1FF", "#8DCFFD",
            "#D3AADB", "#FFBEDF", "#FFA9CC", "#AFD5FF", "#93C5FF"};

    int uid,unumtel;
    String unom, uprenom, uemail, umdp, urole;
    private AdminUserController adminUserController;
    private User currentUser;
    ObservableList<String> RoleList = FXCollections.observableArrayList("[\"ROLE_USER\"]", "[\"ROLE_ADMIN\"]");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RolesModifCB.setItems(RoleList);

    }

    public void setData(User user){

        this.currentUser = user;
        Card.setBackground(Background.fill(Color.web(colors[(int)(Math.random()* colors.length)])));
        Card.setStyle("-fx-border-radius: 5px;-fx-border-color:#808080");
        idModifTF.setText(String.valueOf(user.getId()));
        CinModifTF.setText(String.valueOf(user.getCin()));
        NomModifTf.setText(user.getNom());
        PrenomModifTf.setText(user.getPrenom());
        EmailModifTf.setText(user.getEmail());
        AdresseModifTF.setText(user.getAdresse());
        NumTelModifTf.setText(String.valueOf(user.getNum_tel()));
        ProfessionModifTf.setText(user.getProfession());
        RolesModifCB.setValue(user.getRoles());
        Card.setBackground(Background.fill(Color.web(colors[(int)(Math.random()* colors.length)])));
    }


    /*public void setData(User user) {

        Card.setBackground(Background.fill(Color.web(colors[(int)(Math.random()* colors.length)])));
        Card.setStyle("-fx-border-radius: 5px;-fx-border-color:#808080");
        uid = user.getId();
        uprenom = user.getPrenom();
        unom = user.getNom();
        uemail = user.getEmail();
        umdp = user.getPassword();
        urole = user.getRoles();
        unumtel = user.getNum_tel();
    }*/



    @FXML
    void SupprimerButtonUser(ActionEvent event) {
        try {
            int userId = Integer.parseInt(idModifTF.getText());

            if (userId > 0) {
                User userToDelete = new User();
                userToDelete.setId(userId);
                boolean isDeleted = UserS.Delete(userToDelete);
                if (isDeleted) {
                    showAlert("Success", "User successfully deleted.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to delete the user. No user found with ID: " + userId, Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Invalid user ID.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid user ID format. Please enter a valid number.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "An error occurred while deleting the user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ModifierButtonUser(ActionEvent event) throws SQLException {

        System.out.println("test0");

        try {
            System.out.println("ID from TextField: " + idModifTF.getText());

            int id = Integer.parseInt(idModifTF.getText());
            int cin = Integer.parseInt(CinModifTF.getText());
            String nom = NomModifTf.getText();
            String prenom = PrenomModifTf.getText();
            String email = EmailModifTf.getText();
            String adresse = AdresseModifTF.getText();
            int numtel = Integer.parseInt(NumTelModifTf.getText());
            String role = RolesModifCB.getValue() != null ? RolesModifCB.getValue().toString() : "";
            String profession = ProfessionModifTf.getText();
            System.out.println("ID after parse: " + idModifTF.getText());

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || adresse.isEmpty() || role.isEmpty() || profession.isEmpty()) {
                showAlert("Validation Error", "Please fill in all the fields.", Alert.AlertType.ERROR);
            }
            else {
                User updatedUser = new User(id, cin, nom, prenom, adresse, email, numtel, role, profession);

                boolean updateSuccess = UserS.Update(updatedUser);
                if (updateSuccess) {
                    showAlert("Success", "User updated successfully.", Alert.AlertType.INFORMATION);
                    System.out.println("test1");
                    adminUserController.populateEditForm(this.currentUser);
                } else {
                    showAlert("Error", "Failed to update user. Check if the ID exists.", Alert.AlertType.ERROR);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter valid numbers for CIN and Telephone.", Alert.AlertType.ERROR);
        }
    }

    public void setAdminUserController(AdminUserController controller) {
        this.adminUserController = controller;
    }
    private void showAlert(String validationError, String s, Alert.AlertType alertType) {

        Alert alert = new Alert(alertType);
        alert.setTitle(validationError);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    public Button getModifyButton() {
        return ModifyButton;
    }

}



