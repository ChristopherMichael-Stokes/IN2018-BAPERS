/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.DAO.UserDAO;
import bapers.DAO.UserDAOImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblOut;

    private final UserDAO DAO = new UserDAOImpl();
    private int attempts = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLogin.setOnAction((event) -> {
        lblOut.setText("begin request "+(attempts++));

            if (DAO.userExists(txtUsername.getText())
                    && DAO.validHash(txtUsername.getText(),
                            txtPassword.getText().trim())) {
                
                lblOut.setText("valid input");
                attempts = 0;
            } else {
                lblOut.setText("invalid username or password\n"
                        + "login attempts: " + (attempts++));
            }
            lblOut.autosize();

        });
    }
}
