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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author EdgarLaw
 */
public class SummaryPerformanceReportController implements Initializable {

    @FXML
    private TableView<?> tblDayShift1;
    @FXML
    private TableView<?> tblDayShift2;
    @FXML
    private TableView<?> tblPeriod;
    @FXML
    private Button btnPrint;
    @FXML
    private Label lblSPR;
    @FXML
    private Label lblDayshift1;
    @FXML
    private TableColumn<?, ?> tcDate;
    @FXML
    private TableColumn<?, ?> tcCopyRoom;
    @FXML
    private TableColumn<?, ?> tcDevelopment;
    @FXML
    private TableColumn<?, ?> tcFinishing;
    @FXML
    private TableColumn<?, ?> tcPacking;
    @FXML
    private Label lblDayShift2;
    @FXML
    private Label lblNightShift;
    @FXML
    private TableView<?> tblNightShift;
    @FXML
    private TableColumn<?, ?> tcTitle;
    @FXML
    private Label lblDayShift1Total;
    @FXML
    private TableView<?> tblDayShift1Total;
    @FXML
    private Label lblDayShift2Total;
    @FXML
    private TableView<?> tblDayShift2Total;
    @FXML
    private Label lblNightShiftTotal;
    @FXML
    private TableView<?> tblNightShiftTotal;
    @FXML
    private Label lblSPRTotal;
    @FXML
    private TableView<?> tblPeriodTotal;
    @FXML
    private TableColumn<?, ?> tcFinshing;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
