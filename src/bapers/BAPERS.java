/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.data.domain.User;
import bapers.service.UserServiceImpl;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.swing.JOptionPane;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

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
     * constant for testing and debugging purposes.  Should be set to false
     * for normal user operation
     */
    public static final boolean TESTING = true;

    /**
     * the logged in user
     */
    public static User USER;

    /**
     * the main window for the application
     */
    public static Stage primaryStage;
    private static URL url
            = BAPERS.class.getResource("/bapers/userInterface/fxml/Login.fxml");

    /**
     * @param args the command line arguments
     * @throws java.io.IOException if the specified fxml form cannot be opened
     */
    public static void main(String[] args) throws IOException {
        if (!TESTING) {
            launch(args);
        } else {   
            //finds all .fxml files and prompts the user to select one from a list.
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            Resource[] resources = scanner.getResources("/bapers/userInterface/fxml/*.fxml");
            
            String[] options = Arrays.stream(resources).map(r -> r.getFilename()).toArray(String[]::new);
            Map<String, Resource> forms = new HashMap<>();
            for (int i = 0; i < resources.length; ++i) {
                forms.put(options[i], resources[i]);
            }
            
            USER = new UserServiceImpl().getUser("Manager");
            String selection = (String) JOptionPane.showInputDialog(
                    null, "Select a form", "form",
                    JOptionPane.QUESTION_MESSAGE, null,
                    options, null);
            if (selection != null) {
                url = forms.get(selection).getURL();
                launch(args);
            }
        }
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BAPERS.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
