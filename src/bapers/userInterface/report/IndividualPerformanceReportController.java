/*
 * Copyright (c) 2018, EdgarLaw
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
package bapers.userInterface.report;

import bapers.utility.report.IndividualPerformanceReport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author EdgarLaw
 */
public class IndividualPerformanceReportController extends Report<IndividualPerformanceReport> implements Initializable {

    @FXML
    private TableView<IndividualPerformanceReport> tblIPR;
    @FXML
    private Button btnPrint;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcName;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcTaskID;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcDepartment;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcDate;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcStartTime;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcTimeTaken;
    @FXML
    private TableColumn<IndividualPerformanceReport,String> tcTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblIPR.setItems(reportList);
        setTable();
    }
    
    private void setTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcTaskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tcTimeTaken.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

}
