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
import bapers.utility.report.IprResultSet;
import bapers.utility.report.IprTotalIndividual;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author EdgarLaw
 */
public class IndividualPerformanceReportController extends Report<IprResultSet> implements Initializable {

    @FXML
    private TableView<IndividualPerformanceReport> tblIPR;
    @FXML
    private Button btnPrint;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcName;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcCode;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcTaskID;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcDepartment;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcDate;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcStartTime;
    @FXML
    private TableColumn<IndividualPerformanceReport, String> tcTimeTaken;
    @FXML
    private TableColumn<IprTotalIndividual, String> tcTotalName;
    @FXML
    private TableColumn<IprTotalIndividual, String> tcTotalIndiviudalEffort;
    @FXML
    private TextField txtTotal;
    @FXML
    private TableView<IprTotalIndividual> tblTotal;
    @FXML
    private HBox hbRoot;

    private ObservableList<IndividualPerformanceReport> tb1 = FXCollections.observableArrayList();
    private ObservableList<IprTotalIndividual> tb2 = FXCollections.observableArrayList();
    private ObservableIntegerValue total = new SimpleIntegerProperty(0);
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTable();
        tblIPR.setItems(tb1);
        tblTotal.setItems(tb2);
        txtTotal.setText(total.toString());
        txtTotal.setEditable(false);
        btnPrint.setOnAction((event) -> {
            stage.setMaximized(true);
            stage.setFullScreen(true);
            print(hbRoot);
            stage.setMaximized(false);
            stage.setFullScreen(false);
        });
    }

    /**
     *
     * @param ir
     */
    @Override
    public void setItems(IprResultSet ir) {
        tb1.clear();
        tb1.addAll(ir.individualEffort);
        System.out.println(tb1.size());
        tb2.clear();
        tb2.addAll(ir.totalIndividualEffort);
        txtTotal.setText(ir.totalOverallEffort);
    }

    /**
     *
     */
    @Override
    protected void setTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tcTaskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tcTimeTaken.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));
        
        tcTotalIndiviudalEffort.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTime()));
        tcTotalName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
    }

}
