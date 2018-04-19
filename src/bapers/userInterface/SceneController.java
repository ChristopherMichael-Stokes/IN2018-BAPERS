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
package bapers.userInterface;


import static bapers.BAPERS.primaryStage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static bapers.BAPERS.USER;

/**
 *
 * @author chris
 */
public class SceneController {

    public static enum Scenes {

     //accessing different scenes for the different gui forms
        payment("AddPayment"), 

      
        createAccount("CreateAccount"), 


        home("HomePage"), 

        jobProcessing("JobProcessing"), 

       
        login("Login"),

        
        manageBackup("ManageBackup"),

        
        manageCustomerAccount("ManageCustomerAccount"), 

        
        manageIntervals("ManageIntervals"), 

        
        manageStaff("ManageStaff"), 

        
        manageTasks("ManageTasks"), 

        
        placeOrder("PlaceOrder"), 

     
        report("Report");
        
        private final String fxml;
        private Scenes(String fxml) {
            this.fxml = fxml;
        }

        /**
         *
         * @param fxml
         * @return
         */
        public boolean equals(String fxml) {
            return this.fxml.equals(fxml);
        }        
        @Override
        public String toString() {
            return fxml;
        }
        
    }
    
    /**
     *
     * @param scene
     */
    
    //switching scenes between the forms
    public static void switchScene(Scenes scene) {
        Stage stage = primaryStage;
        double height = 0.0, width = 0.0;
        try {            
            switch(scene) {
                case payment: 
//                    height = 0; width = 0;
                    break;
                case createAccount: 
                    break;      
                case home:
                    break;
                case jobProcessing: 
                    break;
                case login:
                    USER = null;
                    width = 800; height = 450;
                    break;
                case manageBackup:
                    break;
                case manageCustomerAccount: 
                    break;
                case manageIntervals: 
                    break;
                case manageStaff: 
                    break;
                case manageTasks:
                    break;
                case placeOrder:
                    break;
                case report:
                    break;
                default: 
                    switchScene(Scenes.login);
                    return;
            }
            
            stage.setScene(new Scene(FXMLLoader.load(
                    SceneController.class.getResource("fxml/"+scene+".fxml"))));
            if (width != 0.0 && height != 0.0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
            stage.show();
        } catch (IOException ex) {   
            System.err.println(ex.getMessage());
            System.err.println("invalid .fxml file");
            Platform.exit();
        }       
    }
    
    /**
     *
     */
    public static void logout() {
        switchScene(Scenes.login);
    }
}
