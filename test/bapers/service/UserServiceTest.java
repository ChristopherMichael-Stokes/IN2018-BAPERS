/*
 * Copyright (c) 2018, chris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package bapers.service;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class UserServiceTest {
    
    public UserServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStaff method, of class UserService.
     */
    @Test
    public void testGetStaff() {
        System.out.println("getStaff");
        UserService instance = new UserServiceImpl();
        ObservableList<Staff> expResult = null;
        ObservableList<Staff> result = instance.getStaff();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserTypes method, of class UserService.
     */
    @Test
    public void testGetUserTypes() {
        System.out.println("getUserTypes");
        UserService instance = new UserServiceImpl();
        ObservableList<UserType> expResult = null;
        ObservableList<UserType> result = instance.getUserTypes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of userExists method, of class UserService.
     */
    @Test
    public void testUserExists() {
        System.out.println("userExists");
        String id = "";
        UserService instance = new UserServiceImpl();
        boolean expResult = false;
        boolean result = instance.userExists(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validHash method, of class UserService.
     */
    @Test
    public void testValidHash() {
        System.out.println("validHash");
        String id = "";
        String input = "";
        UserService instance = new UserServiceImpl();
        boolean expResult = false;
        boolean result = instance.validHash(id, input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class UserService.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        String id = "";
        UserService instance = new UserServiceImpl();
        Staff expResult = null;
        Staff result = instance.getUser(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addUser method, of class UserService.
     */
    @Test
    public void testAddUser() throws Exception {
        System.out.println("addUser");
        Staff staff = null;
        UserService instance = new UserServiceImpl();
        instance.addUser(staff);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeUser method, of class UserService.
     */
    @Test
    public void testRemoveUser() throws Exception {
        System.out.println("removeUser");
        String id = "";
        UserService instance = new UserServiceImpl();
        instance.removeUser(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateUser method, of class UserService.
     */
    @Test
    public void testUpdateUser() throws Exception {
        System.out.println("updateUser");
        Staff staff = null;
        UserService instance = new UserServiceImpl();
        instance.updateUser(staff);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addUserType method, of class UserService.
     */
    @Test
    public void testAddUserType() throws Exception {
        System.out.println("addUserType");
        String type = "";
        UserService instance = new UserServiceImpl();
        instance.addUserType(type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeUserType method, of class UserService.
     */
    @Test
    public void testRemoveUserType() throws Exception {
        System.out.println("removeUserType");
        String type = "";
        UserService instance = new UserServiceImpl();
        instance.removeUserType(type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class UserServiceImpl implements UserService {

        public ObservableList<Staff> getStaff() {
            return null;
        }

        public ObservableList<UserType> getUserTypes() {
            return null;
        }

        public boolean userExists(String id) {
            return false;
        }

        public boolean validHash(String id, String input) {
            return false;
        }

        public Staff getUser(String id) {
            return null;
        }

        public void addUser(Staff staff) throws PreexistingEntityException, Exception {
        }

        public void removeUser(String id) throws IllegalOrphanException, NonexistentEntityException {
        }

        public void updateUser(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception {
        }

        public void addUserType(String type) throws PreexistingEntityException, Exception {
        }

        public void removeUserType(String type) throws IllegalOrphanException, NonexistentEntityException {
        }
    }
    
}
