/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.service.UserServiceImpl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import bapers.service.UserService;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblOut;

    private final UserService DAO = new UserServiceImpl();
    private int attempts = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setOnKeyPressed((event) -> { 
            if (event.getCode() == KeyCode.ENTER)
                txtPassword.requestFocus();
        });
        txtPassword.setOnKeyPressed((event) -> { 
            if (event.getCode() == KeyCode.ENTER)
                login();
        });        
        btnLogin.setOnAction((event) -> {
            login();
        });
    }

    private void login() {        
        lblOut.setText("begin request " + (++attempts));

        if (DAO.userExists(txtId.getText())
                && DAO.validHash(txtId.getText(),
                        txtPassword.getText().trim())) {

            lblOut.setText("valid input");
            attempts = 0;
            bapers.SceneController.USER = DAO.getUser(txtId.getText());
            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/UserType.fxml"));
                Scene userTypeScene = new Scene(root);
                Stage stage = (Stage)btnLogin.getScene().getWindow();
//                Stage stage = (Stage) ((Node) this.getSource()).getScene().getWindow();
                stage.setScene(userTypeScene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        } else {
            lblOut.setText("invalid id or password\n"
                    + "login attempts: " + attempts);
            txtPassword.setText("");
            txtId.setText("");
            txtId.requestFocus();
//                stage.initModality(Modality.APPLICATION_MODAL);
//                stage.showAndWait();

        }
        lblOut.autosize();
    }
}