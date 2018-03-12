/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.service;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.UserType;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface UserTypeService {

    public void addUserType(String text) throws Exception, PreexistingEntityException;

    public void removeUserType(String text) throws IllegalOrphanException, NonexistentEntityException;

    public ObservableList<UserType> getItems();

}
