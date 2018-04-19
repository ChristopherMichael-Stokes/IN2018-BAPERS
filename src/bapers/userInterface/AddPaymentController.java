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

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.PaymentService;
import bapers.service.PaymentServiceImpl;
import bapers.userInterface.SceneController.Scenes;
import static bapers.userInterface.SceneController.switchScene;
import bapers.utility.CurrencyFormat;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class AddPaymentController implements Initializable {

    @FXML
    private Button btnHome;
    @FXML
    private Label lblPaymentDue;
    @FXML
    private DatePicker dteDatePaid;
    @FXML
    private TextField txtPaymentAmount;
    @FXML
    private RadioButton rbCash;
    @FXML
    private RadioButton rbCard;
    @FXML
    private ComboBox<String> cmbCardType;
    @FXML
    private TextField txtCardNumber;
    @FXML
    private TextField txtExpiry;
    @FXML
    private Button btnAddPayment;
    @FXML
    private ListView<CheckBox> lsvJobs;
    @FXML
    private ComboBox<String> cmbCustomer;
    
    private final CustomerAccountService customerDao 
            = new CustomerAccountServiceImpl();
    private final PaymentService paymentDao
            = new PaymentServiceImpl();
    private final ToggleGroup paymentType = new ToggleGroup();
    
    private final Map<String, CustomerAccount> custMap = new HashMap<>();
    private final Map<CheckBox, Job> jobMap = new HashMap<>();
    private final ObservableList<CheckBox> jobs = FXCollections.observableArrayList();
    
    private static final DateFormat DF = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat strictZeroDecimalFormat  
                = new DecimalFormat("\u00A3###,###.##");
    private final TextFormatter<Double> tf = new CurrencyFormat();


    /**
     * Initializes the controller class.
     * @param url is the directory used to retrieve the .fxml files which contain the gui
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //radio buttons 
        rbCard.setToggleGroup(paymentType);
        rbCard.setOnAction((event) -> {
            cmbCardType.setDisable(false);
            txtCardNumber.setEditable(true);
            txtExpiry.setEditable(true);
        });
        
        rbCash.setToggleGroup(paymentType);
        rbCash.setOnAction((event) -> {
            cmbCardType.setDisable(true);
            txtCardNumber.setEditable(false);
            txtExpiry.setEditable(false);
        });
        paymentType.selectToggle(rbCash);
        rbCash.setSelected(true);
        cmbCardType.setDisable(true);
        txtCardNumber.setEditable(false);
        txtExpiry.setEditable(false);
        
        //Text
//        txtPaymentAmount.setTextFormatter(new CurrencyFormatter());
        txtPaymentAmount.setTextFormatter(tf);
        setTextLimit(txtCardNumber, 4);
        setTextLimit(txtExpiry, 5);
        
        //Lists        
        cmbCardType.getItems().addAll("Visa", "MasterCard", "Debit");   
        loadCustomers();
        lsvJobs.setItems(jobs);
        cmbCustomer.setOnAction((event) -> {
            updatePayment();
            jobMap.clear();
            jobs.clear();            
            loadJobs();    
            updatePayment();
        });
        
        //buttons
        btnHome.setOnAction((event) -> switchScene(Scenes.home));

        btnAddPayment.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle(null);
            
            if (dteDatePaid.getValue() == null) {
                alert.setContentText("You must set a transaction date");
                alert.showAndWait();
            } else if (txtPaymentAmount.getText().trim().equals("")) {
                alert.setContentText("You must add a payment amount");
                alert.showAndWait();                
            } 
            
            Date datePaid = Date.from(dteDatePaid.getValue()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
            int paymentAmount = (int)Double.parseDouble(txtPaymentAmount
                                .getTextFormatter().valueProperty().asString()
                                .get())*100;
            
            List<Job> paidJobs = new ArrayList<>();
            jobMap.forEach((k, v) -> {
                if (k.isSelected())
                    paidJobs.add(v);
                jobs.clear();
            });
            if (rbCash.isSelected()) {
                try {//TODO
                    paymentDao.addPayment(datePaid, 
                        (int)Double.parseDouble(txtPaymentAmount
                                .getTextFormatter().valueProperty().asString()
                                .get())*100, 
                        paidJobs);
                    txtPaymentAmount.setText("");
                    dteDatePaid.setValue(null);
                } catch (Exception ex) {
                    Logger.getLogger(AddPaymentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (rbCard.isSelected()) {
                if (txtCardNumber.getText().trim().equals("") 
                        || txtCardNumber.getText().trim().equals("")
                        || cmbCardType.getValue() == null){
                    alert.setContentText("Please supply card details");
                    alert.showAndWait();
                } else {
                    String cardNumber = txtCardNumber.getText().trim();
                    String[] expiryDate = txtExpiry.getText().trim().split("/");
                    
                    try {
                        short m = Short.parseShort(expiryDate[0]);
                        short y = Short.parseShort(expiryDate[1]);
                        if (m > 12)
                            throw new NumberFormatException();
                        paymentDao.addPayment(datePaid, paymentAmount, cardNumber, 
                                expiryDate[0]+expiryDate[1], 
                                cmbCardType.getValue(), paidJobs);
                    } catch (NumberFormatException ex) {
                        alert.setContentText("enter a valid date");
                        alert.showAndWait();
                        txtExpiry.setText("");
                        return;
                    } catch (IllegalOrphanException ex) {
                        Logger.getLogger(AddPaymentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(AddPaymentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            int selection = cmbCustomer.getSelectionModel().getSelectedIndex();
            loadCustomers();
            cmbCustomer.getSelectionModel().select(0);
            loadJobs();
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
    
    private void loadJobs() {
        if (cmbCustomer.getSelectionModel().isEmpty())
            return;
        ObservableList<Job> unpaidJobs =                
                paymentDao.getUnpaidJobs(custMap.get(cmbCustomer.getValue()).getAccountNumber());
               
        jobs.clear();
        jobMap.clear();
        for (Job job : unpaidJobs) {
            CheckBox cbox = new CheckBox(DF.format(job.getDateIssued()));
            cbox.setOnAction((event) -> updatePayment());
            jobMap.put(cbox, job);
            jobs.add(cbox);
        }
    }
    
    private void updatePayment() {
        double cost = jobs.stream().filter(cb -> cb.isSelected())
                    .map(cb -> jobMap.get(cb))
                    .mapToDouble(j -> paymentDao.getJobCost(j.getJobId()))
                    .reduce(0, (a, b) -> a + b);
        lblPaymentDue.setText("Payment Due: "+tf.getValueConverter().toString(cost));
    }
    
    private void setTextLimit(TextField textField, int length) {
        int ln = length - 1;
        textField.setOnKeyTyped(event -> {
            String string = textField.getText();

            if (string.length() > ln) {
                textField.setText(string.substring(0, ln));
                textField.positionCaret(string.length());
            }
        });
    }
}

