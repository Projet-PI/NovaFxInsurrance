package tn.esprit.services;

import tn.esprit.Interfaces.IUtilisateur;
import tn.esprit.entities.User;
import tn.esprit.utils.DataBase;
import java.sql.*;
import java.util.ArrayList;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.UUID;

public class ServiceUtilisateurs implements IUtilisateur<User> {
    private Connection conx;
    public ServiceUtilisateurs(){conx = DataBase.getInstance().getConx();}


    @Override
    public boolean Add(User user) {
        String qry = "INSERT INTO `user`(`nom`, `prenom`, `adresse`, `email`, `num_tel`, `profession`, `password`, `cin`,`role`) VALUES (?,?,?,?,?,?,?,?,?)";
        boolean isAdded = false;

        try (PreparedStatement stm = conx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, user.getNom());
            stm.setString(2, user.getPrenom());
            stm.setString(3, user.getAdresse());
            stm.setString(4, user.getEmail());
            stm.setInt(5, user.getNum_tel());
            stm.setString(6, user.getProfession());
            stm.setString(7, user.getPassword());
            stm.setInt(8, user.getCin());
            stm.setString(9,user.getRoles());

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        isAdded = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isAdded;
    }


    @Override
    public ArrayList<User> getAll() {
        return null;
    }

    @Override
    public List<User> afficher(String searchQuery, int offset,int pageSize) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query += " WHERE `nom` LIKE ?";
        }

        query += " LIMIT ?, ?";

        try (PreparedStatement stmt = conx.prepareStatement(query)) {
            if (searchQuery != null && !searchQuery.isEmpty()) {
                stmt.setString(1, "%" + searchQuery + "%");
            }

            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);



            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getInt("cin"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("adresse"),
                            rs.getInt("num_tel"),
                            rs.getString("password"),
                            rs.getString("profession"),
                            rs.getString("role")
                    );
                    users.add(user);
                }
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error preparing statement: " + e.getMessage());
        }
        return users;
    }



    @Override
    public List<User> TriparNom() {
        return null;
    }

    @Override
    public List<User> TriparEmail() {
        return null;
    }

    @Override
    public List<User> Rechreche(String recherche) {
        return null;
    }

    @Override
    public boolean Update(User user) {

        System.out.println("Attempting to update user with ID: " + user.getId());

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            String query = "UPDATE user SET nom=?, prenom=?, adresse=?, email=?, num_tel=?, profession=?, cin=?, role=? WHERE id=?";

            connection = DataBase.getInstance().getConx();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            System.out.println("User Details: " + user.toString());

            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getAdresse());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getNum_tel());
            preparedStatement.setString(6, user.getProfession());
            preparedStatement.setInt(7, user.getCin());
            preparedStatement.setString(8, user.getRoles());
            preparedStatement.setInt(9, user.getId());


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                connection.commit();
                return true;
            } else {
                System.out.println("Update failed - No rows affected. Check if the ID exists: " + user.getId());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
        }

    @Override
    public boolean Delete(User user) {
        String query = "DELETE FROM user WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DataBase.getInstance().getConx();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
                return true;
            } else {
                System.out.println("No user found with ID: " + user.getId());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error occurred during the delete operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void DeleteByID(int id) {

    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(esprit\\.tn|gmail\\.com|outlook\\.(com|tn|fr)|yahoo\\.(com|tn|fr))$";
        return email.matches(emailRegex);
    }
    public boolean isValidPhoneNumber(int numTel) {
        String numTelStr = String.valueOf(numTel);
        return numTelStr.length() == 8;
    }
    public boolean checkUserExists(String email) {
        String req = "SELECT count(1) FROM `user` WHERE `email`=?";
        boolean exists = false;
        try {
            PreparedStatement ps = conx.prepareStatement(req);
            ps.setString(1, email);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                exists = res.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
        }
        return exists;
    }


    public void requestPasswordReset(String email) throws SQLException, MessagingException {
        String sql = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement stmt = conx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String token = UUID.randomUUID().toString();
                long currentTimeMillis = System.currentTimeMillis();
                long expirationTimeMillis = currentTimeMillis + (1000 * 60 * 60);
                Timestamp expirationTime = new Timestamp(expirationTimeMillis);

                sql = "UPDATE user SET reset_token = ?, reset_token_expiration = ? WHERE email = ?";
                try (PreparedStatement updateStmt = conx.prepareStatement(sql)) {
                    updateStmt.setString(1, token);
                    updateStmt.setTimestamp(2, expirationTime);
                    updateStmt.setString(3, email);
                    updateStmt.executeUpdate();

                    sendResetEmail(email, token);
                }
            } else {
                throw new SQLException("Email not found.");
            }
        }
    }

    private void sendResetEmail(String recipientEmail, String token) throws MessagingException {

        final String senderEmail = "slim.bentanfous@esprit.tn";
        final String senderPassword = "Salamlam2002!";

        String host = "smtp-mail.outlook.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.stattls.enable","true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Réinitialisation du Mot de Passe");
        message.setText("Pour réinitialiser votre Mot de Passe, utiliser ce Token:\n" + token + "\nLe Token est valide que pour 1 heure.");
        Transport.send(message);
        System.out.println("TEEEEEEEEEEST Envoi Email " + recipientEmail);
    }

    @Override
    public List<User> RechrecheUser(String recherche) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT `id`, `nom`, `prenom`, `adresse`, `email`, `num_tel`, `profession`, `password`, `cin`, `role` FROM `user` WHERE 1WHERE `nom` LIKE '%" + recherche + "%' OR `prenom` LIKE '%" + recherche + "%' OR `email`LIKE '%" + recherche + "%'";
        try {
            Statement ste = conx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                rs.getInt("id");
                        rs.getInt("cin");
                        rs.getString("nom");
                        rs.getString("prenom");
                        rs.getString("email");
                        rs.getString("adresse");
                        rs.getInt("num_tel");
                        rs.getString("profession");
                        rs.getString("role");
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return users;
    }

    public boolean updateWithoutPassword(User user) {
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, num_tel = ?, adresse = ?, profession = ?, cin = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = DataBase.getInstance().getConx();
            if (con == null || !con.isValid(5)) {
                System.err.println("Connection is not valid or closed.");
                return false;
            }
            con.setAutoCommit(false);
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getNum_tel());
            stmt.setString(5, user.getAdresse());
            stmt.setString(6, user.getProfession());
            stmt.setInt(7, user.getCin());
            stmt.setInt(8, user.getId());

            int affectedRows = stmt.executeUpdate();

            con.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getSQLState() + " - " + e.getErrorCode());
            System.err.println("Error updating user: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("SQL Exception on rollback. Error: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                System.err.println("SQL Exception on closing. Error: " + ex.getMessage());
            }
        }
    }



    public List<Integer> getUserIdsWithRole(String role) {
        List<Integer> userIds = new ArrayList<>();
        try {
            String sql = "SELECT id FROM user WHERE role = ?";
            PreparedStatement preparedStatement = conx.prepareStatement(sql);
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userIds.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement preparedStatement = conx.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setNom(resultSet.getString("nom"));
            // Set other user properties as needed
            return user;
        } else {
            return null; // User not found
        }
    }

    public String getClientemailById(int id) {
        String email = new String();
        try {
            String sql = "SELECT email FROM user WHERE id = ?";
            PreparedStatement preparedStatement = conx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                email = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }
    public String getUserNameWithId(int id) {
        String userName = new String();
        try {
            String sql = "SELECT nom FROM user WHERE id = ?";
            PreparedStatement preparedStatement = conx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userName = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }
}
