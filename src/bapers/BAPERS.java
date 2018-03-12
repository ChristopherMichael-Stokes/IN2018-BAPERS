/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import java.util.Arrays;
import java.util.List;
import static javafx.application.Application.launch;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author chris
 */
public class BAPERS {

    public static final EntityManagerFactory EMF
            = Persistence.createEntityManagerFactory("BAPERSPU");

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
//        new Thread(() -> EMF.createEntityManager()).start();
        EntityManager em = EMF.createEntityManager();
//        StoredProcedureQuery query = em.createNamedStoredProcedureQuery("DayShift");
//        query.setParameter("date", "2018-01-13".toCharArray());
//        List<DayShift1> result = query.getResultList();
        StoredProcedureQuery query = em.createStoredProcedureQuery("Day_shift1");
        query.registerStoredProcedureParameter("date", char[].class, ParameterMode.IN);
        query.setParameter("date", "2018-01-13".toCharArray());
        List<Object[]> result = query.getResultList();
        System.out.println("\nday shift result:");
        result.forEach((o) -> {
            System.out.println(Arrays.asList(o).toString());
        });
//        read this --> https://en.wikibooks.org/wiki/Java_Persistence/Advanced_Topics#Stored_Procedures
//        plus this --> https://docs.oracle.com/javaee/7/api/javax/persistence/NamedStoredProcedureQuery.html#resultClasses--
              
        launch(SceneController.class, args);
    }

}

//sha 512 hash of 'password'
//b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86
