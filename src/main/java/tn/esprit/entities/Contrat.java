package tn.esprit.entities;

public class Contrat {
    private int id;
    private int duree;
    private  String date_de_souscription;
    private String type_couverture;

    public Contrat() {
    }

    public Contrat(int id, int duree, String dateDeSouscription, String typeDeCoverture) {
        this.id = id;
        this.duree = duree;
        this.date_de_souscription = dateDeSouscription;
        this.type_couverture = typeDeCoverture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getDate_de_souscription() {
        return date_de_souscription;
    }

    public void setDate_de_souscription(String date_de_souscription) {
        this.date_de_souscription = date_de_souscription;
    }

    public String getType_couverture() {
        return type_couverture;
    }

    public void setType_couverture(String type_couverture) {
        this.type_couverture = type_couverture;
    }

    @Override
    public String toString() {
        return
                 type_couverture;
    }
}
