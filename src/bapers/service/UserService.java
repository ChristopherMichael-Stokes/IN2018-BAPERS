/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.domain.Staff;

/**
 *
 * @author chris
 */
public interface UserService {    

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
    public void removeUser(String id);
    
    /**
     *
     * @param staff
     */
    public void updateUser(Staff staff);
    
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
