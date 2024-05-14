        package tn.esprit.controllers.DEVIS;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.text.Text;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import tn.esprit.entities.Devis;
        import tn.esprit.services.ServiceR;

        import java.io.File;
        import java.io.IOException;

        public class Ajouter {


            @FXML
            private TextField adresse;
            @FXML
            private Button devis;

            @FXML
            private TextField email;

            @FXML
            private TextField nom;

            @FXML
            private TextField phone;

            @FXML
            private Button save;

            @FXML
            private Text errorNom;

            @FXML
            private Text errorAdresse;
            @FXML
            private Button Dashboard;
            @FXML
            private Text errorEmail;

            @FXML
            private Text errorPhone;



            @FXML
            private Button selectImage;
            @FXML
            private ImageView imageView;

            private File selectedImageFile;

// Methode d'ajout Valide mais sans  Envoie de SMS apres la reponse de devis


//            @FXML
//            void ajouterRestaurantAction(ActionEvent event) {
//                // Retrieve values from text fields
//                String nomR = nom.getText();
//                String adresseR = adresse.getText();
//                String emailR = email.getText();
//                int phoneR = Integer.parseInt(phone.getText()); // Assuming phone number is an integer
//
//                // Check if an image is selected
//                String imageR = (selectedImageFile != null) ? selectedImageFile.getPath() : "";
//
//
//                // Create a Restaurant object
//                Restaurant restaurant = new Restaurant(nomR, adresseR, emailR, phoneR, imageR);
//
//                // Call the ajouter method of ServiceRestau to add the restaurant to the database
//                ServiceRestau service = new ServiceRestau();
//                service.ajouter(restaurant);
//
//                // Clear fields and reset selected image
//                nom.clear();
//                adresse.clear();
//                phone.clear();
//                email.clear();
//                imageView.setImage(null);
//                selectedImageFile = null;
//            }

            @FXML
            void ajouterRestaurantAction(ActionEvent event) {


                // Validate input fields before proceeding
                if (isInputValid()) {
                    // Retrieve values from text fields
                   // int id=int
                    String name = nom.getText();
                    String prenom = adresse.getText();
                    String emailR = email.getText();
                    int idR= Integer.parseInt(phone.getText());
                    int id = Integer.parseInt(phone.getText()); // Assuming phone number is an integer

                    // Check if an image is selected
                    String imageR = (selectedImageFile != null) ? selectedImageFile.getPath() : "";

                    // Create a Restaurant object
                    Devis devis = new Devis(idR,name,prenom , emailR);

                    // Call the ajouter method of ServiceRestau to add the restaurant to the database
                    ServiceR service = new ServiceR();
                    service.ajouter(devis);

                    // Clear fields and reset selected image
                    nom.clear();
                    adresse.clear();
                    phone.clear();
                    email.clear();
                    imageView.setImage(null);
                    selectedImageFile = null;

                }


            }

            private boolean isInputValid() {
                boolean isValid = true;

                // Validate and display error messages
                if (nom.getText().isEmpty() || !nom.getText().matches("^[a-zA-Z]+$")) {
                    errorNom.setText("Nom is required and should not contain numbers");
                    isValid = false;
                } else {
                    errorNom.setText("");
                }

                if (adresse.getText().isEmpty() || !nom.getText().matches("^[a-zA-Z]+$")) {
                    errorAdresse.setText("Adresse is required and should not contain numbers ");
                    isValid = false;
                } else {
                    errorAdresse.setText("");
                }

                if (email.getText().isEmpty()) {
                    errorEmail.setText("Email is required");
                    isValid = false;
                } else if (!email.getText().contains("@")) {
                    errorEmail.setText("Invalid email format");
                    isValid = false;
                } else {
                    errorEmail.setText("");
                }



                return isValid;
            }


            @FXML
            void selectImageAction(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );
                selectedImageFile = fileChooser.showOpenDialog(null);

                if (selectedImageFile != null) {
                    // Display the selected image in the ImageView
                    Image image = new Image(selectedImageFile.toURI().toString());
                    imageView.setImage(image);
                }
            }
            @FXML
            void initialize()  {

                assert adresse != null : "fx:id=\"adresse\" was not injected: check your FXML file 'ajouter.fxml'.";
                assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'ajouter.fxml'.";
                //assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'ajouter.fxml'.";
                assert nom != null : "fx:id=\"nom\" was not injected: check your FXML file 'ajouter.fxml'.";
                assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'ajouter.fxml'.";
                assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'ajouter.fxml'.";

        }





            @FXML
            public void Dashboard(ActionEvent event) throws IOException {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher.fxml"));
                    Parent root = loader.load();

                    // Créer la scène avec la nouvelle page
                    Scene scene = new Scene(root);

                    // Obtenir la scène actuelle à partir du bouton cliqué
                    Stage stage = (Stage) Dashboard.getScene().getWindow();

                    // Remplacer la scène actuelle par la nouvelle scène
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void devis(ActionEvent event) throws IOException {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/backavis.fxml"));
                    Parent root = loader.load();

                    // Créer la scène avec la nouvelle page
                    Scene scene = new Scene(root);

                    // Obtenir la scène actuelle à partir du bouton cliqué
                    Stage stage = (Stage) devis.getScene().getWindow();

                    // Remplacer la scène actuelle par la nouvelle scène
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }



