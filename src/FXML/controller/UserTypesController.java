/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller;

import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.domain.UserType;
import bapers.service.UserTypeDao;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class UserTypesController implements Initializable {

    @FXML
    private ListView<UserType> lsvUserTypes;
    private final ObservableList<UserType> items = 
            FXCollections.observableArrayList();
    private final UserTypeDao userTypeDAO = new UserTypeDao();
    @FXML
    private Label lblUserType;
    @FXML
    private TextField txtUserType;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lsvUserTypes.setItems(items);
        items.addAll(userTypeDAO.allTypes());
        
        lsvUserTypes.setCellFactory(new Callback<ListView<UserType>, ListCell<UserType>> () {
            @Override
            public ListCell<UserType> call(ListView<UserType> param) {
                ListCell<UserType> listCell = new ListCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        if (item != null) {
                            UserType userType = (UserType) item;
                            setText(userType.getType());
                        } else {
                            setText("");
                        }
                    }
                };
                return listCell;
            }            
        });
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!txtUserType.getText().trim().isEmpty()) {
                    try {
                        UserType newUserType = new UserType(txtUserType.getText());
                        userTypeDAO.addType(txtUserType.getText());
                        items.add(newUserType);
                    } catch (Exception ex) {
                        txtUserType.clear();
                    }
                }
            }            
        });
        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserType selectedItem = lsvUserTypes.getSelectionModel().getSelectedItem();
                if (selectedItem!=null) {
                    try {
                        userTypeDAO.removeType(selectedItem.getType());
                    } catch (IllegalOrphanException | NonexistentEntityException e) {
                    }
                }
                items.remove(selectedItem);
            }
            
        });
    }    
    
}
