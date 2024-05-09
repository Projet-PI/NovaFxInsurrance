


package tn.esprit.services;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.Interfaces.IService;
import tn.esprit.entities.Devis;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceR implements IService<Devis> {

    Connection cnx = DataBase.getInstance().getConx();

    @Override
    public void ajouter(Devis devis) {
        String req = "INSERT INTO devis(name,prenom,email) VALUES (?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, devis.getName());
            ps.setString(2, devis.getPrenom());
            ps.setString(3, devis.getEmail());

            ps.executeUpdate();
            System.out.println("devis added !");
            Notifications.create()
                    .darkStyle()
                    .title("added with success")
                    .hideAfter(Duration.seconds(10))
                    .show();
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
            Notifications.create()
                    .darkStyle()
                    .title("Error")
                    .hideAfter(Duration.seconds(10))
                    .show();
        }
    }

    @Override
    public void modifier(Devis devis) {

        String req = "UPDATE devis SET name = ?, prenom = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, devis.getName());
            ps.setString(2, devis.getPrenom());
            ps.setString(3, devis.getEmail());
            ps.setInt(4, devis.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Devis avec l'ID " + devis.getId() + " mis à jour avec succès !");
            } else {
                System.out.println("Aucun enregistrement trouvé avec l'ID " + devis.getId() + ". La mise à jour a échoué.");
            }
        } catch (SQLException e) {
            // Gérer ou enregistrer l'exception
            e.printStackTrace();
        }

    }



    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM devis WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                Notifications.create()
                        .darkStyle()
                        .title("deleted with success")
                        .hideAfter(Duration.seconds(10))
                        .show();

                System.out.println("devis with ID " + id + " deleted successfully!");

            } else {
                System.out.println("No record found with ID " + id + ". Deletion failed.");
            }
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }

    }

    @Override
    public Devis getOneById(int id) {
        return null;
    }

    @Override
    public List<Devis> getAll() {
        List<Devis> devis = new ArrayList<>();
        String req = "SELECT * FROM devis";
        try (Statement st = cnx.createStatement();
             ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String prenom= res.getString("prenom");
                String email = res.getString("email");

                Devis r = new Devis(id,name, prenom,email);
                devis.add(r);
            }
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
        return (List<Devis>) devis;
    }




}
