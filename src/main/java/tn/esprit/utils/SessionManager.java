package tn.esprit.utils;

import tn.esprit.entities.User;

public class SessionManager {
    private static SessionManager instance;
    private static User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logoutCurrentUser() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getEmail() + " is logging out.");
        }
        currentUser = null;
        System.out.println("User has been logged out.");
    }
}
