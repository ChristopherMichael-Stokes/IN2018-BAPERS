/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.DAO;

import bapers.JPA.UserJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static bapers.SimpleHash.getStringHash;
import bapers.domain.User;

/**
 *
 * @author chris
 */
public class UserDAOImpl implements UserDAO{
    
    private final EntityManagerFactory emf;
    private final UserJpaController controller;
    
    public UserDAOImpl() {
        emf = Persistence.createEntityManagerFactory("BAPERSPU");
        controller = new UserJpaController(emf);
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
}
