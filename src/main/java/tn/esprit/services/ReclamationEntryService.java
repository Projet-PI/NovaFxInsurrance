package tn.esprit.services;

import org.json.JSONObject;
import tn.esprit.entities.reclamation_entry;
import tn.esprit.entities.reclamation_groupe;
import tn.esprit.utils.DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Method to insert values into the join table

// Class to represent the regulation response


public class ReclamationEntryService {
    Connection connection;

    public ReclamationEntryService() {
        connection = DataBase.getInstance().getConx();
    }

    public ResultSet GetAll(reclamation_groupe item) {
        ResultSet rs = null;
        try {
            String req = "SELECT reclamation_entry.* FROM reclamation_entry " +
                    "JOIN reclamation_groupe_reclamation_entry ON reclamation_entry.id = reclamation_groupe_reclamation_entry.reclamation_entry_id " +
                    "WHERE reclamation_groupe_reclamation_entry.reclamation_groupe_id = " + item.id;
            rs = connection.prepareStatement(req).executeQuery();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public ResultSet GetById(int id) {
        ResultSet rs = null;

        // inject a user id
        // check if the user is agent or normal user
        /*
         TODO
         */

        try {
            String req = "SELECT * FROM `reclamation_entry` WHERE `id` = " + id;
            rs = connection.prepareStatement(req).executeQuery();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public void addReclamationEntry(reclamation_entry reclamationEntry, reclamation_groupe reclamationGroupe) {
        Connection connection = null;
        try {
            // Extract the prompt from reclamationEntry
            String prompt = reclamationEntry.getPrompt();
            String regulation = "";
            // Call the method to retrieve regulation and elapsed time
            if (reclamationEntry.getResponseType().equals("AI")) {
                ChatResponse regulationResponse = fetchRegulation(prompt);
                regulation = regulationResponse.getRegulation();
                ReclamationGroupeService reclamationGroupeService = new ReclamationGroupeService();
                reclamationGroupe.setStatus("completed");
                reclamationGroupeService.UpdateReclamationGroupe(reclamationGroupe);
            } else {
                reclamationEntry.setResponse("No response");
                regulation = "No response";
                reclamationEntry.setStatus("pending");
                // update reclamation group status to pending
                ReclamationGroupeService reclamationGroupeService = new ReclamationGroupeService();
                reclamationGroupe.setStatus("pending");
                reclamationGroupeService.UpdateReclamationGroupe(reclamationGroupe);
            }


            // Insert the reclamation entry into the database
            insertReclamationEntry(reclamationEntry, regulation);

            // Retrieve the ID of the inserted reclamation entry
            int reclamationEntryId = getMaxReclamationEntryId();

            // Insert the reclamation_groupe_id and reclamation_entry_id into the join table
            insertIntoJoinTable(reclamationGroupe.getId(), reclamationEntryId);

            System.out.println("Reclamation entry added successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // Method to fetch regulation from the API
    private ChatResponse fetchRegulation(String prompt) throws IOException {
        URL url = new URL("https://b071-154-108-103-84.ngrok-free.app/apiv1/regulation/get-regulation");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        JSONObject requestJson = new JSONObject();
        requestJson.put("text", prompt);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestJson.toString().getBytes());
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String regulation = jsonResponse.getString("regulation");
            String elapsedTime = jsonResponse.getString("elapsed_time");
            System.out.println("Regulation: " + regulation);
            System.out.println("Elapsed Time: " + elapsedTime);

            return new ChatResponse(regulation, elapsedTime);
        } finally {
            connection.disconnect();
        }
    }

    // Method to insert reclamation entry into the database
    private void insertReclamationEntry(reclamation_entry reclamationEntry, String regulation) throws SQLException {
        String query = "INSERT INTO reclamation_entry (prompt, day_time, response, status, response_type) VALUES (?, ?, ?, ?, ?)";
             PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reclamationEntry.getPrompt());
            preparedStatement.setString(2, reclamationEntry.getDay_time());
            preparedStatement.setString(3, regulation);
            preparedStatement.setString(4, "processed");
            preparedStatement.setString(5, reclamationEntry.getResponseType());
            preparedStatement.executeUpdate();
    }

    private void insertIntoJoinTable(int reclamationGroupeId, int reclamationEntryId) throws SQLException {
        String query = "INSERT INTO reclamation_groupe_reclamation_entry (reclamation_groupe_id, reclamation_entry_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reclamationGroupeId);
            preparedStatement.setInt(2, reclamationEntryId);
            preparedStatement.executeUpdate();
        }

    // Method to retrieve the maximum ID from the reclamation_entry table
    private int getMaxReclamationEntryId() throws SQLException {
        String query = "SELECT MAX(id) FROM reclamation_entry";
        PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    public void updateReclamationEntry(reclamation_entry reclamationEntry) {
        String request = "UPDATE reclamation_entry SET prompt=?, day_time=?, response=?, status=?, response_type=? WHERE id =?";
        try {
            PreparedStatement st = connection.prepareStatement(request);
            st.setString(1, reclamationEntry.getPrompt());
            st.setString(2, reclamationEntry.getDay_time());
            st.setString(3, reclamationEntry.getResponse());
            st.setString(4, reclamationEntry.getStatus());
            st.setString(5, reclamationEntry.getResponseType());
            st.setInt(6, reclamationEntry.id);
            st.executeUpdate();
            System.out.println("reclamation_entry modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void DeleteReclamationEntry(int id) {
        String request = "DELETE FROM reclamation_entry WHERE id =?";
        try {
            PreparedStatement st = connection.prepareStatement(request);
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("reclamation_entry supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
