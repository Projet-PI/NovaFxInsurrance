package tn.esprit.Interfaces;

import tn.esprit.entities.reclamation_groupe;

import java.util.List;

public interface IReclamationGroupeService {
    public void GetAll(reclamation_groupe p) ;
    public List<reclamation_groupe> afficherEntite() ;
    public void UpdateReclamationGroupe(reclamation_groupe p) ;
    public  void DeleteReclamationGroupe(int p) ;
}
