/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.User;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface UserService {
    
    public boolean userExists(String firstName, String surname);

    /**
     *
     * @return
     */
    public ObservableList<User> getUsers();

    /**
     *
     * @param username
     * @return
     */
    public boolean userExists(String username);

    /**
     *
     * @param username
     * @param input
     * @return
     */
    public boolean validHash(String username, String input);

    /**
     *
     * @param username
     * @return
     */
    public User getUser(String username);

    /**
     *
     * @param user
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    public void addUser(User user) throws PreexistingEntityException, Exception;

    /**
     *
     * @param username
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    public void removeUser(String username) throws IllegalOrphanException, NonexistentEntityException;

    /**
     *
     * @param user
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     * @throws java.lang.Exception
     */
    public void updateUser(User user) throws IllegalOrphanException, NonexistentEntityException, Exception;
}
