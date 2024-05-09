package tn.esprit.services;

import tn.esprit.entities.RapportClient;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    List<RapportClient> afficher() throws SQLException;

    public  void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public List<T> getAll();
    public T getOneById(int id) ;

    List<RapportClient> afficherMaListe(String userName) throws SQLException;
}
