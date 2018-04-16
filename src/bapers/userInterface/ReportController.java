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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ReportController implements Initializable {
    @FXML
    private TextField txtSurname;
    @FXML
    private DatePicker dpStartDateIPR;
    @FXML
    private DatePicker dpEndDateSPR;
    @FXML
    private Button btnPrint;
    @FXML
    private RadioButton rbSPR;
    @FXML
    private RadioButton rbIR;
    @FXML
    private Label lblHomeReports;
    @FXML
    private Label lblReports;
    @FXML
    private TextField txtAccountNumber;
    @FXML
    private Button btnConfirm;
    @FXML
    private RadioButton rbIPR;
    @FXML
    private DatePicker dpEndDateIPR;
    @FXML 
    private DatePicker dpStartDateSPR;
    @FXML
    private DatePicker dpEndDateIR;
    @FXML
    private TextField txtFirstName;
    @FXML
    private DatePicker dpStartDateIR;
    private final ToggleGroup reportType = new ToggleGroup();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblReports.setText("Reports");
        lblHomeReports.setText("Home>Reports");
        rbIPR.setToggleGroup(reportType);
        rbIR.setToggleGroup(reportType);
        rbSPR.setToggleGroup(reportType);
        txtSurname.setVisible(false);
        txtFirstName.setVisible(false);
        dpStartDateIPR.setVisible(false);
        dpStartDateSPR.setVisible(false);
        dpStartDateIR.setVisible(false);
        dpEndDateIPR.setVisible(false);
        dpEndDateSPR.setVisible(false);
        dpEndDateIR.setVisible(false);
        txtAccountNumber.setVisible(false);
        rbIPR.setOnAction((event) -> {
        txtSurname.setVisible(true);
        txtFirstName.setVisible(true);
        dpStartDateIPR.setVisible(true);
        dpStartDateSPR.setVisible(false);
        dpStartDateIR.setVisible(false);
        dpEndDateIPR.setVisible(true);
        dpEndDateSPR.setVisible(false);
        dpEndDateIR.setVisible(false);
        txtAccountNumber.setVisible(false);
        });
        rbSPR.setOnAction((event) -> {
        txtSurname.setVisible(false);
        txtFirstName.setVisible(false);
        dpStartDateIPR.setVisible(false);
        dpStartDateSPR.setVisible(true);
        dpStartDateIR.setVisible(false);
        dpEndDateIPR.setVisible(false);
        dpEndDateSPR.setVisible(true);
        dpEndDateIR.setVisible(false);
        txtAccountNumber.setVisible(false);
        });
        rbIR.setOnAction((event) -> {
        txtSurname.setVisible(false);
        txtFirstName.setVisible(false);
        dpStartDateIPR.setVisible(false);
        dpStartDateSPR.setVisible(false);
        dpStartDateIR.setVisible(true);
        dpEndDateIPR.setVisible(false);
        dpEndDateSPR.setVisible(false);
        dpEndDateIR.setVisible(true);
        txtAccountNumber.setVisible(true);
        });
        btnConfirm.setOnAction((event)->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle(null);
        if(rbIR.isSelected()){
            if(dpStartDateIR.getValue() == null
                    ||dpEndDateIR.getValue() == null
                    ||txtAccountNumber.getText().trim().equals(""))
            {
                alert.setContentText("Please fill in all the details!");
                alert.showAndWait();
            }
            else
            {
                
            }
        }
        else if(rbIPR.isSelected()){
            if(dpStartDateIPR.getValue() == null
                    ||dpEndDateIPR.getValue() == null
                    ||txtSurname.getText().trim().equals("")
                    ||txtFirstName.getText().trim().equals(""))
            {
                alert.setContentText("Please fill in all the details!");
                alert.showAndWait();
            }
            else
            {
                
            }
        }
        else if(rbSPR.isSelected()){
            if(dpStartDateSPR.getValue() == null
                    ||dpEndDateSPR.getValue() == null)
            {
                alert.setContentText("Please fill in all the details!");
                alert.showAndWait();
            }
            else
            {
                
            }
        }
        else
        {
            alert.setContentText("Please Select type for the report!");
            alert.showAndWait();
        }
        });
    
}
}
