package tn.esprit.entities;

import javafx.scene.control.ComboBox;

public class Assurance {
    private  int id ;
    private String nom,type ;
    private float montant ;
    private String date_debut,date_fin;
    private int contrat_id;
    private boolean favoris;


    public int getComvoid() {
        return comvoid;
    }

    public void setComvoid(int comvoid) {
        this.comvoid = comvoid;
    }

    private
    int comvoid;


    public Assurance( String nom, String type, float montant, String date_debut, String date_fin, int contrat_id, boolean favoris, int comvoid) {
        this.nom = nom;
        this.type = type;
        this.montant = montant;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.contrat_id = contrat_id;
        this.favoris = favoris;
        this.comvoid = comvoid;

    }

    public Assurance(String nom, float montant, String date_debut, String date_fin, int contrat_id) {
        System.out.println("Creating Assurance with Contrat ID: " + contrat_id); // Print the contratId
        this.nom = nom;
        this.montant = montant;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.contrat_id = contrat_id;
    }
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public float getMontant() {
        return montant;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public int getContrat_id() {
        System.out.println("Contrat ID for current Assurance: " + this.contrat_id); // Print the contratId
        return this.contrat_id;
    }
    public boolean isFavoris() {
        return favoris;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public void setContrat_id(int contrat_id) {
        System.out.println("Setting Contrat ID for Assurance: " + contrat_id); // Print the contratId
        this.contrat_id = contrat_id;
    }
    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", montant=" + montant +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", contrat_id=" + contrat_id +
                ", favoris=" + favoris +
                '}';
    }

    public Assurance(){




    }

}

