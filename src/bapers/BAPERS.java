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
//    @Override
//    public void start(Stage stage) throws Exception, IOException {
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = loader.load(this.getClass().getResource("/fxml/Login.fxml").openStream());
//        LoginController controller = loader.getController();
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/login.css");
//
//        stage.setTitle("User Types");
//        stage.setScene(scene);
//        stage.show();
//        
//        /*  ideas on scene switching
//        
//            make a parent window, that all scenes are triggered from.
//        
//            make an event from each scene and raise the event when a scene 
//            change is necessary.
//            
//            catch event from parent scene
//        */
//    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("git works");
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

//sha 512 hash of 'password'
//b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86
