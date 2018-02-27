/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.StaffJpaController;
import static bapers.SimpleHash.getStringHash;
import bapers.domain.Staff;

/**
 *
 * @author chris
 */
public class UserServiceImpl implements UserService{
    
    private final StaffJpaController controller;
    
    public UserServiceImpl() {
        controller = new StaffJpaController(EMF);
    }
    
    @Override
    public boolean userExists(String id) {
        return controller.findStaff(id) != null;
    }

    @Override
    public boolean validHash(String id, String input) {
        Staff user = controller.findStaff(id);
        if (input == null || user == null)
            return false;
        String hash = getStringHash(input.trim().getBytes(), "SHA-512");
        System.out.println(hash);
        return hash.equals(user.getPassphrase());
    }   

    @Override
    public Staff getUser(String id) {
        return controller.findStaff(id);
    }
}
