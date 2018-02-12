/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import bapers.service.UserTypeDao;

/**
 *
 * @author chris
 */
public class BAPERS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        UserTypeDao utd = new UserTypeDao();
        System.out.println("all user types:");
        utd.allTypes().forEach(s -> System.out.println(s));
    }
    
}
