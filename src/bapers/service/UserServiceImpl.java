/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.StaffJpaController;
import static bapers.SimpleHash.getStringHash;
import bapers.data.dataAccess.UserTypeJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public class UserServiceImpl implements UserService {

    private final StaffJpaController controller;
    private final UserTypeJpaController typeController;
    private final ObservableList<Staff> staff;
    private final ObservableList<UserType> userTypes;

    /**
     *
     */
    public UserServiceImpl() {
        controller = new StaffJpaController(EMF);
        typeController = new UserTypeJpaController(EMF);
        staff = FXCollections.observableArrayList(controller.findStaffEntities());
        userTypes = FXCollections.observableArrayList(typeController.findUserTypeEntities());
    }

    /**
     * determines whether staff member is in system
     *
     * @param id of staff member
     * @return true if the staff member exists in the system
     */
    @Override
    public boolean userExists(String id) {
        return controller.findStaff(id) != null;
    }

    /**
     *
     * @param id
     * @param input
     * @return
     */
    @Override
    public boolean validHash(String id, String input) {
        Staff user = controller.findStaff(id);
        if (input == null || user == null) {
            return false;
        }
        String hash = getStringHash(input.trim().getBytes(), "SHA-512");
        return hash.equals(user.getPassphrase());
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Staff getUser(String id) {
        return controller.findStaff(id);
    }

    /**
     *
     * @param staff
     */
    @Override
    public void addUser(Staff staff) throws PreexistingEntityException, Exception {
        controller.create(staff);
    }

    /**
     *
     * @param id
     */
    @Override
    public void removeUser(String id) throws IllegalOrphanException, NonexistentEntityException {
        controller.destroy(id);
    }

    /**
     *
     * @param staff
     */
    @Override
    public void updateUser(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception {
        controller.edit(staff);
    }

    /**
     *
     * @param type
     */
    @Override
    public void addUserType(String type) throws PreexistingEntityException, Exception {
        typeController.create(new UserType(type));
    }

    /**
     *
     * @param type
     */
    @Override
    public void removeUserType(String type) throws IllegalOrphanException, NonexistentEntityException {
        typeController.destroy(type);
    }

    @Override
    public ObservableList<Staff> getStaff() {
        return staff;
    }

    @Override
    public ObservableList<UserType> getUserTypes() {
        return userTypes;
    }
}
