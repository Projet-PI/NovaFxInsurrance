package tn.esprit.services;

import tn.esprit.entities.Sinistre;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSinistre  {

    private Connection connection;

    public ServiceSinistre(){
        try {
            connection = DataBase.getInstance().getConx();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void ajouter(Sinistre sinistre) throws SQLException {
        String sql = "INSERT INTO sinistre (sinistre_client_id, sinistre_expert_id, is_fautif, pourcentage, rapport) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sinistre.getSinistre_client_id());
        preparedStatement.setInt(2, sinistre.getSinistre_expert_id());
        preparedStatement.setInt(3, sinistre.getIs_fautif());
        preparedStatement.setInt(4, sinistre.getPourcentage());
        preparedStatement.setString(5, sinistre.getRapport()); // Modifier ici pour utiliser setString pour la propriété "rapport"
        preparedStatement.executeUpdate();
    }

    public void modifier(Sinistre sinistre) throws SQLException {
        String sql = "UPDATE sinistre SET sinistre_client_id = ?, sinistre_expert_id = ?, is_fautif = ?, pourcentage = ?, rapport = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sinistre.getSinistre_client_id());
        preparedStatement.setInt(2, sinistre.getSinistre_expert_id());
        preparedStatement.setInt(3, sinistre.getIs_fautif());
        preparedStatement.setInt(4, sinistre.getPourcentage());
        preparedStatement.setString(5, sinistre.getRapport()); // Modifier ici pour utiliser setString pour la propriété "rapport"
        preparedStatement.setInt(6, sinistre.getId());
        preparedStatement.executeUpdate();
    }
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM sinistre WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Sinistre> afficher() throws SQLException {
        List<Sinistre> sinistres = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT * FROM sinistre";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Sinistre s = new Sinistre();
                s.setId(rs.getInt("id"));
                s.setSinistre_client_id(rs.getInt("sinistre_client_id"));
                s.setSinistre_expert_id(rs.getInt("sinistre_expert_id"));
                s.setIs_fautif(rs.getInt("is_fautif"));
                s.setPourcentage(rs.getInt("pourcentage"));
                s.setRapport(rs.getString("rapport"));
                sinistres.add(s);
            }
        } else {
            System.err.println("Failed to establish database connection.");
        }
        return sinistres;
    }

    public List<Sinistre> afficherMaListe(String userName) throws SQLException {
        return null;
    }


    public List<Integer> getSinistreIdsByUserExpert(int userId, int expertId) throws SQLException {
        List<Integer> sinistreIds = new ArrayList<>();
        // Assuming you have a connection to your database
        Connection connection = DataBase.getInstance().getConx(); // Assuming DB is your database utility class
        // Define your SQL query
        String sql = "SELECT id FROM sinistre WHERE sinistre_client_id = ? AND sinistre_expert_id = ?";
        // Create a prepared statement
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, expertId);
            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Iterate over the result set and populate the list of sinistre IDs
                while (resultSet.next()) {
                    int sinistreId = resultSet.getInt("id");
                    sinistreIds.add(sinistreId);
                }
            }
        }
        return sinistreIds;
    }
    public int getpourcentage(int id) throws SQLException {
        int sinistreIds = 0;
        // Assuming you have a connection to your database
        Connection connection = DataBase.getInstance().getConx(); // Assuming DB is your database utility class
        // Define your SQL query
        String sql = "SELECT pourcentage FROM sinistre WHERE id = ?";
        // Create a prepared statement
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters
            preparedStatement.setInt(1, id);
            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Iterate over the result set and populate the list of sinistre IDs
                while (resultSet.next()) {
                    int sinistreId = resultSet.getInt("pourcentage");
                    sinistreIds = sinistreId;
                }
            }
        }
        return sinistreIds;
    }
public int getIsFautif(int id){
        int isFautif = 0;
    Connection connection = DataBase.getInstance().getConx(); // Assuming DB is your database utility class
    // Define your SQL query
    String sql = "SELECT is_fautif FROM sinistre WHERE id = ?";
    // Create a prepared statement
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        // Set the parameters
        preparedStatement.setInt(1, id);
        // Execute the query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // Check if an is_fautif value is found
            if (resultSet.next()) {
                isFautif = resultSet.getInt("is_fautif");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return isFautif;
}

}