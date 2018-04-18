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
import bapers.data.domain.CustomerAccount;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.TaskService;
import bapers.service.TaskServiceImpl;
import static bapers.userInterface.SceneController.switchScene;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageCustomerAccountController implements Initializable {

    @FXML
    private TextField txtVariablePercentage;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtPostCode;
    @FXML
    private TextField txtLandline;
    @FXML
    private TextField txtFlexiblePercentage;
    @FXML
    private TextField txtFixedPercentage;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtAddressLine2;
    @FXML
    private TextField txtAddressLine1;
    @FXML
    private TextField txtAccountName;
    @FXML
    private ScrollPane scpFlexible;
    @FXML
    private ScrollPane scpAccounts;
    @FXML
    private RadioButton rbVariable;
    @FXML
    private RadioButton rbFlexible;
    @FXML
    private RadioButton rbFixed;
    @FXML
    private ListView lsvTasks;
    @FXML
    private ListView lsvFlexible;
    @FXML
    private ListView lsvAccounts;
    @FXML
    private Label lblRegion;
    @FXML
    private Label lblPostcode;
    @FXML
    private Label lblLandline;
    @FXML
    private Label lblHomeCustomerAccounts;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblCustomerAccounts;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblAddress2;
    @FXML
    private Label lblAddress1;
    @FXML
    private Label lblAccountName;
    @FXML
    private ComboBox cbbVariable;
    @FXML
    private Button btnUpgrade;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRemoveAccount;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnDowngrade;
    @FXML
    private Button btnApplyDiscount;
    @FXML
    private Button btnActivateAccount;
    private CustomerAccountService customerAccountServiceDao = new CustomerAccountServiceImpl();
    private TaskService taskServiceDao = new TaskServiceImpl();
    private final ToggleGroup discountType = new ToggleGroup();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbFixed.setToggleGroup(discountType);
        rbVariable.setToggleGroup(discountType);
        rbFlexible.setToggleGroup(discountType);
        lblRegion.setText("Region");
        lblPostcode.setText("Post Code");
        lblLandline.setText("Landline");
        lblHomeCustomerAccounts.setText("Home>Customer Accounts");
        lblEmail.setText("Email");
        lblCustomerAccounts.setText("Customer Account");
        lblCity.setText("City");
        lblAddress2.setText("Address2");
        lblAddress1.setText("Address1");
        lblAccountName.setText("Account Number");
        btnHome.setOnAction((event) -> switchScene(SceneController.Scenes.home));
        btnSearch.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle(null);
            if (txtSearch.getText().trim().equals("")) {
                alert.setContentText("Search bar cannot be empty!");
                alert.showAndWait();
            } else {
                ObservableList<String> searchObservableList;
                List<String> searchList = new ArrayList<String>();
                customerAccountServiceDao.getCustomerAccounts().forEach((o)
                        -> {
                    if (o.getAccountHolderName().contains(txtSearch.getText().trim())) {
                        searchList.add(o.getAccountHolderName());
                    }
                });
                if (searchList.isEmpty()) {
                    lsvAccounts.setItems(null);
                    alert.setContentText("No entry for the word" + "(" + txtSearch.getText() + ")");
                    alert.showAndWait();
                } else {
                    searchObservableList = FXCollections.observableArrayList(searchList);
                    lsvAccounts.setItems(searchObservableList);
                    lsvAccounts.setOrientation(Orientation.VERTICAL);
                }
            }
        });
        lsvAccounts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldvalue, String newvalue) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o)
                        -> {
                    if (o.getAccountHolderName() == ov.getValue()) {
                        turnAllTextBlank();
                        getAllText(o);
                    }
                });
            }
        });
        ObservableList<Integer> taskIDOList;
        List<Integer> taskIDList = new ArrayList<Integer>();
        taskServiceDao.getTasks().forEach((o)->{taskIDList.add(o.getTaskId());});
        taskIDOList = FXCollections.observableArrayList(taskIDList);
        cbbVariable.setItems(taskIDOList);
    }

    private void turnAllTextBlank() {
        txtRegion.clear();
        txtPostCode.clear();
        txtCity.clear();
        txtAddressLine1.clear();
        txtAddressLine2.clear();
        txtLandline.clear();
        txtEmail.clear();
        txtAccountName.clear();

    }

    private void getAllText(CustomerAccount o) {
        List<Address> al = o.getAddressList();
        if (!al.isEmpty()) {
            txtRegion.setText(al.get(0).getRegion());
            txtPostCode.setText(al.get(0).getAddressPK().getPostcode());
            txtCity.setText(al.get(0).getAddressPK().getCity());
            txtAddressLine1.setText(al.get(0).getAddressPK().getAddressLine1());
            txtAddressLine2.setText(al.get(0).getAddressLine2());
        }
        
        txtLandline.setText(o.getLandline());
        txtEmail.setText(o.getEmail());
        txtAccountName.setText(o.getAccountHolderName());
    }
}
