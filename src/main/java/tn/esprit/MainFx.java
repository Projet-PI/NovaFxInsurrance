package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainFx extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/afficherbackassurance.fxml"));
        try {
            Parent root = loader.load();
            Scene scene =new Scene(root);
            primaryStage.setTitle("Nova-Assurance");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    }

