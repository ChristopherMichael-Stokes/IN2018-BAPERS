/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.StaffJpaController;
import bapers.data.dataAccess.UserTypeJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import static bapers.utility.SimpleHash.getStringHash;
import java.security.NoSuchAlgorithmException;
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
        return controller.findStaff(Integer.parseInt(id)) != null;
    }

    /**
     *
     * @param id
     * @param input
     * @return
     */
    @Override
    public boolean validHash(String id, String input) {
        Staff user = controller.findStaff(Integer.parseInt(id));
        if (input == null || user == null) {
            return false;
        }
        
        String algorithm = "SHA-512", hash = null;
        try {
            hash = getStringHash(input.trim().getBytes(), algorithm);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(algorithm+" is not valid\n"+ex.getMessage());
            System.exit(-1);
        }
        return hash.equals(user.getPassphrase());
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Staff getUser(String id) {
        return controller.findStaff(Integer.parseInt(id));
    }

    /**
     *
     * @param staff
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    @Override
    public void addUser(Staff staff) 
            throws PreexistingEntityException, Exception {
        controller.create(staff);
    }

    /**
     *
     * @param id
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    @Override
    public void removeUser(String id) 
            throws IllegalOrphanException, NonexistentEntityException {
        controller.destroy(Integer.parseInt(id));
    }

    /**
     *
     * @param staff
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     * @throws java.lang.Exception
     */
    @Override
    public void updateUser(Staff staff) 
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        controller.edit(staff);
    }

    /**
     *
     * @param type
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    @Override
    public void addUserType(String type) 
            throws PreexistingEntityException, Exception {
        typeController.create(new UserType(type));
    }

    /**
     *
     * @param type
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    @Override
    public void removeUserType(String type) 
            throws IllegalOrphanException, NonexistentEntityException {
        typeController.destroy(type);
    }

    /**
     *
     * @return
     */
    @Override
    public ObservableList<Staff> getStaff() {
        return staff;
    }

    /**
     *
     * @return
     */
    @Override
    public ObservableList<UserType> getUserTypes() {
        return userTypes;
    }
}