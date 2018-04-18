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

import bapers.service.UserService;
import bapers.service.UserServiceImpl;
import bapers.userInterface.report.Report;
import bapers.utility.FormUtils;
import bapers.utility.report.IndividualReport;
import bapers.utility.report.IprResultSet;
import bapers.utility.report.ReportService;
import static bapers.utility.report.ReportService.*;
import bapers.utility.report.ShiftResultSet;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.springframework.util.StringUtils;

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
    @FXML
    private Button btnHome;
    @FXML
    private RadioButton rbAllUsers;
    @FXML
    private RadioButton rbUser;

    private final ToggleGroup reportType = new ToggleGroup(),
            userSelection = new ToggleGroup();
    private final String uri = "/bapers/userInterface/report/";
    private final UserService userDao = new UserServiceImpl();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblReports.setText("Reports");
        lblHomeReports.setText("Home>Reports");

        rbAllUsers.setToggleGroup(userSelection);
        rbUser.setToggleGroup(userSelection);
        rbIPR.setToggleGroup(reportType);
        rbIR.setToggleGroup(reportType);
        rbSPR.setToggleGroup(reportType);
        rbUser.setOnAction((event) -> {
            txtFirstName.setEditable(true);
            txtSurname.setEditable(true);
        });
        rbAllUsers.setOnAction((event) -> {
            txtFirstName.setEditable(false);
            txtSurname.setEditable(false);
        });
        
        rbAllUsers.setSelected(true);

        txtSurname.setVisible(false);
        txtFirstName.setVisible(false);
        dpStartDateIPR.setVisible(false);
        dpStartDateSPR.setVisible(false);
        dpStartDateIR.setVisible(false);
        dpEndDateIPR.setVisible(false);
        dpEndDateSPR.setVisible(false);
        dpEndDateIR.setVisible(false);
        txtAccountNumber.setVisible(false);
        rbAllUsers.setVisible(false);
        rbUser.setVisible(false);
        rbIPR.setOnAction((event) -> {
            rbAllUsers.setVisible(true);
            rbUser.setVisible(true);
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
            rbAllUsers.setVisible(false);
            rbUser.setVisible(false);
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
            rbAllUsers.setVisible(false);
            rbUser.setVisible(false);
        });
        btnConfirm.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle(null);
            if (rbIR.isSelected()) {
                if (dpStartDateIR.getValue() == null
                        || dpEndDateIR.getValue() == null
                        || txtAccountNumber.getText().trim().equals("")) {
                    alert.setContentText("Please fill in all the details!");
                    alert.showAndWait();
                } else {
                    System.err.println(dpStartDateIR.getValue().toString());
                    List<IndividualReport> irList
                            = getIndividualReport(txtAccountNumber.getText().trim(),
                                    dpStartDateIR.getValue().toString(),
                                    dpEndDateIR.getValue().toString());

                    Pair<Report, Stage> report = loadReport("IndividualReport.fxml");
                    if (report == null) {
                        FormUtils.haltAlert("Cannot display report");
                        return;
                    }
                    report.getKey().setItems(irList);
                    report.getValue().showAndWait();
                }
            } else if (rbIPR.isSelected()) {
                if (dpStartDateIPR.getValue() == null
                        || dpEndDateIPR.getValue() == null) {
                    alert.setContentText("Please fill in all the details!");
                    alert.showAndWait();
//                } else if (){

                } else {
                    String firstName = StringUtils.capitalize(getText(txtFirstName)),
                            surname = StringUtils.capitalize(getText(txtSurname)),
                            name = firstName + " " + surname;
                    if (rbAllUsers.isSelected()) {
                        name = "all";
                    } else if (rbUser.isSelected()) {
                        if (!userDao.userExists(firstName, surname)) {
                            FormUtils.haltAlert(name + " does not exist in the system");
                            return;
                        }
                    }
                    IprResultSet iprResult
                            = ReportService.getIndividualPerformanceReport(
                                    name, dpStartDateIPR.getValue().toString(),
                                    dpEndDateIPR.getValue().toString());

                    Pair<Report, Stage> report = loadReport("IndividualPerformanceReport.fxml");
                    if (report == null) {
                        FormUtils.haltAlert("Cannot show report");
                        return;
                    }
                    report.getKey().setItems(iprResult);
                    report.getValue().showAndWait();
                }
            } else if (rbSPR.isSelected()) {
                if (dpStartDateSPR.getValue() == null
                        || dpEndDateSPR.getValue() == null) {
                    alert.setContentText("Please fill in all the details!");
                    alert.showAndWait();
                } else {
                    String startDate = dpStartDateSPR.getValue().toString(),
                            endDate = dpEndDateSPR.getValue().toString();
                    ShiftResultSet srs = ReportService.getSummaryPerformanceReport(startDate, endDate);
                    Pair<Report, Stage> report = loadReport("SummaryPerformanceReport.fxml");
                    if (report == null) {
                        FormUtils.haltAlert("Cannot show report");
                        return;
                    }
                    report.getKey().setItems(srs);
                    report.getValue().showAndWait();
                }
            } else {
                alert.setContentText("Please Select type for the report!");
                alert.showAndWait();
            }
        });

    }

    private String getText(TextField tf) {
        return tf.getText().trim();
    }

    private <T extends Report> Pair<T, Stage> loadReport(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(uri + name));
            Stage s = new Stage();
            Scene scene = new Scene(loader.load());
            s.setScene(scene);
            T r = loader.<T>getController();
            return new Pair<>(r, s);
        } catch (IOException ex) {
            return null;
        }
    }
}
