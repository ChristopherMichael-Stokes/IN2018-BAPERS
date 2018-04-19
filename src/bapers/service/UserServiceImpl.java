/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.UserJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.User;
import static bapers.utility.SimpleHash.getStringHash;
import java.security.NoSuchAlgorithmException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public class UserServiceImpl implements UserService {

    private final UserJpaController controller;
    
    /**
     *
     * @param firstName
     * @param surname
     * @return
     */
    public boolean userExists(String firstName, String surname){
        return controller.findUserEntities().stream()
                .filter(u -> (u.getFirstName()+" "+u.getSurname()).equals(firstName+" "+surname))
                .findAny().isPresent();
    }

    

    /**
     *
     */
    public UserServiceImpl() {
        controller = new UserJpaController(EMF);
    }

    /**
     * determines whether staff member is in system
     *
     * @param username of user
     * @return true if the staff member exists in the system
     */
    @Override
    public boolean userExists(String username) {
        return controller.findUser(username) != null;
    }

    /**
     *
     * @param username
     * @param input
     * @return
     */
    @Override
    public boolean validHash(String username, String input) {
        User user = controller.findUser(username);
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
     * @param username
     * @return
     */
    @Override
    public User getUser(String username) {
        return controller.findUser(username);
    }

    /**
     *
     * @param user
     * @throws bapers.data.dataAccess.exceptions.PreexistingEntityException
     * @throws java.lang.Exception
     */
    @Override
    public void addUser(User user) 
            throws PreexistingEntityException, Exception {
        controller.create(user);
    }

    /**
     *
     * @param username
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    @Override
    public void removeUser(String username) 
            throws IllegalOrphanException, NonexistentEntityException {
        controller.destroy(username);
    }

    /**
     *
     * @param user
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     * @throws java.lang.Exception
     */
    @Override
    public void updateUser(User user) 
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        controller.edit(user);
    }

   /**
     *
     * @return
     */
    @Override
    public ObservableList<User> getUsers() {
        return FXCollections.observableArrayList(controller.findUserEntities());
    }
}