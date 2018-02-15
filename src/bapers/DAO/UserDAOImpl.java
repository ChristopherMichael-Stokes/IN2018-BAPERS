/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.DAO;

import static bapers.BAPERS.EMF;
import bapers.JPA.UserJpaController;
import static bapers.SimpleHash.getStringHash;
import bapers.domain.User;

/**
 *
 * @author chris
 */
public class UserDAOImpl implements UserDAO{
    
    private final UserJpaController controller;
    
    public UserDAOImpl() {
        controller = new UserJpaController(EMF);
    }
    
    @Override
    public boolean userExists(String username) {
        return controller.findUser(username) != null;
    }

    @Override
    public boolean validHash(String username, String input) {
        User user = controller.findUser(username);
        if (input == null || user == null)
            return false;
        return getStringHash(input.trim().getBytes(), "SHA-512")
                .equals(user.getPassphrase());
    }   

    @Override
    public User getUser(String username) {
        return controller.findUser(username);
    }
}
