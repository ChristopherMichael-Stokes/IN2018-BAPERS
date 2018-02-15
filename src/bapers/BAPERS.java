/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author chris
 */
public class BAPERS extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/Login.fxml"));
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/login.css");

        stage.setTitle("User Types");
        stage.setScene(scene);
        stage.show();
        
        /*  ideas on scene switching
        
            make a parent window, that all scenes are triggered from.
        
            make an event from each scene and raise the event when a scene 
            change is necessary.
            
            catch event from parent scene
        */
    }
        
        
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
//        String pass = "password";
//        String sha = getStringHash(pass.getBytes(), "SHA-512"),
//                md2 = getStringHash(pass.getBytes(), "MD2");
//        System.out.println(sha+" "+sha.length());
//        System.out.println(md2+" "+md2.length());
    }
    
    
}

//first user

//insert into user (username, first_name, surname, passphrase, fk_type)
//values ('boss','chris','theboss','b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86',
//(select type from user_type where type = "office manager"));
//select user.* from user;