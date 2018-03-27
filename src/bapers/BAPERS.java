/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.data.domain.Staff;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
     *
     */
    public static Staff USER;
    private static URL url
            = BAPERS.class.getResource("/bapers/userInterface/fxml/Login.fxml");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
        Resource[] resources = scanner.getResources("/bapers/userInterface/fxml/*.fxml");
        String[] options = Arrays.stream(resources).map(r -> r.getFilename()).toArray(String[]::new);
        String selection;
        Map<String, Resource> forms = new HashMap<>();
        for (int i = 0; i < resources.length; ++i) {
            forms.put(options[i], resources[i]);
        }
        selection = (String) JOptionPane.showInputDialog(
                null, "Select a form", "form",
                JOptionPane.QUESTION_MESSAGE, null,
                options, null);
        if (selection != null) {
            url = forms.get(selection).getURL();
            launch(args);
        }
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
