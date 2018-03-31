/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface UserService {

    /**
     *
     * @return
     */
    public ObservableList<Staff> getStaff();

    /**
     *
     * @return
     */
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
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    public void addUser(Staff staff) throws PreexistingEntityException, Exception;

    /**
     *
     * @param id
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    public void removeUser(String id) throws IllegalOrphanException, NonexistentEntityException;

    /**
     *
     * @param staff
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     * @throws java.lang.Exception
     */
    public void updateUser(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception;

    /**
     *
     * @param type
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    public void addUserType(String type) throws PreexistingEntityException, Exception;

    /**
     *
     * @param type
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    public void removeUserType(String type) throws IllegalOrphanException, NonexistentEntityException;
}
