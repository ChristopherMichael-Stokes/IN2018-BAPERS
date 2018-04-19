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

import bapers.utility.report.IndividualReport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author EdgarLaw
 */
public class IndividualReportController extends Report<IndividualReport> implements Initializable {

    @FXML
    private TableView<IndividualReport> tblIR;
    @FXML
    private Button btnPrint;
    @FXML
    private TableColumn<IndividualReport, String> tcCode;
    @FXML
    private TableColumn<IndividualReport, String> tcPrice;
    @FXML
    private TableColumn<IndividualReport, String> tcTask;
    @FXML
    private TableColumn<IndividualReport, String> tcDepartment;
    @FXML
    private TableColumn<IndividualReport, String> tcStartTime;
    @FXML
    private TableColumn<IndividualReport, String> tcTimeTaken;
    @FXML
    private TableColumn<IndividualReport, String> tcCompletedBy;
    @FXML
    private TableColumn<IndividualReport, String> tcShelfOnCompletion;
    @FXML
    private AnchorPane apRoot;
    @FXML
    private VBox vbRoot;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblIR.setItems(reportList);
        setTable();
        btnPrint.setOnAction((event) -> {
            stage.setMaximized(true);
            stage.setFullScreen(true);
            print(vbRoot);
            stage.setMaximized(false);
            stage.setFullScreen(false);
        });
    }

    @Override
    protected void setTable() {
        tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcTask.setCellValueFactory(new PropertyValueFactory<>("task"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tcStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tcTimeTaken.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));
        tcCompletedBy.setCellValueFactory(new PropertyValueFactory<>("completedBy"));
        tcShelfOnCompletion.setCellValueFactory(new PropertyValueFactory<>("shelfOnCompletion"));
    }

}
