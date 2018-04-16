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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageCustomerAccountController implements Initializable {

    @FXML
    private Button btnSearch;
    @FXML
    private ScrollPane scpAccounts;
    @FXML
    private ListView<?> lsvAccounts;
    @FXML
    private Button btnUpgrade;
    @FXML
    private Button btnDowngrade;
    @FXML
    private Button btnActivateAccount;
    @FXML
    private Button btnRemoveAccount;
    @FXML
    private Button btnHome;
    @FXML
    private TextField txtAccountName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtLandline;
    @FXML
    private TextField txtAddressLine1;
    @FXML
    private TextField txtAddressLine2;
    @FXML
    private TextField txtPostCode;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtCity;
    @FXML
    private VBox rbFlexible;
    @FXML
    private RadioButton rbVariable;
    @FXML
    private ListView<?> lsvTasks;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtFlexiblePercentage;
    @FXML
    private ScrollPane scpFlexible;
    @FXML
    private ListView<?> lsvFlexible;
    @FXML
    private RadioButton rbFixed;
    @FXML
    private TextField txtFixedPercentage;
    @FXML
    private Button btnApplyDiscount;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
