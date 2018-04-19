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

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.ContactJpaController;
import bapers.data.dataAccess.CustomerAccountJpaController;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.ComponentTaskPK;
import bapers.data.domain.Contact;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.JobComponentPK;
import bapers.data.domain.Task;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.JobService;
import bapers.service.JobServiceImpl;
import bapers.service.TaskService;
import bapers.service.TaskServiceImpl;
import bapers.utility.FormUtils;
import static bapers.utility.SimpleHash.getStringHash;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class PlaceOrderController implements Initializable {

    @FXML
    private TextField txtSearchCustomer;
    @FXML
    private ListView<CustomerAccount> lsvCustomer;
    @FXML
    private Button btnCreateAccount;
    @FXML
    private Label lblJobinfo;
    @FXML
    private RadioButton optNo;
    @FXML
    private RadioButton optYes;
    @FXML
    private TextField txtRecordDeadline;
    @FXML
    private TextField txtItemAmount;
    @FXML
    private TextField txtComponentDesc;
    @FXML
    private Button btnAddComponent;
    @FXML
    private ListView<JobComponent> lsvComponent;
    @FXML
    private Label lblPrice1;
    @FXML
    private ComboBox<String> cmbTask;
    @FXML
    private Button btnAddTask;
    @FXML
    private ListView<ComponentTask> lsvTasks;
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private Button btnPrintLabel;
    @FXML
    private TextField txtExtraPercentage;
    @FXML
    private Button btnRemoveComponent;
    @FXML
    private Button btnRemoveTask;
    @FXML
    private Label lblJobinfo1;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtJobDescription;
    @FXML
    private Button btnHome;

    private final ToggleGroup urgent = new ToggleGroup();
    private final CustomerAccountService customerDao
            = new CustomerAccountServiceImpl();
    private final TaskService taskDao = new TaskServiceImpl();
    private final JobService jobDao = new JobServiceImpl();
//    private final TextFormatter<Double> tf = new DecimalFormat();

    private final ObservableList<JobComponent> components
            = FXCollections.observableArrayList();
    private final ObservableList<CustomerAccount> accounts
            = FXCollections.observableArrayList();
    private final ObservableList<ComponentTask> componentTasks
            = FXCollections.observableArrayList();
    private final ObservableList<String> tasks = FXCollections.observableArrayList();
    private final Map<String, Task> taskMap = new HashMap<>();
    private final Map<JobComponent, List<ComponentTask>> jobTaskMap = new HashMap<>();
    private final DateFormat time = new SimpleDateFormat("HH:mm");
    private Job job = new Job();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnHome.setOnAction(event -> SceneController.switchScene(SceneController.Scenes.home));
        //radioButtons
        optNo.setToggleGroup(urgent);
        optYes.setToggleGroup(urgent);
        optNo.setSelected(true);
        isUrgent(false);
        optNo.setOnAction((event) -> isUrgent(false));
        optYes.setOnAction((event) -> isUrgent(true));

        //customer search
        lsvCustomer.setItems(accounts);
        lsvCustomer.setCellFactory((param) -> new ListCell<CustomerAccount>() {
            @Override
            protected void updateItem(CustomerAccount item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getAccountHolderName() == null) {
                    setText(null);
                } else {
                    setText(item.getAccountHolderName());
                }
            }
        });

        accounts.setAll(customerDao.findCustomers(getText(txtSearchCustomer)));
        txtSearchCustomer.setOnKeyReleased((event) -> {
            accounts.setAll(customerDao.findCustomers(getText(txtSearchCustomer)));
        });

        //components
        lsvComponent.setItems(components);
        lsvComponent.getSelectionModel().selectedItemProperty().addListener(c -> {
            SelectionModel<JobComponent> sm = lsvComponent.getSelectionModel();
            JobComponent jc = sm.getSelectedItem();
            if (jc != null) {
                componentTasks.setAll(jc.getComponentTaskList());
            }
        });

        btnRemoveComponent.setOnAction((event) -> {
            lsvComponent.getSelectionModel().getSelectedItem();
        });
        btnAddComponent.setOnAction((event) -> {
            String description = getText(txtComponentDesc);
            if (isEmpty(description)) {
                FormUtils.haltAlert("Enter a description");
            }
            if (components.stream().anyMatch(jc -> jc.getDescription().equals(description))) {
                FormUtils.haltAlert("You cannot add the same componet twice");
                return;
            }
            JobComponent jc = new JobComponent(0, null);
            jc.setComponentTaskList(new ArrayList<>());
            jc.setDescription(description);
            try {
                JobComponentPK jpk = jc.getJobComponentPK();
                jpk.setComponentId(getStringHash(description).substring(0, 6));
                jc.setJobComponentPK(jpk);
                components.add(jc);
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("hash broken");
            }
        });

        btnRemoveComponent.setOnAction((event) -> {
            JobComponent selection = lsvComponent.getSelectionModel().getSelectedItem();
            if (selection != null) {
                components.remove(selection);

            }
        });

        lsvComponent.setCellFactory((param) -> new ListCell<JobComponent>() {
            @Override
            protected void updateItem(JobComponent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getDescription() == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });

        //tasks
        lsvTasks.setItems(componentTasks);
        lsvTasks.setCellFactory((param) -> new ListCell<ComponentTask>() {
            @Override
            protected void updateItem(ComponentTask item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getTask() == null) {
                    setText(null);
                } else {
                    setText(item.getTask().getTaskId().toString());
                }
            }
        });
        cmbTask.setItems(tasks);
        updateTask();
        cmbTask.setOnMouseClicked((event) -> updateTask());

        lsvComponent.getSelectionModel().selectedItemProperty().addListener(c -> {
            SelectionModel<JobComponent> sm = lsvComponent.getSelectionModel();
            JobComponent jc = sm.getSelectedItem();
            if (jc != null) {
                componentTasks.clear();
                List<ComponentTask> ctlist = jobTaskMap.get(jc);
                if (ctlist == null) {
                    ctlist = new ArrayList<>();
                    jobTaskMap.put(jc, ctlist);
                }

                componentTasks.addAll(ctlist);
            }
        });

        btnRemoveTask.setOnAction((event) -> {
            JobComponent jc = lsvComponent.getSelectionModel().getSelectedItem();
            ComponentTask selection = lsvTasks.getSelectionModel().getSelectedItem();
            if (selection != null && jc != null) {
                componentTasks.remove(selection);
                taskMap.remove(selection.getTask().getTaskId().toString());
                List<ComponentTask> ctl = jc.getComponentTaskList();
                ctl.remove(selection);
                jc.setComponentTaskList(ctl);
            }
        });

        btnAddTask.setOnAction((event) -> {
            JobComponent selection = lsvComponent.getSelectionModel().getSelectedItem();
            if (selection == null) {
                FormUtils.haltAlert("Select a component to add a task to");
                return;
            }
            if (cmbTask.getSelectionModel().isEmpty()) {
                FormUtils.haltAlert("Select a task");
                return;
            }
            Task t = taskMap.get(cmbTask.getSelectionModel().getSelectedItem());

            SelectionModel<JobComponent> sm = lsvComponent.getSelectionModel();
            JobComponent jc = sm.getSelectedItem();
            if (jc != null) {
                componentTasks.setAll(jc.getComponentTaskList());
            }

            ComponentTask ct = new ComponentTask(0, selection.getJobComponentPK().getComponentId(), t.getTaskId());
            ct.setTask(t);
            List<ComponentTask> ctk = jobTaskMap.get(selection);
            if (ctk == null) {
                jobTaskMap.put(selection, new ArrayList());
            }

            List<ComponentTask> ctal = jobTaskMap.get(selection);
            ctal.add(ct);
            jobTaskMap.replace(jc, ctal);

            componentTasks.setAll(ctal);
        });

        //finalize
        btnCreateAccount.setOnAction((event) -> {
            Stage stage = new Stage();
            try {
                Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/bapers/userInterface/fxml/CreateAccount.fxml")));
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                System.err.println("Cannot load add customer account form");
            }
        });

        btnPlaceOrder.setOnAction((event) -> {
            String firstName = getText(txtFirstName), surname = getText(txtSurname),
                    mobile = getText(txtMobile);

            if (isEmpty(firstName) || isEmpty(surname)) {
                FormUtils.haltAlert("Please enter contact details");
                return;
            }

            CustomerAccount account = lsvCustomer.getSelectionModel().getSelectedItem();
            if (account == null) {
                FormUtils.haltAlert("Please select a customer account");
                return;
            }

            job.setDateIssued(new Date());

            if (optYes.isSelected()) {
                String deadline_ = getText(txtRecordDeadline),
                        percentage_ = getText(txtExtraPercentage);
                Date deadline = null;
                try {
                    if (!isEmpty(deadline_)) {
                        deadline = time.parse(deadline_);
                    }
                } catch (ParseException ex) {
                    deadline = null;
                }
                if (deadline == null) {
                    FormUtils.haltAlert("Enter a deadline in the format hh:mm, e.g."
                            + " four and a half hours would be 4:30\n");
                    return;
                }
                Float percentage = null;
                try {
                    if (!isEmpty(percentage_)) {
                        percentage = Float.parseFloat(percentage_);
                    }
                } catch (NumberFormatException ex) {
                    percentage = null;
                }
                if (percentage == null) {
                    FormUtils.haltAlert("Enter a valid percentage");
                    return;
                }

                job.setAddedPercentage(percentage);
                job.setDeadline(deadline);
            }
            String description = getText(txtJobDescription);
            if (!isEmpty(description)) {
                job.setDescription(description);
            }

            Short amount = null;
            try {
                if (!isEmpty(getText(txtItemAmount))) {
                    amount = Short.parseShort(getText(txtItemAmount));
                }
            } catch (NumberFormatException ex) {
                amount = null;
            }
            if (amount == null) {
                FormUtils.haltAlert("Enter an amount of items");
                return;
            }
            job.setQuanity(amount);

            Contact contact = new Contact(firstName, surname,
                    account.getAccountNumber());
            contact.setJobList(new ArrayList<>());
            contact.setCustomerAccount(account);
            CustomerAccountJpaController cajpa = new CustomerAccountJpaController(EMF);
            CustomerAccount ca = cajpa.findCustomerAccount(account.getAccountNumber());
            contact.setCustomerAccount(ca);
            ContactJpaController cjpa = new ContactJpaController(EMF);
            try {
                cjpa.create(contact);
            } catch (Exception ex) {
                System.err.println("could not add contact\n" + ex);
            }
//            try {
////                new ContactJpaController(EMF).create(contact);
//                if (!)
//                contact = customerDao.addContact(contact, account);
//            } catch (Exception ex) {
//                System.err.println("could not add contact\n"+ex);
//                return;
//            }

            job.setContact(contact);

            job = jobDao.addJob(job);
            job.setJobComponentList(components);
            for (JobComponent jc : components) {
                List<ComponentTask> ctl = jobTaskMap.get(jc);
                jc.setJob(job);
                JobComponentPK jcp = jc.getJobComponentPK();
                jcp.setFkJobId(job.getJobId());
                jc.setJobComponentPK(jcp);
                try {
                    jobDao.addJobComponent(jc);
                } catch (Exception ex) {
                    System.err.println("could not add job component\n" + ex);
                    FormUtils.haltAlert("could not add job component");
                    return;
                }
                for (ComponentTask ct : ctl) {
                    ComponentTaskPK cpk = new ComponentTaskPK(job.getJobId(), jc.getJobComponentPK().getComponentId(), ct.getTask().getTaskId());
                    ct.setJobComponent(jc);
                    ct.setJobComponent(jc);
                    try {
                        jobDao.addComponentTask(ct);
                    } catch (Exception ex) {
                        System.err.println("could not add task\n" + ex);
                        FormUtils.haltAlert("could not add task");
                        return;
                    }
                }
            }
            FormUtils.haltAlert("Job successfully added");
            SceneController.switchScene(SceneController.Scenes.home);
//            for (JobComponent jc : components) {
//                jc.setJob(job);
//                JobComponentPK jcp = jc.getJobComponentPK();
//                jcp.setFkJobId(job.getJobId());
//                jc.setJobComponentPK(jcp);
//                for (ComponentTask ct : jc.getComponentTaskList()) {
////                    List<ComponentTask> ctl = jc.getComponentTaskList();
////                    ctl.remove(ct);
////                    jc.setComponentTaskList(ctl);
//                    ComponentTaskPK cpk = ct.getComponentTaskPK();
//                    cpk.setFkJobId(job.getJobId());
//                    ct.setComponentTaskPK(cpk);
//                    jc.getComponentTaskList().remove(ct);
////                    try {
////                        jobDao.addComponentTask(ct, ct.getTask(), jc);
////                    } catch (Exception ex) {
////                        FormUtils.haltAlert("Cannot update task");
////                        return;
////                    }
//                }
//            }
//            try {
//                jobDao.updateJob(job);
//                FormUtils.haltAlert("Job successfully added");
//                SceneController.switchScene(SceneController.Scenes.home);
//            } catch (NonexistentEntityException ex) {
//                FormUtils.haltAlert("Cannot add tasks, Job does not exist?");
//            } catch (Exception ex) {
//                FormUtils.haltAlert("Cannot add tasks, Job does not exist?");
//            }
        });

    }

    private String getText(TextField tf) {
        return tf.getText().trim();
    }

    private boolean isEmpty(String s) {
        return s.equals("");
    }

    private void isUrgent(boolean selected) {
        txtRecordDeadline.setDisable(!selected);
        txtExtraPercentage.setDisable(!selected);
    }

    private void updateTask() {
        taskDao.getTasks().forEach((t) -> {
            taskMap.put(t.getTaskId().toString(), t);
            tasks.add(t.getTaskId().toString());
        });
    }
}
