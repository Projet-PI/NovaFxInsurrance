module Gestion.Assurancefx {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires jdk.compiler;
    requires twilio;
    requires okhttp3;
    requires com.google.gson;
    requires org.apache.poi.ooxml;
    requires facebook4j.core;
    exports tn.esprit.controles;
    opens tn.esprit.controles to javafx.fxml;
    opens tn.esprit.entity to javafx.base;

}