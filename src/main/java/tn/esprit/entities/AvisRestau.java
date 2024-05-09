package tn.esprit.entities;

import java.util.Objects;

public class AvisRestau extends Devis {
    private int idR;
    private int id;
    private int devis_id_id;
    private int idUser; // Nouvelle colonne ajoutée

    private String response;
    private String commentaire;
    private String status;


   /* public AvisRestau(int id, String response, String status) {
        this.id = id;
        this.status = status;

        this.response = response;

    }
*/




    public AvisRestau(int id, String response, String status,int devis_id_id) {
        this.id = id;
        this.devis_id_id=devis_id_id;
        this.response = response;
        this.status = status;

    }
    public AvisRestau() {
    }


    public  int getDevis_id_id(){

        return devis_id_id;}
    public void setDevis_id_id(int devis_id_id ) {
        this.devis_id_id = devis_id_id;
    }


    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public  void setResponse(String Response) {
        this.response = Response;
    }

    @Override
    public String toString() {
        return "AvisRestau{" +
                "id=" + id +
                ",response='" + response + '\'' +
                ",status='" + status + '\'' +
                ",devis_id_id='" + devis_id_id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvisRestau that = (AvisRestau) o;
        return id == that.id &&
                Objects.equals(response, that.response) &&
                Objects.equals(status, that.status)&&
                Objects.equals(devis_id_id, that.devis_id_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,response,status,devis_id_id);
    }
}
