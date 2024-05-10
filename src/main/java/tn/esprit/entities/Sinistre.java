package tn.esprit.entities;

public class Sinistre {
    private int id;
    private int sinistre_client_id;
    private int sinistre_expert_id;
    private int is_fautif;
    private int pourcentage;
    private String rapport;

    public Sinistre(int id, int sinistre_client_id, int sinistre_expert_id, int is_fautif, int pourcentage, String rapport) {
        this.id = id;
        this.sinistre_client_id = sinistre_client_id;
        this.sinistre_expert_id = sinistre_expert_id;
        this.is_fautif = is_fautif;
        this.pourcentage = pourcentage;
        this.rapport = rapport;
    }

    public Sinistre() {
    }

    public int getId() {
        return id;
    }

    public int getSinistre_client_id() {
        return sinistre_client_id;
    }

    public int getSinistre_expert_id() {
        return sinistre_expert_id;
    }

    public int getIs_fautif() {
        return is_fautif;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public String getRapport() {
        return rapport;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSinistre_client_id(int sinistre_client_id) {
        this.sinistre_client_id = sinistre_client_id;
    }

    public void setSinistre_expert_id(int sinistre_expert_id) {
        this.sinistre_expert_id = sinistre_expert_id;
    }

    public void setIs_fautif(int is_fautif) {
        this.is_fautif = is_fautif;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    @Override
    public String toString() {
        return "Sinistre{" +
                "id=" + id +
                ", sinistre_client_id=" + sinistre_client_id +
                ", sinistre_expert_id=" + sinistre_expert_id +
                ", is_fautif=" + is_fautif +
                ", pourcentage=" + pourcentage +
                ", rapport='" + rapport + '\'' +
                '}';
    }
}
