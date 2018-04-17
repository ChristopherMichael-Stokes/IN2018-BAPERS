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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
    private Button btnRestoreLastBackup;
    @FXML
    private ListView<File> lsvBackup;
    @FXML
    private Button btnRestoreFromBackup;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblSize;
        
    private  ObservableList<File> files = FXCollections.observableArrayList();    
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //List
        loadBackups();
        lsvBackup.setItems(files);
        
        lsvBackup.setCellFactory((ListView<File> param) -> {
            return new ListCell<File>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) 
                        try {
                            setText(df.format(BACKUPDATE.parse(item.getName())));
                    } catch (ParseException ex) {
                        Logger.getLogger(ManageBackupController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        });
        
        lsvBackup.getSelectionModel().selectedItemProperty().addListener(c -> {
            File f = lsvBackup.getSelectionModel().getSelectedItem();
            if (f == null) {
                loadBackups();
                return;
            }
            lblSize.setText("Size: "+(f.length()/1e3)+" KB");
        });
        
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
            File f = lsvBackup.getSelectionModel().getSelectedItem();
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
        
        btnHome.setOnAction((event) -> SceneController.switchScene(Scenes.home));
        
    }    
    
    private void haltAlert(String message) {
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void loadBackups() {
        files.clear();
        files.addAll(getBackupList());
        files.sort((File o1, File o2) -> -o1.getName().compareTo(o2.getName()));   
        lblSize.setText("Size: ");
    }
}
