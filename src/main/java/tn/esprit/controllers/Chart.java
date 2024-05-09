package tn.esprit.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.entities.AvisRestau;
import tn.esprit.services.ServiceDevis;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Chart implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private Button Ajout;

    @FXML
    private Button BackAvis;

    @FXML
    private Button BackAvis1;

    public void BackAvis1(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) BackAvis1.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void BackAvis(ActionEvent event) throws IOException
    {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Backavis.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) BackAvis.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void Ajout(ActionEvent event) throws IOException
    {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) Ajout.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Récupérer la liste de devis (à remplacer par la vraie liste)
        ServiceDevis serviceDevis = new ServiceDevis();
        List<AvisRestau> listeDevis = serviceDevis.getAll();


        // Créer une instance de la classe Chart
        Chart chart = new Chart();

        // Calculer les statistiques à partir de la liste de devis
        int[] statistiques = chart.calculerStatistiques(listeDevis);

        // Créer les données pour le PieChart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Traité", statistiques[0]),
                        new PieChart.Data("Non traité", statistiques[1])
                );

        // Liaison des noms et valeurs des données pour l'affichage dans le PieChart
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " : ", data.pieValueProperty()
                        )
                )
        );

        // Ajouter les données au PieChart
        pieChart.getData().addAll(pieChartData);

    }

    private int[] calculerStatistiques(List<AvisRestau> listeDevis) {
        int traiteCount = 0;
        int nonTraiteCount = 0;


        for (AvisRestau devis : listeDevis) {
            // Vérifier le statut de chaque devis et incrémenter les compteurs en conséquence
            if (devis.getStatus().equalsIgnoreCase("traite")) {
                traiteCount++;
            } else if (devis.getStatus().equalsIgnoreCase("non traite")) {
                nonTraiteCount++;
            }
        }

        // Retourner les résultats sous forme de tableau
        return new int[]{traiteCount, nonTraiteCount};
    }

}
