package tn.esprit.entities;

public class RapportClient {
    private int rapportClientId;
    private String clientNom;
    private String clientPrenom;
    private String clientEmail;
    private int clientPhoneNumber;
    private int isFautif;
    private int pourcentage;
    private String rapport;
    private int clientAge;
    // Constructor and other methods...
    public RapportClient(int rapportClientId, String clientNom, String clientPrenom, String clientEmail, int clientPhoneNumber, int isFautif, int pourcentage,String rapport, int clientAge) {
        this.rapportClientId = rapportClientId;
        this.clientNom = clientNom;
        this.clientPrenom=clientPrenom;
        this.clientEmail=clientEmail;
        this.clientPhoneNumber=clientPhoneNumber;
        this.isFautif=isFautif;
        this.pourcentage=pourcentage;
        this.rapport=rapport;
        this.clientAge=clientAge;
    }

    public RapportClient() {

    }

    // Getters and setters
    public int getClientAge() {
        return clientAge;
    }
    public void setClientAge(int clientAge) {
        this.clientAge = clientAge;
    }

    public int  getisFautif(){
        return isFautif;
    }


    public int getRapportClientId() {
        return rapportClientId;
    }

    public void setRapportClientId(int rapportClientId) {
        this.rapportClientId = rapportClientId;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getClientPrenom() {
        return clientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public int getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(int clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }


    public void setIsFautif(int isFautif) {
        this.isFautif = isFautif;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    @Override
    public String toString() {
        return "RapportClient{" +
                "rapportClientId=" + rapportClientId +
                ", clientNom='" + clientNom + '\'' +
                ", clientPrenom='" + clientPrenom + '\'' +

                ", clientPhoneNumber=" + clientPhoneNumber +
                ", clientEmail=" + clientEmail +
                ", isFautif=" + isFautif +
                ", Rapport=" + rapport +
                ", Pourcentage=" + pourcentage +
                '}';
    }
}
