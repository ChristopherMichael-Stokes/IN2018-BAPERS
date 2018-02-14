/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.DAO;

/**
 *
 * @author chris
 */
public interface UserDAO {
    
    public boolean userExists(String username);
    
    public boolean validHash(String username, String input);
    
}
