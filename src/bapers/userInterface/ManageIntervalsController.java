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
import static bapers.userInterface.SceneController.switchScene;
import static bapers.utility.FormUtils.haltAlert;
import bapers.utility.Intervals;
import bapers.utility.Times;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageIntervalsController implements Initializable {

    @FXML
    private Button btnHome;
    @FXML
    private RadioButton rbReport;
    @FXML
    private RadioButton rbDatabase;
    @FXML
    private ComboBox<Integer> cmbDays;
    @FXML
    private ComboBox<Integer> cmbHours;
    @FXML
    private ComboBox<Integer> cmbMinutes;
    @FXML
    private Button btnSetTime;
    
    private final ToggleGroup timeType = new ToggleGroup();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Combo box
        cmbHours.getItems().add(0);
        for (int i = 1; i < 60; ++i) {
            cmbHours.getItems().add(i);
            cmbMinutes.getItems().add(i);
        }
        for (int i = 0; i <= 14; ++i) {
            cmbDays.getItems().add(i);
        }
        
        //Radio buttons
        rbReport.setToggleGroup(timeType);
        rbDatabase.setToggleGroup(timeType);        
        timeType.selectToggle(rbReport);
        
        //button
        btnHome.setOnAction((event) -> switchScene(Scenes.home));
        
        btnSetTime.setOnAction((event) -> {
            Integer days = cmbDays.getValue(), hours = cmbHours.getValue(),
                    minutes = cmbMinutes.getValue();
            if (minutes == null) {
                haltAlert("Select at least the amount of minutes (hours and days are optional)");
                return;
            }
            Integer optDays = cmbDays.getValue(), optHours = cmbHours.getValue(),
                    optMinutes = cmbMinutes.getValue();
            int days_ = optDays == null ? 0 : optDays,
                    hours_ = optHours == null ? 0 : optHours,
                    minutes_ = optMinutes == null ? 0 : optMinutes;
            if (rbReport.isSelected()) {
                Intervals.setMainReportIntervals(new Times(days_, hours_, minutes_));
            } else if (rbDatabase.isSelected()) {
                Intervals.setMainBackupIntervals(new Times(days_, hours_, minutes_));
            }
            
        });
    }    
    
}
