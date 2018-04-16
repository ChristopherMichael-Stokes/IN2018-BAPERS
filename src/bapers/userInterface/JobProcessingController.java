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

import bapers.data.domain.ComponentTask;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.JobService;
import bapers.service.JobService.Jobs;
import bapers.service.JobServiceImpl;
import bapers.userInterface.SceneController.Scenes;
import static bapers.userInterface.SceneController.switchScene;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class JobProcessingController implements Initializable {

    @FXML
    private TableColumn<ComponentTask, String> tcTask;
    @FXML
    private TableColumn<ComponentTask, String> tcDescription;
    @FXML
    private TableColumn<ComponentTask, String> tcStartTime;
    @FXML
    private TableColumn<ComponentTask, String> tcEndTime;
    @FXML
    private Button btnHome;
    @FXML
    private ComboBox<String> cmbCustomer;
    
    private final JobService jobDao = new JobServiceImpl();
    private final CustomerAccountService customerDao 
            = new CustomerAccountServiceImpl();
    
    private final Map<String, CustomerAccount> custMap = new HashMap<>();
    private final Map<Job, Jobs> jobMap = new HashMap<>();
    private final ObservableList<Job> jobs = FXCollections.observableArrayList();
    @FXML
    private ComboBox<Jobs> cmbJobType;
    @FXML
    private TableColumn<ComponentTask, String> tcJob;
    @FXML
    private TableView<Job> tblJob;
    @FXML
    private TableView<ComponentTask> tblTask;
    @FXML
    private TableColumn<Job, String> tcJobJob;
    @FXML
    private TableColumn<Job, String> tcJobDateIssued;
    @FXML
    private TableColumn<Job, String> tcJobItems;
    @FXML
    private TableColumn<Job, String> tcJobDeadline;
    @FXML
    private TableColumn<Job, String> tcJobPercentage;
    @FXML
    private TableColumn<Job, String> tcJobStatus;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //button
        btnHome.setOnAction((event) -> switchScene(Scenes.home));
        
        //combo
        cmbJobType.getItems().addAll(Jobs.all, Jobs.complete, Jobs.incomplete);
        cmbJobType.getSelectionModel().clearAndSelect(0);
        loadCustomers();
        cmbCustomer.setOnAction((event) -> loadJobs(cmbJobType.getValue()));
        cmbJobType.setOnAction((event) -> loadJobs(cmbJobType.getValue()));
        
        //table
        DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        tcJobJob.setCellValueFactory(p -> getProperty(""+p.getValue().getJobId()));
        tcJobDateIssued.setCellValueFactory(p -> getProperty(dateTimeFormat.format(p.getValue().getDateIssued())));
        tcJobDeadline.setCellValueFactory((p) -> {
            Date deadline = p.getValue().getDeadline();
            return deadline == null ? getProperty("None") : getProperty(timeFormat.format(deadline));
        });
        tcJobItems.setCellValueFactory(p -> getProperty(""+p.getValue().getQuanity()));
        tcJobStatus.setCellValueFactory(p -> getProperty(jobDao.jobComplete(p.getValue()) == false ? "incomplete" : "complete"));
        tcJobPercentage.setCellValueFactory(p -> {
            Float percentage = p.getValue().getAddedPercentage();
            return percentage == null ? getProperty("None") : getProperty(percentage+" %");
        });
        tblJob.setItems(jobs);
        

        
        
        
        
                        
    }    
    
    private SimpleStringProperty getProperty(String s) {
        return new SimpleStringProperty(s);
    }
    
    private void loadCustomers() {
        ObservableList<CustomerAccount> customers = customerDao.getCustomerAccounts();
        if (customers == null)
            return;
        
        cmbCustomer.getItems().clear();
        custMap.clear();
        for (CustomerAccount cust : customers) {
            String accountName = cust.getAccountHolderName();
            custMap.put(accountName, cust);
            cmbCustomer.getItems().add(accountName);
        }
    }
    
    private void loadJobs(Jobs jobType) {
        if (cmbCustomer.getValue() == null)
            return;       
        jobs.clear();
        jobs.addAll(jobDao.getJobs(custMap.get(cmbCustomer.getValue()), jobType));
    }
    
    
}

