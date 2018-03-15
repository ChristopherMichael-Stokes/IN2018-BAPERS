/*
 * Copyright (c) 2018, chris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package bapers.userInterface;

import bapers.service.UserService;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

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
        txtPassword.setOnKeyReleased((event) -> lblOut.setText(txtPassword.getText()));
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
            bapers.BAPERS.USER = DAO.getUser(txtId.getText());
            System.out.println(bapers.BAPERS.USER.getFirstName());
//            try {
//                Parent root = FXMLLoader.load(this.getClass().getResource("/bapers/userInterface/fxml/UserType.fxml"));
//                Scene userTypeScene = new Scene(root);
//                Stage stage = (Stage)btnLogin.getScene().getWindow();
////                Stage stage = (Stage) ((Node) this.getSource()).getScene().getWindow();
//                stage.setScene(userTypeScene);
//                stage.show();
//            } catch (IOException ex) {
//                ex.printStackTrace(System.err);
//                System.exit(-1);
//            }
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