/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.StaffJpaController;
import static bapers.SimpleHash.getStringHash;
import bapers.data.domain.Staff;

/**
 *
 * @author chris
 */
public class UserServiceImpl implements UserService{
    
    private final StaffJpaController controller;
    
    /**
     *
     */
    public UserServiceImpl() {
        controller = new StaffJpaController(EMF);
    }
    
    /**
     * determines whether staff member is in system
     * @param id of staff member
     * @return true if the staff member exists in the system
     */
    @Override
    public boolean userExists(String id) {
        return controller.findStaff(id) != null;
    }

    /**
     *
     * @param id
     * @param input
     * @return
     */
    @Override
    public boolean validHash(String id, String input) {
        Staff user = controller.findStaff(id);
        if (input == null || user == null)
            return false;
        String hash = getStringHash(input.trim().getBytes(), "SHA-512");
        return hash.equals(user.getPassphrase());
    }   

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Staff getUser(String id) {
        return controller.findStaff(id);
    }

    /**
     *
     * @param staff
     */
    @Override
    public void addUser(Staff staff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param id
     */
    @Override
    public void removeUser(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param staff
     */
    @Override
    public void updateUser(Staff staff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param type
     */
    @Override
    public void addUserType(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param type
     */
    @Override
    public void removeUserType(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
