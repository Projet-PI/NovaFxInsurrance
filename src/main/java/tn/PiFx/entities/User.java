package tn.PiFx.entities;

public class User {
    int cin,num_tel,id;
    String nom,prenom,adresse,email,password,profession,roles,reset_token,reset_token_expiration;

    //Constructeur Vide
    public User(){}
    public static User Current_User;


    //Constructeur Paramétré


    public User(int cin, String nom, String prenom, String email, String adresse,int num_tel, String password, String roles,String profession) {
        this.cin = cin;
        this.num_tel = num_tel;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.profession = profession;
    }

    public User(int id,int cin,String nom, String prenom, String email,String adresse,int num_tel, String password, String profession, String roles) {
        this.cin = cin;
        this.num_tel = num_tel;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.password = password;
        this.profession = profession;
        this.roles = roles;
    }



    public User(int id, int cin, String nom, String prenom, String adresse, String email, int numtel, String role, String profession) {
        this.cin = cin;
        this.num_tel = numtel;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.profession = profession;
        this.roles = role;
    }

    public static void setCurrent_User(User Current_User) {
        User.Current_User = Current_User;
    }


    //Getters & Setters
    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getReset_token() {
        return reset_token;
    }

    public void setReset_token(String reset_token) {
        this.reset_token = reset_token;
    }

    public String getReset_token_expiration() {
        return reset_token_expiration;
    }

    public void setReset_token_expiration(String reset_token_expiration) {
        this.reset_token_expiration = reset_token_expiration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "cin=" + cin +
                ", num_tel=" + num_tel +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profession='" + profession + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }


}

