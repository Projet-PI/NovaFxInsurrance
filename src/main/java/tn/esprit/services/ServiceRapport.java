package tn.esprit.services;

import tn.esprit.entities.RapportClient;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRapport implements IService<RapportClient> {

    private Connection connection;
    private RapportClient rapportClient;
    public ServiceRapport(){
        connection = DataBase.getInstance().getConx();
    }
    private List<RapportClient> rapports;

    @Override
    public List<RapportClient> afficher() throws SQLException {
        List<RapportClient> rapportClients = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT * FROM rapportclient1";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                RapportClient rapportClient = new RapportClient(); // Initialize RapportClient object within the loop
                rapportClient.setRapportClientId(rs.getInt("rapportClientId"));
                rapportClient.setClientNom(rs.getString("clientNom"));
                rapportClient.setClientPrenom(rs.getString("clientPrenom"));
                rapportClient.setClientEmail(rs.getString("clientEmail"));
                rapportClient.setClientPhoneNumber(rs.getInt("clientPhoneNumber"));
                rapportClient.setIsFautif(rs.getInt("isFautif")); // Assuming isFautif is boolean in the database
                rapportClient.setPourcentage(rs.getInt("pourcentage"));
                rapportClient.setRapport(rs.getString("rapport")); // Adjust column name accordingly
                rapportClients.add(rapportClient);

            }

            // Assign the fetched list to the instance variable
            this.rapports = rapportClients;

        } else {
            this.rapports = new ArrayList<>();

        }
        return rapportClients;

    }


    @Override
    public void ajouter(RapportClient rapportClient) throws SQLException {
        String sql = "INSERT INTO `rapportclient1`(`clientNom`, `clientPrenom`, `clientEmail`, `clientPhoneNumber`, `isFautif`, `pourcentage`, `rapport`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, rapportClient.getClientNom());
        preparedStatement.setString(2, rapportClient.getClientPrenom());
        preparedStatement.setString(3, rapportClient.getClientEmail());
        preparedStatement.setInt(4,rapportClient.getClientPhoneNumber());
        preparedStatement.setInt(5, rapportClient.getisFautif());
        preparedStatement.setInt(6, rapportClient.getPourcentage());
        preparedStatement.setString(7, rapportClient.getRapport());
        preparedStatement.executeUpdate();

    }


    public void modifier(RapportClient sinistre) throws SQLException {

        String sql = "UPDATE rapportclient1 SET isFautif = ?, pourcentage = ?, rapport = ? WHERE rapportClientId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, sinistre.getisFautif());
        preparedStatement.setInt(2, sinistre.getPourcentage());
        preparedStatement.setString(3,sinistre.getRapport());
        preparedStatement.setInt(4,sinistre.getRapportClientId());
        preparedStatement.executeUpdate();
        System.out.println("done");

    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM rapportclient1 WHERE rapportClientId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<RapportClient> getAll() {
        return null;
    }

    @Override
    public RapportClient getOneById(int id) {
        return null;
    }



    /*@Override
    public List<RapportClient> afficherMaListe(String userName) throws SQLException {
        List<RapportClient> rapportClients = new ArrayList<>();
        String sql = "SELECT * FROM rapportclient1 WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName); // Set the user name as a parameter
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            RapportClient rapportClient = new RapportClient();
            rapportClient.setRapportClientId(rs.getInt("id"));
            rapportClient.setClientNom(rs.getString("nom"));
            rapportClient.setClientPrenom(rs.getString("prenom"));
            rapportClient.setClientAge(rs.getInt("age"));
            rapportClient.setClientEmail(rs.getString("email"));
            rapportClient.setClientPhoneNumber(rs.getInt("phone_number"));
            rapportClients.add(rapportClient);
        }
        return rapportClients;
    }*/


    @Override
    public List<RapportClient> afficherMaListe(String userName) throws SQLException {
        return null;
    }


}