/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.DAO;

import bapers.JPA.exceptions.IllegalOrphanException;
import bapers.JPA.exceptions.NonexistentEntityException;
import bapers.JPA.exceptions.PreexistingEntityException;
import bapers.domain.UserType;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface UserTypeDAO {

    public void addUserType(String text) throws Exception, PreexistingEntityException;

    public void removeUserType(String text) throws IllegalOrphanException, NonexistentEntityException;

    public ObservableList<UserType> getItems();

    public void setItems(ObservableList<UserType> items);

}
