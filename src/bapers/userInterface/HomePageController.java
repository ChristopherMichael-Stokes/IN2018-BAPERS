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

import static bapers.BAPERS.USER;
import bapers.userInterface.SceneController.Scenes;
import static bapers.userInterface.SceneController.switchScene;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class HomePageController implements Initializable {

    @FXML
    private Button btnCustomerAccount;
    @FXML
    private Button btnIntervals;
    @FXML
    private Button btnBackup;
    @FXML
    private Button btnUsers;
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private Button btnJobProcessing;
    @FXML
    private Button btnPayment;
    @FXML
    private Button btnReports;
    @FXML
    private Button btnTasks;
    @FXML
    private Label lblName;
    @FXML
    private Label lblTime;
    @FXML
    private Button btnLogout;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblName.setText("Welcome back "+USER.getFirstName()+" "+USER.getSurname());
        
        //missing add customer account button
        btnCustomerAccount.setOnAction((event) -> switchScene(Scenes.manageCustomerAccount));
        btnIntervals.setOnAction((event) -> switchScene(Scenes.manageIntervals));
        btnPayment.setOnAction((event) -> switchScene(Scenes.payment));
        btnBackup.setOnAction((event) -> switchScene(Scenes.manageBackup));
        btnUsers.setOnAction((event) -> switchScene(Scenes.manageStaff));
        btnPlaceOrder.setOnAction((event) -> switchScene(Scenes.placeOrder));
        btnJobProcessing.setOnAction((event) -> switchScene(Scenes.jobProcessing));
        btnPayment.setOnAction((event) -> switchScene(Scenes.payment));
        btnReports.setOnAction((event) -> switchScene(Scenes.report));
        btnTasks.setOnAction((event) -> switchScene(Scenes.manageTasks));
        btnLogout.setOnAction((event) -> switchScene(Scenes.login));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), (event) -> {
                    lblTime.setText(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
                }),
                new KeyFrame(Duration.seconds(1))//Do something every second. In this case we are going to increment setStrP.
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
