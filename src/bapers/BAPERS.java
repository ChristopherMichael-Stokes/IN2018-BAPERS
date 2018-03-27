/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.data.domain.Staff;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author chris
 */
public class BAPERS extends Application {
    
    /**
     *
     */
    public static final EntityManagerFactory EMF
            = Persistence.createEntityManagerFactory("BAPERSPU");

    /**
     *
     */
    public static Staff USER;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/bapers/userInterface/fxml/Login.fxml"));
        Scene scene = new Scene(root, 800, 450);        
        scene.getStylesheets().add("/bapers/userInterface/styles/login.css");

        primaryStage.setTitle("Staff Types");
        primaryStage.setScene(scene);
        primaryStage.show();    
    }
}
