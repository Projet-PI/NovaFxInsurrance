package tn.esprit.Interfaces;

import tn.esprit.entities.Contrat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Service <T>{
    public void add(T t) throws SQLException;

    void add(Contrat c, Connection conn) throws SQLException;

    public List<T> show() throws SQLException;
    public void delete(int id) throws SQLException;
    public void edit(T t) throws SQLException;
    public void update(T t) throws SQLException;





}
