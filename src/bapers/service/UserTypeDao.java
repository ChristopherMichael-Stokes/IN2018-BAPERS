/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.UserTypeJpaController;
import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.domain.UserType;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author chris
 */
public class UserTypeDao {
    private final UserTypeJpaController userTypeController;
    public final EntityManagerFactory emf;
    
    public UserTypeDao() {
        emf = Persistence.createEntityManagerFactory("BAPERSPU");
        userTypeController = new UserTypeJpaController(emf);
    }
    
    public void addType(String typeName) throws Exception {
        userTypeController.create(new UserType(typeName));
    }
    
    public void removeType(String typeName) throws NonexistentEntityException, IllegalOrphanException {
        userTypeController.destroy(typeName);
    }
    
    public List<UserType> allTypes() {
        return userTypeController.findUserTypeEntities();
    }
    
}
