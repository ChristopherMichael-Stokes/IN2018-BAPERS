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

import bapers.userInterface.SceneController.Scenes;
import static bapers.utility.BackupService.*;
import static bapers.utility.FormUtils.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageBackupController implements Initializable {

    @FXML
    private Button btnHome;
    @FXML
    private Button btnBackupNow;
    @FXML
    private Button btnRestoreFromBackup;
    @FXML
    private TableView<File> tblBackup;
    @FXML
    private TableColumn<File, String> tcDate;
    @FXML
    private TableColumn<File, String> tcTime;
    @FXML
    private TableColumn<File, String> tcSize;
    @FXML
    private Button btnRemoveBackup;
        
    private  ObservableList<File> files = FXCollections.observableArrayList();    
    private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat time = new SimpleDateFormat("HH:mm:ss");
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Table
        loadBackups();
        tblBackup.setItems(files);
        
        tblBackup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tcDate.setCellValueFactory((param) -> {
            try {
                String d = date.format(BACKUPDATE.parse(param.getValue().getName()));
                return getProperty(d);
            } catch (ParseException ex) {               
                return getProperty("Invalid date");
            }
        });       
        tcTime.setCellValueFactory((param) -> {
            try {
                String d = time.format(BACKUPDATE.parse(param.getValue().getName()));
                return getProperty(d);
            } catch (ParseException ex) {                
                return getProperty("Invalid time");
            }
        }); 
        tcSize.setCellValueFactory((param) -> getProperty(param.getValue().length()/1e3 +" KB"));
                
        //button
        alert.getButtonTypes().add(ButtonType.OK);

        btnBackupNow.setOnAction((event) -> {
            try {
                backup();
                haltAlert("Successfully created backup");
                loadBackups();
            } catch (IOException ex) {
                haltAlert("Cannot backup at this moment");
            }
        });
        
        btnRestoreFromBackup.setOnAction((event) -> {
            File f = tblBackup.getSelectionModel().getSelectedItem();
            if (f == null) {
                haltAlert("Please select a backup to load");
                loadBackups();
                return;
            }
            try {
                restoreFromBackup(f);
                haltAlert("Successfully restored backup");
            } catch (IOException ex) {
                haltAlert("Cannot restore from selected backup");
            }
        });
        
        btnRemoveBackup.setOnAction((event) -> {
            ObservableList<File> f = tblBackup.getSelectionModel().getSelectedItems();
            if (f == null) {
                haltAlert("Please select one or more backups to remove");
                loadBackups();
                return;
            }
            f.stream().forEach(d -> d.delete());
            loadBackups();
        });
        
        btnHome.setOnAction((event) -> SceneController.switchScene(Scenes.home));        
    }    
    
    private void loadBackups() {
        files.clear();
        files.addAll(getBackupList());
        files.sort((File o1, File o2) -> -o1.getName().compareTo(o2.getName())); 
    }
}
