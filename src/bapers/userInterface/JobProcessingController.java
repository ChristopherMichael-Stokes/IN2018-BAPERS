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

import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.Task;
import bapers.data.domain.User;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.JobService;
import bapers.service.JobService.Jobs;
import bapers.service.JobServiceImpl;
import bapers.service.PaymentService;
import bapers.service.PaymentServiceImpl;
import bapers.service.UserService;
import bapers.service.UserServiceImpl;
import bapers.userInterface.SceneController.Scenes;
import static bapers.userInterface.SceneController.switchScene;
import static bapers.utility.FormUtils.*;
import bapers.utility.CurrencyFormat;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class JobProcessingController implements Initializable {

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
    @FXML
    private ComboBox<Jobs> cmbJobType;
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
    @FXML
    private TableColumn<Job, String> tcJobCost;
    @FXML
    private TextField txtTaskStart;
    @FXML
    private TextField txtTaskEnd;
    @FXML
    private Button btnUpdateTask;
    @FXML
    private Button btnAddTask;
    @FXML
    private Button btnRemoveTask;
    @FXML
    private TableColumn<ComponentTask, String> tcTask;
    @FXML
    private TextField txtTask;
    @FXML
    private TableColumn<ComponentTask, String> tcJobTask;
    @FXML
    private TextField txtJobTask;
    @FXML
    private TableColumn<ComponentTask, String> tcUser;
    @FXML
    private TextField txtUser;
    
    private final JobService jobDao = new JobServiceImpl();
    private final CustomerAccountService customerDao 
            = new CustomerAccountServiceImpl();    
    private final PaymentService paymentDao = new PaymentServiceImpl();
    private final UserService userDao = new UserServiceImpl();
    
    private final Map<String, CustomerAccount> custMap = new HashMap<>();
    
    private final ObservableList<Job> jobs = FXCollections.observableArrayList();
    private final ObservableList<ComponentTask> tasks = FXCollections.observableArrayList();
    private final TextFormatter<Double> tf = new CurrencyFormat();
    
    private final DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
    
    private final Alert alert = new Alert(Alert.AlertType.NONE);
   
    
    

    /**
     * Initializes the controller class.
     * @param url is the directory used to retrieve the .fxml files which contain the gui
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
        tblJob.setItems(jobs);
        tblTask.setItems(tasks);
        
        
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
        tcJobCost.setCellValueFactory(p -> getProperty(tf.getValueConverter().toString(paymentDao.getJobCost(p.getValue().getJobId()))));
        tblJob.getSelectionModel().selectedItemProperty().addListener(c -> { 
            SelectionModel<Job> sm = tblJob.getSelectionModel();
            Job j = sm.getSelectedItem();
            loadTasks(j);
        });
        
        tcEndTime.setCellValueFactory((p) -> {
            Date endTime = p.getValue().getEndTime();
            return endTime == null ? getProperty("None") : getProperty(dateTimeFormat.format(endTime));
        });
        tcStartTime.setCellValueFactory((p) -> {
            Date startTime = p.getValue().getStartTime();
            return startTime == null ? getProperty("None") : getProperty(dateTimeFormat.format(startTime));
        });
        tcTask.setCellValueFactory(p -> getProperty(""+p.getValue().getComponentTaskPK().getFkTaskId()));
        tcJobTask.setCellValueFactory(p -> getProperty(p.getValue().getComponentTaskPK().getFkComponentId()));
        tcDescription.setCellValueFactory(p -> getProperty(p.getValue().getJobComponent().getDescription()));
        tcUser.setCellValueFactory(p -> {
            ComponentTask ct = p.getValue();
            if (ct == null)
                return null;
            User u = ct.getFkUsername();
            return u != null ? getProperty(u.getUsername()) : getProperty("No User");
        });
                
        tblTask.setEditable(true);
        tblTask.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ESCAPE))
                clearText();
        });
        
        tblTask.getSelectionModel().selectedItemProperty().addListener(c -> {
            SelectionModel<ComponentTask> sm = tblTask.getSelectionModel();
            ComponentTask ct = sm.getSelectedItem();
            if (ct == null)
                return;
            
            txtJobTask.setText(ct.getComponentTaskPK().getFkComponentId());
            txtTask.setText(""+ct.getComponentTaskPK().getFkTaskId());
            
            User u = ct.getFkUsername();
            if (u != null) {
                txtUser.setText(u.getUsername());
            }
            
            Date startTime = ct.getStartTime(), endTime = ct.getEndTime();
            txtTaskEnd.setText(endTime != null ? dateTimeFormat.format(ct.getEndTime()) : "");
            txtTaskStart.setText(startTime != null ? dateTimeFormat.format(ct.getStartTime()) : "");     
            txtJobTask.setEditable(false);
            txtTask.setEditable(false);
        });
        
        //buttons
        alert.getButtonTypes().add(ButtonType.OK);
        btnRemoveTask.setOnAction((event) -> {
            ComponentTask ct = tblTask.getSelectionModel().getSelectedItem();
            if (ct == null) {
                haltAlert("You must first select a task to remove");
                return;
            }
            try {
                jobDao.removeComponentTask(ct);
                haltAlert("Task successfully removed");
                int jobSelection = tblJob.getSelectionModel().getSelectedIndex(),
                    selection = cmbCustomer.getSelectionModel().getSelectedIndex();
                loadCustomers();
                cmbCustomer.getSelectionModel().select(selection);
                loadJobs(cmbJobType.getValue());
                tblJob.getSelectionModel().select(jobSelection);
            } catch (NonexistentEntityException ex) {
                haltAlert("Task no longer exists");
                clearText();
                return;
            }
        });
        
        btnUpdateTask.setOnAction((event) -> {
            //TODO - add code for txtUser
            ComponentTask ct = tblTask.getSelectionModel().getSelectedItem();
            if (ct == null) {
                haltAlert("You must first select a task to update");
                return;
            } 
            String taskId_ = getText(txtTask), startText = getText(txtTaskStart), 
                    endText = getText(txtTaskEnd);
            
            if (taskId_.equals("")) {
                haltAlert("You must provide a task ID");
                return;
            }
            Task t = null;
            try {
                int taskId = Integer.parseInt(taskId_);
                t = jobDao.getTask(taskId);
                if (t == null) {
                    haltAlert("Task does not exist");
                    txtTask.clear();
                    return;
                }
                ct.setTask(jobDao.getTask(taskId));
            } catch (NumberFormatException ex) {
                haltAlert("Invalid task ID");
                return;
            }
            
            try {
                Date startTime = dateTimeFormat.parse(startText);
                ct.setStartTime(startTime);
            } catch (ParseException ex) {
                haltAlert("Enter a valid start time");
                txtTaskStart.clear();
                return;
            }
            try {
                Date endTime = dateTimeFormat.parse(endText);                                    
                ct.setEndTime(endTime);
            } catch (ParseException ex) {
                alert.setContentText("Enter a valid end time");
                txtTaskEnd.clear();
                return;
            }
            
            User u = userDao.getUser(getText(txtUser));
            if (u != null)
                ct.setFkUsername(u);
            
            try {
                jobDao.updateTask(ct);
                haltAlert("Task successfully updated");               
            } catch (NonexistentEntityException ex) {
                haltAlert("Selected item no longer exists");
            } catch (Exception ex) {
                System.err.println(ex);
                haltAlert("Cannot update task");
            } finally {
                int jobSelection = tblJob.getSelectionModel().getSelectedIndex(),
                    selection = cmbCustomer.getSelectionModel().getSelectedIndex();
                loadCustomers();
                cmbCustomer.getSelectionModel().select(selection);
                loadJobs(cmbJobType.getValue());
                tblJob.getSelectionModel().select(jobSelection);
                clearText();
            }            
        });
        
        btnAddTask.setOnAction((event) -> {          
            if (empty(txtTask) || empty(txtJobTask)) {
                haltAlert("Please enter task and job task ID fields");
                return;
            }
            int jobId = 0;
            JobComponent jc = jobDao.getComponent(getText(txtJobTask), 
                    tblJob.getSelectionModel().getSelectedItem().getJobId());
            if (jc == null) {
                haltAlert("Make sure a job is selected, and that the job task exists");
                txtJobTask.clear();
                return;
            }
            int taskId = 0;
            Task t = null;
            try {
                taskId = Integer.parseInt(getText(txtTask));
                t = jobDao.getTask(taskId);
                if (!jobDao.taskExists(taskId)) {
                    haltAlert("Task does not exist");
                    txtTask.clear();
                    return;
                }
                
            } catch (NumberFormatException ex) {
                haltAlert("Invalid task ID");
            }
            
            ComponentTask ct = new ComponentTask(jc.getJobComponentPK().getFkJobId(), jc.getJobComponentPK().getComponentId(), taskId);
            try {
                String startText = getText(txtTaskStart);
                if (startText.equals(""))
                    return;
                Date startTime = dateTimeFormat.parse(startText);
                ct.setStartTime(startTime);
            } catch (ParseException ex) {
                haltAlert("Enter a valid start time");
                txtTaskStart.clear();
                return;
            }
            try {
                String endText = getText(txtTaskEnd);
                if (endText.equals(""))
                    return;
                Date endTime = dateTimeFormat.parse(endText);                                    
                ct.setEndTime(endTime);
            } catch (ParseException ex) {
                alert.setContentText("Enter a valid end time");
                txtTaskEnd.clear();
                return;
            }
            
            User u = userDao.getUser(getText(txtUser));
            if (u != null)
                ct.setFkUsername(u);
            
            try {                
                jobDao.addComponentTask(ct, t, jc);
                haltAlert("Task successfully added");
                int jobSelection = tblJob.getSelectionModel().getSelectedIndex(),
                    selection = cmbCustomer.getSelectionModel().getSelectedIndex();
                loadCustomers();
                cmbCustomer.getSelectionModel().select(selection);
                loadJobs(cmbJobType.getValue());
                tblJob.getSelectionModel().select(jobSelection);
                clearText();
            } catch (PreexistingEntityException ex) {
                haltAlert("The given task already exists");
                clearText();
            } catch (Exception ex) {
                haltAlert("Could not add task");
            }
            
        });
                        
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
        clearText();
    }
    
    private void loadTasks(Job job) {
        tasks.clear();
        if (job == null)
            return;
        
        tasks.addAll(job.getJobComponentList().stream()
                .flatMap(jc -> jc.getComponentTaskList().stream())
                .collect(Collectors.toList()));
    }
    
    private void clearText() {
        txtJobTask.setEditable(true);
        txtTask.setEditable(true);
        txtJobTask.clear();
        txtTask.clear();
        txtTaskEnd.clear();
        txtTaskStart.clear();
        txtUser.clear();
    }
    
    private String getText(TextField tf) {
        return tf.getText().trim();
    }
    
    private boolean empty(TextField tf) {
        return getText(tf).equals("");
    }    
}

