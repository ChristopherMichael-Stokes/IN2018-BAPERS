/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.UserTypeJpaController;
import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import bapers.domain.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeJpaController controller;
    private final ObservableList<UserType> items;

    public UserTypeServiceImpl() {
        controller = new UserTypeJpaController(EMF);
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
  
}
