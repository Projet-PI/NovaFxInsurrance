package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.entities.AvisRestau;
import tn.esprit.entities.Devis;

public class UnAvis  {

    @FXML
    private Label commentaire;
    @FXML
    private Label status;

    private AvisRestau a ;

    private Devis r ;



    public void setData(AvisRestau a){

        this.a=a ;
        //this.a.setId(r.getId());
        commentaire.setText(a.getResponse());
        status.setText(a.getStatus());

    }

}
