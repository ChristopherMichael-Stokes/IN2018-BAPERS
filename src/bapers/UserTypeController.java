/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.DAO.UserTypeDAO;
import bapers.DAO.UserTypeDAOImpl;
import bapers.JPA.exceptions.IllegalOrphanException;
import bapers.JPA.exceptions.NonexistentEntityException;
import bapers.JPA.exceptions.PreexistingEntityException;
import bapers.domain.UserType;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class UserTypeController implements Initializable {

    @FXML
    private TextField txtUserType;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private ListView<UserType> lsvUserTypes;

    private static final UserTypeDAO DAO = new UserTypeDAOImpl();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lsvUserTypes.setItems(DAO.getItems());

        lsvUserTypes.setCellFactory(new Callback<ListView<UserType>, ListCell<UserType>>() {
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

        btnAdd.setOnAction((ActionEvent event) -> {
            try {
                DAO.addUserType(txtUserType.getText());
                txtUserType.clear();
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(UserTypeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UserTypeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnRemove.setOnAction((ActionEvent event) -> {
            try {
                DAO.removeUserType(lsvUserTypes.getSelectionModel().getSelectedItem().getType());
            } catch (IllegalOrphanException | NonexistentEntityException ex) {
                Logger.getLogger(UserTypeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

}
