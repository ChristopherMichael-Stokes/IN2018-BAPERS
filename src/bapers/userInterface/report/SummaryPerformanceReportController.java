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

import bapers.utility.report.Shift;
import bapers.utility.report.ShiftResultSet;
import bapers.utility.report.SummaryShift;
import bapers.utility.report.TotalShift;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author EdgarLaw
 */
public class SummaryPerformanceReportController extends Report<ShiftResultSet>
        implements Initializable {

    @FXML
    private TableView<Shift> tblDayShift1;
    @FXML
    private TableView<Shift> tblDayShift2;
    @FXML
    private Button btnPrint;
    @FXML
    private Label lblSPR;
    @FXML
    private Label lblDayshift1;
    @FXML
    private Label lblDayShift2;
    @FXML
    private Label lblNightShift;
    @FXML
    private TableView<Shift> tblNightShift;
    @FXML
    private Label lblDayShift1Total;
    @FXML
    private TableView<TotalShift> tblDayShift1Total;
    @FXML
    private Label lblDayShift2Total;
    @FXML
    private TableView<TotalShift> tblDayShift2Total;
    @FXML
    private Label lblNightShiftTotal;
    @FXML
    private TableView<TotalShift> tblNightShiftTotal;
    @FXML
    private Label lblSPRTotal;
    @FXML
    private TableView<SummaryShift> tblSummary;
    @FXML
    private TableView<TotalShift> tblSummaryTotal;
    
    private final ObservableList<Shift> 
            dayShift1 = FXCollections.observableArrayList(),
            dayShift2 = FXCollections.observableArrayList(),
            nightShift = FXCollections.observableArrayList();    
    private final ObservableList<SummaryShift> 
            summaryShift = FXCollections.observableArrayList();
    
    private final ObservableList<TotalShift> 
            dayShift1Total = FXCollections.observableArrayList(),
            dayShift2Total = FXCollections.observableArrayList(),
            nightShiftTotal = FXCollections.observableArrayList(),
            summaryShiftTotal = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTable();
        initTable(tblDayShift1, Shift.class);
        initTable(tblDayShift2, Shift.class);
        initTable(tblNightShift, Shift.class);
        initTable(tblSummary, SummaryShift.class);
        initTable(tblDayShift1Total, TotalShift.class);
        initTable(tblDayShift2Total, TotalShift.class);
        initTable(tblNightShiftTotal, TotalShift.class);
        initTable(tblSummaryTotal, TotalShift.class);
        
        Class<SummaryShift> ss = SummaryShift.class;
        System.err.println("shift: "+Shift.class.getClass().getName());
        System.err.println("summary shift: "+ss.getClass().getName());
        className(Shift.class);
    }
    
    private <S> String className(Class<S> objectClass) {
        return objectClass.getTypeName();
    }
    
    private <S extends TotalShift> void initTable(TableView<S> table, Class<S> objectClass) {
        TableColumn<S, String> 
                copyRoom = new TableColumn<>("Copy Room"),
                development = new TableColumn<>("Development"),
                finishing = new TableColumn<>("Finishing"),
                packing = new TableColumn<>("Packing");
        table.getColumns().clear();
        
        
        String genericName = className(objectClass),
                shiftName = className(Shift.class),
                summaryShiftName = className(SummaryShift.class);

        if (genericName.equals(shiftName)) {
            TableColumn<S, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            table.getColumns().add(date);
        } else if (genericName.equals(summaryShiftName)) {
            TableColumn<S, String> title = new TableColumn<>("Title");
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            table.getColumns().add(title);
        }
        
        copyRoom.setCellValueFactory(new PropertyValueFactory<>("copyRoom"));
        development.setCellValueFactory(new PropertyValueFactory<>("development"));
        finishing.setCellValueFactory(new PropertyValueFactory<>("finishing"));
        packing.setCellValueFactory(new PropertyValueFactory<>("packing"));
        
        table.getColumns().addAll(copyRoom, development, finishing, packing);
    }
    
    @Override
    public void setItems(ShiftResultSet srs) {
        dayShift1.setAll(srs.dayShift1);
        dayShift2.setAll(srs.dayShift2);
        nightShift.setAll(srs.nightShift);
        summaryShift.setAll(srs.summaryShift);
        
        dayShift1Total.setAll(srs.dayShift1Total);
        dayShift2Total.setAll(srs.dayShift2Total);
        nightShiftTotal.setAll(srs.nightShiftTotal);
        summaryShiftTotal.setAll(srs.summaryShiftTotal);
    }

    @Override
    protected void setTable() {
        tblDayShift1.setItems(dayShift1);
        tblDayShift2.setItems(dayShift2);
        tblNightShift.setItems(nightShift);
        tblSummary.setItems(summaryShift);
        
        tblDayShift1Total.setItems(dayShift1Total);
        tblDayShift2Total.setItems(dayShift2Total);
        tblNightShiftTotal.setItems(nightShiftTotal);
        tblSummaryTotal.setItems(summaryShiftTotal);
    }

    

}
