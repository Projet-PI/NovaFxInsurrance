package tn.esprit.services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tn.esprit.Interfaces.IServiceDevis;
import tn.esprit.entities.AvisRestau;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceDevis implements IServiceDevis {

    Connection cnx = DataBase.getInstance().getConx();
    private static final int STATIC_USER_ID = 2; // Utilisateur statique, vous devez remplacer cela par la gestion de l'utilisateur connecté


    @Override
    public void ajouter(Object o) {
        if (o instanceof AvisRestau) {
            AvisRestau avis = (AvisRestau) o;
            String req = "INSERT INTO response_devis(devis_id_id,status,response) VALUES (?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                //   String querry = "INSERT INTO response_devis(id,devis_id_id,status,response) VALUES (?, ?,?,?)";
                //ps.setInt(1, STATIC_USER_ID);
                //ps.setInt(1, avis.getIdR());
                ps.setInt(1, avis.getDevis_id_id());
                ps.setString(2, avis.getStatus());
                ps.setString(3, avis.getResponse());


                ps.executeUpdate();

                // Récupérer la clé primaire générée si nécessaire
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idAvis = generatedKeys.getInt(1);
                    avis.setId(idAvis);
                }

                System.out.println("Avis added!");
            } catch (SQLException e) {
                // Handle or log the exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void modifier(Object o) {
        if (o instanceof AvisRestau) {
            AvisRestau user = (AvisRestau) o;
            // String req = "UPDATE response_devis SET devis_id_id = ?, response = ?, status = ? WHERE id = ?";
            System.out.println("Attempting to update user with ID: " + user.getId());  // Debug statement

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                String query = "UPDATE response_devis SET devis_id_id = ?, response = ?, status = ? WHERE id = ?";

                connection = DataBase.getInstance().getConx();
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(query);
                //  System.out.println("User Details: " + user.toString());  // Make sure 'User' class has a proper 'toString' method
                preparedStatement.setInt(1, user.getDevis_id_id());
                preparedStatement.setString(2, user.getResponse());
                preparedStatement.setString(3, user.getStatus());
                preparedStatement.setInt(4, user.getId());


                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    connection.commit();

                } else {
                    System.out.println("Update failed - No rows affected. Check if the ID exists: " + user.getId());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void supprimer(int id) {

        // Suite a la derniere suivie le 03/05/2024 le prof a demande d'ajouter une alerte de supprition .
        /*     String req = "DELETE FROM response_devis WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

                System.out.println("restaurant with ID " + id + " deleted successfully!");
            } else {
                System.out.println("No record found with ID " + id + ". Deletion failed.");
            }
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }

   }*///// Ajout alerte
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ?");
        alert.setContentText("Cette action est irréversible.");

        // Personnalisation des boutons de la boîte de dialogue
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Affichage de la boîte de dialogue et attente de la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                String req = "DELETE FROM response_devis WHERE id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(req)) {
                    ps.setInt(1, id);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {

                        System.out.println("restaurant with ID " + id + " deleted successfully!");
                    } else {
                        System.out.println("No record found with ID " + id + ". Deletion failed.");
                    }
                } catch (SQLException e) {
                    // Handle or log the exception
                    e.printStackTrace();
                }
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'élément a été supprimé avec succès.");
            }
        });

    }


    // Méthode pour afficher une boîte de dialogue d'alerte
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @Override
    public Object getOneById(int id) {
        return null;
    }


    public List<AvisRestau> getAll() {
        List<AvisRestau> avisList = new ArrayList<>();
        String req = "SELECT id,devis_id_id,response,status FROM response_devis";
        try (Statement st = cnx.createStatement();
             ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String response = res.getString("response");

                String status = res.getString("status");
                int devis_id_id = res.getInt("devis_id_id");
                AvisRestau avis = new AvisRestau(id, response, status, devis_id_id);
                avisList.add(avis);
            }
        } catch (SQLException e) {
            // Gérer ou enregistrer l'exception
            e.printStackTrace();
        }
        return avisList;
    }


}
