package tn.esprit.Interfaces;

import tn.esprit.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface IUtilisateur<T> {
    boolean Add (T t);
    ArrayList<T> getAll();
    List<T> afficher(String recherche,int i,int b);
    List<T> TriparNom();
    List<T> TriparEmail();
    List<T> Rechreche(String recherche);
    boolean Update(T t);

   // boolean Update(Connection connection, User user);

    boolean Delete(T t);
    void DeleteByID(int id);

    List<User> RechrecheUser(String recherche);
}
