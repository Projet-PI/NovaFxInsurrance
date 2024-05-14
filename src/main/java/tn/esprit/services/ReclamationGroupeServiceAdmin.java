package tn.esprit.services;

import org.jetbrains.annotations.NotNull;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.utils.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReclamationGroupeServiceAdmin {
    Connection connection;

    public ReclamationGroupeServiceAdmin() {
        connection = DataBase.getInstance().getConx();
    }

    public ResultSet GetAll(int pageNumber, int pageSize, String searchQuery, String filterBy) {
        ResultSet rs = null;

        // inject a user id
        // check if the user is agent or normal user
    /*
        TODO:
        - check if the user is agent or normal user
     */
        try {
            int offset = (pageNumber - 1) * pageSize;
            String req = "SELECT * FROM `reclamation_groupe`";

            // If there's a search query, add a WHERE clause
            // if there's filter by status, add a WHERE clause
            if (searchQuery != null && !searchQuery.isEmpty()) {
                req += " WHERE `name` LIKE ?";
            }

            if (filterBy != null && !filterBy.isEmpty()) {
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    req += " AND `status` = ?";
                } else {
                    req += " WHERE `status` = ?";
                }
            }

            req += " LIMIT ?, ?";

            PreparedStatement st = connection.prepareStatement(req);

            // Set parameters
            int parameterIndex = 1;

            // Set search query parameter if applicable
            if (searchQuery != null && !searchQuery.isEmpty()) {
                st.setString(parameterIndex++, "%" + searchQuery + "%");
            }

            st.setInt(parameterIndex++, offset);
            st.setInt(parameterIndex++, pageSize);

            rs = st.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }


    public ResultSet GetById(int id) {
        ResultSet rs = null;

        // inject a user id
        // check if the user is agent or normal user
        /*
            TODO:
            - check if the user is agent or normal user
         */
        try {
            String request = "SELECT * FROM `reclamation_groupe` WHERE `id` =" + id + " AND `user_id_id` = 1";
            PreparedStatement st = connection.prepareStatement(request);
            rs = st.executeQuery(request);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public void AddReclamationGroupe(@NotNull reclamation_groupe p) {
        String request = "INSERT INTO `reclamation_groupe`( `user_id_id`, `reclamation_agent_id_id`, `name`, `day_time`, `status`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(request);
            st.setInt(1, p.getUser_id_id());
            st.setInt(2, p.getReclamation_agent_id_id());
            st.setString(3,p.getName());
            st.setObject(4, p.getDay_time());
            st.setString(5, p.getStatus());
            st.executeUpdate();
            System.out.println("reclamation_groupe ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateReclamationGroupe(@NotNull reclamation_groupe p) {
        String request = "UPDATE reclamation_groupe SET user_id_id=?, reclamation_agent_id_id=?,name=?,day_time=?,status=? WHERE id =?";
        try {
            PreparedStatement st = connection.prepareStatement(request);
            st.setInt(6,p.getId());
            st.setInt(1, p.getUser_id_id());
            st.setInt(2, p.getReclamation_agent_id_id());
            st.setString(3, p.getName());
            st.setObject(4, p.getDay_time());
            st.setString(5, p.getStatus());
            int rowsAffected = st.executeUpdate();
            return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void DeleteReclamationGroupe(int id) {
        String request = "DELETE FROM reclamation_groupe WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(request);
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("reclamation_groupe supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
