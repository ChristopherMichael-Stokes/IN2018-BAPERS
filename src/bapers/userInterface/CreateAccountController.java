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

import bapers.data.domain.Address;
import bapers.data.domain.AddressPK;
import bapers.data.domain.CustomerAccount;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import static bapers.userInterface.SceneController.switchScene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class CreateAccountController implements Initializable {
    
    @FXML
    private TextField txtAccountHolder;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAddress1;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtPostcode;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtCountry;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnCreate;
    @FXML
    private Label lblCreateAccount;
    private CustomerAccountService customerDao = new CustomerAccountServiceImpl();
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCreateAccount.setText("Create Account");
        btnHome.setOnAction((event) -> switchScene(SceneController.Scenes.home));
        btnCreate.setOnAction((event) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(null);
        if(txtAccountHolder.getText().trim().equals(""))
        {
         alert.setContentText("Account Holder cannot be empty!");
         alert.showAndWait();
        }
        else
        {
            short s = 0;
            AddressPK address = new AddressPK(txtAddress1.getText(),txtPostcode.getText(),txtCity.getText(),s);
            if(!txtAddress2.getText().trim().equals(""))
            {
                Address newAddress = new Address(address,txtCountry.getText());
                newAddress.setAddressLine2(txtAddress2.getText());
                customerDao.addCustomer(new CustomerAccount(s,txtAccountHolder.getText()),newAddress);
            }
            else
            {
            customerDao.addCustomer(new CustomerAccount(s,txtAccountHolder.getText()),new Address(address,txtCountry.getText()));
            }
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Customer Account has been created!");
            alert.showAndWait();
            switchScene(SceneController.Scenes.home);
        }
        });
        
    }    
    
}
