/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface UserService {

    public ObservableList<Staff> getStaff();

    public ObservableList<UserType> getUserTypes();

    /**
     *
     * @param id
     * @return
     */
    public boolean userExists(String id);

    /**
     *
     * @param id
     * @param input
     * @return
     */
    public boolean validHash(String id, String input);

    /**
     *
     * @param id
     * @return
     */
    public Staff getUser(String id);

    /**
     *
     * @param staff
     */
    public void addUser(Staff staff);

    /**
     *
     * @param id
     */
    public void removeUser(String id) throws IllegalOrphanException, NonexistentEntityException;

    /**
     *
     * @param staff
     */
    public void updateUser(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception ;

    /**
     *
     * @param type
     */
    public void addUserType(String type);

    /**
     *
     * @param type
     */
    public void removeUserType(String type);
}
