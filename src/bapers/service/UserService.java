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
    
    /**
     *
     * @param firstName of user
     * @param surname of user
     * @return true if user is present in the system
     */
    public boolean userExists(String firstName, String surname);

    /**
     *
     * @return list of all users in the system
     */
    public ObservableList<User> getUsers();

    /**
     *
     * @param username of user to find
     * @return true if user is present in the system
     */
    public boolean userExists(String username);

    /**
     * checks whether the hash of the input matches the users stored password
     * @param username of the user
     * @param input password to check
     * @return true if passwords match
     */
    public boolean validHash(String username, String input);

    /**
     *
     * @param username of user to find from the system
     * @return user from the database
     */
    public User getUser(String username);

    /**
     *
     * @param user to add to the system
     * @throws PreexistingEntityException if user already exists
     * @throws java.lang.Exception if database connection fails
     */
    public void addUser(User user) throws PreexistingEntityException, Exception;

    /**
     *
     * @param username of user to remove from the system
     * @throws IllegalOrphanException if removing the user invalidates foreign keys
     * @throws NonexistentEntityException if user does not exist in the system
     */
    public void removeUser(String username) throws IllegalOrphanException, NonexistentEntityException;

    /**
     *
     * @param user to update
     * @throws IllegalOrphanException if updating the user invalidates foreign keys
     * @throws NonexistentEntityException if the user does not exist in the system
     * @throws java.lang.Exception if database connection fails
     */
    public void updateUser(User user) throws IllegalOrphanException, NonexistentEntityException, Exception;
}
