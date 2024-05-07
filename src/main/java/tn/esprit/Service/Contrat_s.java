package tn.esprit.Service;

import tn.esprit.entity.Contrat;
import utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Contrat_s implements Service<Contrat> {
    private Connection cnx;

    // Empty constructor
    public Contrat_s() {
        this.cnx = DataBase.getInstance().getConn(); // Initialize connection
    }

    public static Contrat_s getInstance() {
        // Implement singleton pattern to return an instance of Contrat_s
        return new Contrat_s();
    }
    @Override
    public void add(Contrat contrat) throws SQLException {
        String qry = "INSERT INTO contrat (duree, date_de_souscription, type_couverture) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, contrat.getDuree());
            pstm.setString(2, contrat.getDate_de_souscription());
            pstm.setString(3, contrat.getType_couverture());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add(Contrat c, Connection conn) throws SQLException {
        if (conn != null) {
            String qry = "INSERT INTO contrat (duree, date_de_souscription, type_couverture) VALUES (?, STR_TO_DATE(?, '%m/%d/%Y'), ?)";
            try {
                PreparedStatement pstm = conn.prepareStatement(qry);
                pstm.setInt(1, c.getDuree());
                pstm.setString(2, c.getDate_de_souscription());
                pstm.setString(3, c.getType_couverture());
                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<Contrat> show() throws SQLException {
        List<Contrat> contrats = new ArrayList<>();
        String qry = "SELECT * FROM contrat";

        try (PreparedStatement pstm = cnx.prepareStatement(qry);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Contrat contrat = new Contrat();
                contrat.setId(rs.getInt("id"));
                contrat.setDuree(rs.getInt("duree"));
                contrat.setDate_de_souscription(rs.getString("date_de_souscription"));
                contrat.setType_couverture(rs.getString("type_couverture"));
                contrats.add(contrat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return contrats;
    }

    @Override
    public void delete(int id) throws SQLException {
        String qry = "DELETE FROM contrat WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit(Contrat contrat) throws SQLException {
        String qry = "UPDATE contrat SET duree = ?, date_de_souscription = ?, type_couverture = ? WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, contrat.getDuree());
            pstm.setString(2, contrat.getDate_de_souscription());
            pstm.setString(3, contrat.getType_couverture());
            pstm.setInt(4, contrat.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTypeCovertureByContratId(int contratId) throws SQLException {
        System.out.println("Fetching type_couverture for contratId: " + contratId); // Print the contratId
        String query = "SELECT type_couverture FROM contrat WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, contratId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String typeCouverture = resultSet.getString("type_couverture");
                    System.out.println("Fetched type_couverture: " + typeCouverture); // Print the fetched type_couverture
                    return typeCouverture;
                } else {
                    throw new SQLException("Contrat with ID " + contratId + " not found.");
                }
            }
        }
    }
    
    @Override
    public void update(Contrat contrat) throws SQLException {
        String qry = "UPDATE contrat SET duree = ?, date_de_souscription = ?, type_couverture = ? WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, contrat.getDuree());
            pstm.setString(2, contrat.getDate_de_souscription());
            pstm.setString(3, contrat.getType_couverture());
            pstm.setInt(4, contrat.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}