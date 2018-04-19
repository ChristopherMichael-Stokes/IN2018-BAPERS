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
   
    public boolean userExists(String firstName, String surname){
        return controller.findUserEntities().stream()
                .filter(u -> (u.getFirstName()+" "+u.getSurname()).equals(firstName+" "+surname))
                .findAny().isPresent();
    }

    public UserServiceImpl() {
        controller = new UserJpaController(EMF);
    }

    @Override
    public boolean userExists(String username) {
        return controller.findUser(username) != null;
    }

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

    @Override
    public User getUser(String username) {
        return controller.findUser(username);
    }

    @Override
    public void addUser(User user) 
            throws PreexistingEntityException, Exception {
        controller.create(user);
    }

    @Override
    public void removeUser(String username) 
            throws IllegalOrphanException, NonexistentEntityException {
        controller.destroy(username);
    }

    @Override
    public void updateUser(User user) 
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        controller.edit(user);
    }

    @Override
    public ObservableList<User> getUsers() {
        return FXCollections.observableArrayList(controller.findUserEntities());
    }
}