package tn.esprit.utils;

import tn.esprit.entities.User;

public class SessionManager {
    private static SessionManager instance;
    private String userId;

    private int userfront ;
    private static User currentUser;


    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public static void getInstance(int id, String nom, String prenom, int numtel, String email, String role, String image) {

    }

    public static void getInstace(int id, int cin, String nom, String prenom, String email, String password, int numTel, String profession, String roles) {

    }


    public static void logoutCurrentUser() {

        // Clear all session data
        // This could include setting the current user object to null,
        // clearing any saved authentication tokens, etc.
        currentUser = null; // Assuming currentUser is a static field holding the logged-in user
        // Optionally log this action
        System.out.println("User has been logged out.");
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setUser(User curUser) {
        currentUser = curUser;
        // You might want to do additional actions here, such as logging or setting up user-specific settings
        System.out.println("User set in session: " + curUser.getEmail());

    }
    public static User getUser() {
        return currentUser;
    }
    public void clearSession() {
        currentUser = null;
        System.out.println("Session cleared.");
    }
}
