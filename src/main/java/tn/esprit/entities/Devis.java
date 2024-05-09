package tn.esprit.entities;

import java.util.Objects;

public class Devis {
    private int id ;
    private String  name ;
    private String prenom ;
    private String email;

    private String imageR;

    // private User responsable ;

    public Devis(){


    }

    public Devis(int idR,String name, String prenom, String email ) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.id=idR;

    }








    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nomR) {
        this.name = nomR;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String Prenom) {
        this.prenom = Prenom;
    }



    public String getEmail() {
        return email;
    }

    public void setEmailR(String emailR) {
        this.email = emailR;
    }



    public String getImageR() {
        // Assuming "images" is the folder where your images are stored
        return "/images/" + imageR;
        //return imageR ;
    }

    public void setImageR(String imageR) {
        this.imageR = imageR;
    }

    @Override
    public String toString() {
        return "Devis{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devis that = (Devis) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
