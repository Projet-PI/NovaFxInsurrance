package tn.esprit.utils;

public class SessionManager {
    private static SessionManager instance;
    private String userId;

    private int userfront ;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserFront(int userfront) {
        this.userfront = userfront;
    }
    public int getUserFront() {
        return userfront;
    }


    public String getUserId()
    {
        return userId;
    }
    public void cleanUserSessionAdmin() {
        userId= " " ;
    }
    public void cleanUserSessionFront() {
        userfront= 0;
    }

}
