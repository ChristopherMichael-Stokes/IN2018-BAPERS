/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.DAO;

import bapers.JPA.UserTypeJpaController;
import bapers.JPA.exceptions.IllegalOrphanException;
import bapers.JPA.exceptions.NonexistentEntityException;
import bapers.JPA.exceptions.PreexistingEntityException;
import bapers.domain.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author chris
 */
public class UserTypeDAOImpl implements UserTypeDAO {

    private final EntityManagerFactory emf;
    private final UserTypeJpaController controller;
    private ObservableList<UserType> items;

    public UserTypeDAOImpl() {
        emf = Persistence.createEntityManagerFactory("BAPERSPU");
        controller = new UserTypeJpaController(emf);
        items = FXCollections.observableArrayList(controller.findUserTypeEntities());
    }

    private void updateUserType() {
        items.clear();
        items.addAll(controller.findUserTypeEntities());
    }

    @Override
    public void addUserType(String text) throws PreexistingEntityException, Exception {
        if (text != null) {
            controller.create(new UserType(text.trim()));
            updateUserType();
        }
    }

    @Override
    public void removeUserType(String text) throws IllegalOrphanException, NonexistentEntityException {
        if (text != null) {
            controller.destroy(text.trim());
            updateUserType();
        }
    }

    @Override
    public ObservableList<UserType> getItems() {
        updateUserType();
        return items;
    }

    @Override
    public void setItems(ObservableList<UserType> items) {
        this.items = items;
    }

}
