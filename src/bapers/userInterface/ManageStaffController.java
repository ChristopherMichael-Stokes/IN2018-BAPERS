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

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.LocationJpaController;
import bapers.data.dataAccess.UserJpaController;
import bapers.data.domain.Location;
import bapers.data.domain.User;
import bapers.service.UserService;
import bapers.service.UserServiceImpl;
import static bapers.userInterface.SceneController.switchScene;
import bapers.utility.SimpleHash;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageStaffController implements Initializable {

    @FXML
    private Button btnHome;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private ComboBox<String> cmbUserType;
    @FXML
    private ComboBox<String> cmbUsersLocation;
    @FXML
    private Button btnCreateUser;
    @FXML
    private ComboBox<String> cbcSelectUser;
    @FXML
    private ListView<String> lvUser;
    @FXML
    private Button btnDelete;
    private UserJpaController uJpa = new UserJpaController(EMF);
    private LocationJpaController lJpa = new LocationJpaController(EMF);
    private UserService us = new UserServiceImpl();

    /**
     * Initializes the controller class.
     *
     * @param url is the directory used to retrieve the .fxml files which contain the gui
     * @param rb
     */
    //Creating a new user type, inserting their details including name, surname, usertype and the users location
    // a username and password must also be created for the staff
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnHome.setOnAction((event) -> switchScene(SceneController.Scenes.home));
        btnCreateUser.setOnAction((event) -> {
            if (isEmpty(txtUsername)||isEmpty(txtPassword)||cmbUserType.getSelectionModel().isEmpty()) {
                alert("Please fill in all the require fields!");
            }
            else{
                
                User newUser = new User();
                newUser.setUsername(txtUsername.getText());
                if(!isEmpty(txtFirstName)){
                newUser.setFirstName(txtFirstName.getText());
                }
                if(!isEmpty(txtSurname)){
                newUser.setSurname(txtSurname.getText());
            }
                if(cmbUserType.getValue().equals("office manager")){
                    newUser.setType((short)0);
                }
                if(cmbUserType.getValue().equals("shift manager")){
                    newUser.setType((short)1);
                }
                if(cmbUserType.getValue().equals("receptionist")){
                    newUser.setType((short)2);
                }
                if(cmbUserType.getValue().equals("technician")){
                    newUser.setType((short)3);
                    if(cmbUsersLocation.getSelectionModel().isEmpty()){
                        alert("Location need to be select for technician!");
                      
                    }
                    else if(cmbUsersLocation.getValue().equals("copy room")){
                        newUser.setFkLocation(lJpa.findLocation(cmbUsersLocation.getValue()));
                    }
                    else if(cmbUsersLocation.getValue().equals("development area")){
                        newUser.setFkLocation(lJpa.findLocation(cmbUsersLocation.getValue()));
                    }
                    else if(cmbUsersLocation.getValue().equals("packing department")){
                        newUser.setFkLocation(lJpa.findLocation(cmbUsersLocation.getValue()));
                    }
                    else if(cmbUsersLocation.getValue().equals("finishing room")){
                        newUser.setFkLocation(lJpa.findLocation(cmbUsersLocation.getValue()));
                    }
                    
                }
                
                try {
                    newUser.setPassphrase(SimpleHash.getStringHash(txtPassword.getText()));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(ManageStaffController.class.getName()).log(Level.SEVERE, null, ex);
                }
                us.getUsers().forEach((o)->{
                    if(o.getUsername().equals(newUser.getUsername()))
                    {
                        alert("User "+newUser.getUsername()+" alredy exist!");
                    }
                    else{
                        try {
                            uJpa.create(newUser);
                        } catch (Exception ex) {
                            Logger.getLogger(ManageStaffController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        information("User Account is created!");
                    }
                
                });
 
                
            }
        });
        List<String> userList = new ArrayList<String>();
        ObservableList<String> oUserList;
        userList.add("office manager");
        userList.add("shift manager");
        userList.add("receptionist");
        userList.add("technician");
        oUserList = FXCollections.observableArrayList(userList);
        List<String> locationList = new ArrayList<String>();
        ObservableList<String> oLocationList;
        locationList.add("copy room");
        locationList.add("development area");
        locationList.add("packing department");
        locationList.add("finishing room");
        oLocationList = FXCollections.observableArrayList(locationList);
        cmbUserType.setItems(oUserList);
        cmbUsersLocation.setItems(oLocationList);
    }

    private boolean isEmpty(TextField tf) {
        return tf.getText().trim().equals("");
    }
    //Alert messsage if information is not inputted in the textfields
    private void alert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    //alert message sent if all information completed
    private void information(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
