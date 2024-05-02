   /*package tn.esprit;

import entity.Assurance;
import entity.Contrat; // Import Contrat class
import Service.Assurance_s;
import Service.Contrat_s; // Import Contrat_s class
import utils.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class
Main {
    public static void main(String[] args) {
        // Obtenez une instance de la base de données
        Connection cnx = DataBase.getInstance().getConn();

        // Créez une instance de Contrat_s en lui passant la connexion
        Contrat_s contratService = new Contrat_s(cnx);

        try {
            // Créez un objet Contrat avec des valeurs de test pour l'ajouter dans la base de données
            Contrat contratToAdd = new Contrat();
            // Set values for the contratToAdd object
            contratToAdd.setId(1);
            contratToAdd.setDuree(12);
            contratToAdd.setDate_de_souscription("2024-03-28");
            contratToAdd.setType_couverture("Type de couverture");

            // Ajoutez le contrat dans la base de données
            contratService.add(contratToAdd);

            // Affichez tous les contrats dans la base de données
            List<Contrat> allContrats = contratService.show();
            System.out.println("Tous les contrats dans la base de données :");
            for (Contrat contrat : allContrats) {
                System.out.println(contrat);
            }

            // Supprimez un contrat de la base de données (remplacez 1 par l'ID du contrat à supprimer)
            contratService.delete(1);
            System.out.println("Contrat avec ID 1 supprimé avec succès.");

            // Modifiez un contrat existant dans la base de données (remplacez 2 par l'ID du contrat à modifier)
            Contrat contratToEdit = new Contrat();
            contratToEdit.setId(2);
            // Set new values for contratToEdit object
            contratToEdit.setDuree(24);
            contratToEdit.setDate_de_souscription("2024-03-28");
            contratToEdit.setType_couverture("Nouveau type de couverture");
            contratService.edit(contratToEdit);
            System.out.println("Contrat avec ID 2 modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Assurez-vous de fermer la connexion après utilisation
            if (cnx != null) {
                try {
                    cnx.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
*/