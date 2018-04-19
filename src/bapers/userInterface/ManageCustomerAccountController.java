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
import bapers.data.dataAccess.AddressJpaController;
import bapers.data.dataAccess.CardDetailsJpaController;
import bapers.data.dataAccess.ComponentTaskJpaController;
import bapers.data.dataAccess.ContactJpaController;
import bapers.data.dataAccess.CustomerAccountJpaController;
import bapers.data.dataAccess.DiscountBandJpaController;
import bapers.data.dataAccess.JobComponentJpaController;
import bapers.data.dataAccess.JobJpaController;
import bapers.data.dataAccess.PaymentInfoJpaController;
import bapers.data.dataAccess.TaskDiscountJpaController;
import bapers.data.dataAccess.TaskJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.domain.Address;
import bapers.data.domain.AddressPK;
import bapers.data.domain.Contact;
import bapers.data.domain.ContactPK;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.DiscountBand;
import bapers.data.domain.DiscountBandPK;
import bapers.data.domain.Task;
import bapers.data.domain.TaskDiscount;
import bapers.data.domain.TaskDiscountPK;
import bapers.service.CustomerAccountService;
import bapers.service.CustomerAccountServiceImpl;
import bapers.service.TaskService;
import bapers.service.TaskServiceImpl;
import static bapers.userInterface.SceneController.switchScene;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ManageCustomerAccountController implements Initializable {

    @FXML
    private TextField txtVariablePercentage;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtPostCode;
    @FXML
    private TextField txtLandline;
    @FXML
    private TextField txtFlexiblePercentage;
    @FXML
    private TextField txtFixedPercentage;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtAddressLine2;
    @FXML
    private TextField txtAddressLine1;
    @FXML
    private TextField txtAccountName;
    @FXML
    private ScrollPane scpAccounts;
    @FXML
    private RadioButton rbVariable;
    @FXML
    private RadioButton rbFlexible;
    @FXML
    private RadioButton rbFixed;
    @FXML
    private ListView lsvTasks;
    @FXML
    private ListView lsvFlexible;
    @FXML
    private ListView lsvAccounts;
    @FXML
    private Label lblRegion;
    @FXML
    private Label lblPostcode;
    @FXML
    private Label lblLandline;
    @FXML
    private Label lblHomeCustomerAccounts;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblCustomerAccounts;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblAddress2;
    @FXML
    private Label lblAddress1;
    @FXML
    private Label lblAccountName;
    @FXML
    private ComboBox cbbVariable;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRemoveAccount;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnDowngrade;
    @FXML
    private Button btnActivateAccount;
    private CustomerAccountService customerAccountServiceDao = new CustomerAccountServiceImpl();
    private TaskService taskServiceDao = new TaskServiceImpl();
    private final ToggleGroup discountType = new ToggleGroup();
    @FXML
    private Label lblFirstName;
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblMobile;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtMobile;
    @FXML
    private Button btnUpdateDetails;
    @FXML
    private Button btnUpdateVar;
    @FXML
    private Button btnDeleteVar;
    @FXML
    private Button btnUpdateFlex;
    @FXML
    private Button btnDeleteFlex;
    @FXML
    private Button btnSetFixedDiscount;
    @FXML
    private ListView lvContact;
    private short editAccount = -1;
    private String surname;
    private AddressJpaController addressController = new AddressJpaController(EMF);
    private ContactJpaController contactController = new ContactJpaController(EMF);
    private Contact editContact = null;
    private CustomerAccountJpaController customerJpa = new CustomerAccountJpaController(EMF);
    private DiscountBandJpaController dbJpa = new DiscountBandJpaController(EMF);
    private TaskDiscountJpaController tdJpa = new TaskDiscountJpaController(EMF);
    private ComponentTaskJpaController ctJpa = new ComponentTaskJpaController(EMF);
    private JobComponentJpaController jcJpa = new JobComponentJpaController(EMF);
    private CardDetailsJpaController cdJpa = new CardDetailsJpaController(EMF);
    private PaymentInfoJpaController piJpa = new PaymentInfoJpaController(EMF);
    private JobJpaController jJpa = new JobJpaController(EMF);
    private TaskJpaController tJpa = new TaskJpaController(EMF);
    private short discountAccount = -1;
    boolean isUpdate = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(null);
        rbFixed.setToggleGroup(discountType);
        rbVariable.setToggleGroup(discountType);
        rbFlexible.setToggleGroup(discountType);
        lblFirstName.setText("First Name");
        lblSurname.setText("Surname");
        lblMobile.setText("Mobile");
        lblRegion.setText("Region");
        lblPostcode.setText("Post Code");
        lblLandline.setText("Landline");
        lblHomeCustomerAccounts.setText("Home>Customer Accounts");
        lblEmail.setText("Email");
        lblCustomerAccounts.setText("Customer Account");
        lblCity.setText("City");
        lblAddress2.setText("Address2");
        lblAddress1.setText("Address1");
        lblAccountName.setText("Account Number");
        btnHome.setOnAction((event) -> switchScene(SceneController.Scenes.home));
        btnSearch.setOnAction((event) -> {
            if (txtSearch.getText().trim().equals("")) {
                alert.setContentText("Search bar cannot be empty!");
                alert.showAndWait();
                turnAllTextBlank();
                setAllDiscountClear();
                editAccount = -1;
                surname = null;
                editContact = null;
                discountAccount = -1;
            } else {
                ObservableList<String> searchObservableList;
                List<String> searchList = new ArrayList<String>();
                customerAccountServiceDao.getCustomerAccounts().forEach((o)
                        -> {
                    if (o.getAccountHolderName().contains(txtSearch.getText().trim())) {
                        searchList.add(o.getAccountHolderName());
                    }
                });
                if (searchList.isEmpty()) {
                    lsvAccounts.setItems(null);
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("No entry for the word" + "(" + txtSearch.getText() + ")");
                    alert.showAndWait();
                    turnAllTextBlank();
                    setAllDiscountClear();
                    editAccount = -1;
                    surname = null;
                    editContact = null;
                    discountAccount = -1;

                } else {
                    searchObservableList = FXCollections.observableArrayList(searchList);
                    lsvAccounts.setItems(searchObservableList);
                    lsvAccounts.setOrientation(Orientation.VERTICAL);
                }
            }
        });
        lsvAccounts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldvalue, String newvalue) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o)
                        -> {
                    if (o.getAccountHolderName() == ov.getValue()) {
                        turnAllTextBlank();
                        getAllText(o);
                        editAccount = o.getAccountNumber();
                        discountAccount = o.getDiscountType();
                        surname = null;
                        editContact = null;
                    }
                });
            }
        });
        lvContact.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldvalue, String newvalue) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o)
                        -> {
                    o.getContactList().forEach((oC) -> {
                        if (oC.getContactPK().getForename().concat(" ").concat(oC.getContactPK().getSurname()).equals(ov.getValue())) {
                            getContact(oC);
                            editContact = oC;
                            surname = oC.getContactPK().getSurname();
                        }
                    });

                });
            }
        });
        ObservableList<Integer> taskOList;
        List<Integer> taskList = new ArrayList<Integer>();
        taskServiceDao.getTasks().forEach((o) -> {
            taskList.add(o.getTaskId());
        });
        taskOList = FXCollections.observableArrayList(taskList);
        cbbVariable.setItems(taskOList);

        btnUpdateDetails.setOnAction((event) -> {
            if (lsvAccounts.getItems() == null || editAccount == -1) {
                alert.setAlertType(Alert.AlertType.WARNING);

                alert.setContentText("You need to select a customer account!");
                alert.showAndWait();
            } else {

                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount && !isEmpty(txtAccountName)) {
                        CustomerAccount ca = o;
                        ca.setAccountHolderName(txtAccountName.getText());
                        ca.setEmail(txtEmail.getText());
                        ca.setLandline(txtLandline.getText());
                        try {
                            customerAccountServiceDao.updateAccount(ca);
                        } catch (NonexistentEntityException ex) {
                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (o.getAccountNumber() == editAccount && !isEmpty(txtFirstName) && !isEmpty(txtMobile) && !isEmpty(txtSurname)) {
                        try {
                            if (editContact != null) {
                                ContactPK newPK = new ContactPK();
                                contactController.destroy(editContact.getContactPK());
                                newPK.setForename(txtFirstName.getText());
                                newPK.setSurname(txtSurname.getText());
                                editContact.setContactPK(newPK);
                                editContact.setMobile(txtMobile.getText());
                            } else {
                                ContactPK newPK = new ContactPK();
                                editContact = new Contact();
                                newPK.setForename(txtFirstName.getText());
                                newPK.setSurname(txtSurname.getText());
                                editContact.setContactPK(newPK);
                                editContact.setMobile(txtMobile.getText());
                                editContact.setCustomerAccount(o);
                            }
                            try {
                                contactController.create(editContact);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } catch (IllegalOrphanException ex) {
                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NonexistentEntityException ex) {
                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (o.getAccountNumber() == editAccount && !haveAddress(o)) {
                        if (finishAddress()) {
                            AddressPK addressPK = new AddressPK();
                            addressPK.setAddressLine1(txtAddressLine1.getText());
                            addressPK.setCity(txtCity.getText());
                            addressPK.setPostcode(txtPostCode.getText());
                            Address address = new Address(addressPK);
                            address.setRegion(txtRegion.getText());
                            address.setAddressLine2(txtAddressLine2.getText());
                            address.setCustomerAccount(o);
                            try {
                                addressController.create(address);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {

                            if (!finishAddress()) {
                                alert.setAlertType(Alert.AlertType.WARNING);

                                alert.setContentText("Please fill in every details for address!");
                                alert.showAndWait();
                            }

                        }
                    } else if (haveAddress(o) && o.getAccountNumber() == editAccount) {
                        if (!finishAddress()) {
                            alert.setAlertType(Alert.AlertType.WARNING);
                            alert.setContentText("Please fill in every details for address!");
                            alert.showAndWait();
                        } else {
                            AddressPK addressPK = new AddressPK();
                            addressPK.setAddressLine1(txtAddressLine1.getText());
                            addressPK.setCity(txtCity.getText());
                            addressPK.setPostcode(txtPostCode.getText());
                            Address address = new Address();
                            address.setAddressPK(addressPK);
                            address.setRegion(txtRegion.getText());
                            address.setAddressLine2(txtAddressLine2.getText());
                            address.setCustomerAccount(o);
                            try {
                                addressController.destroy(o.getAddressList().get(0).getAddressPK());
                                addressController.create(address);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });

            }

        });

        btnRemoveAccount.setOnAction((event) -> {
            if (editAccount == -1) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("No customer account is selected!");
                alert.showAndWait();
            } else {
                try {
                    customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                        if (o.getAccountNumber() == editAccount) {
                            o.getAddressList().forEach((oA) -> {
                                try {
                                    addressController.destroy(oA.getAddressPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                            o.getContactList().forEach((oC) -> {
                                try {
                                    oC.getJobList().forEach((oJ) -> {
                                        oJ.getJobComponentList().forEach((oJC) -> {
                                            oJC.getComponentTaskList().forEach((oCT) -> {
                                                try {
                                                    ctJpa.destroy(oCT.getComponentTaskPK());
                                                } catch (NonexistentEntityException ex) {
                                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            });
                                            try {
                                                jcJpa.destroy(oJC.getJobComponentPK());
                                            } catch (IllegalOrphanException ex) {
                                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (NonexistentEntityException ex) {
                                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        });
                                        try {
                                            jJpa.destroy(oJ.getJobId());
                                        } catch (IllegalOrphanException ex) {
                                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (NonexistentEntityException ex) {
                                            Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                                    contactController.destroy(oC.getContactPK());
                                } catch (IllegalOrphanException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });

                            o.getDiscountBandList().forEach((oDB) -> {
                                try {
                                    dbJpa.destroy(oDB.getDiscountBandPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                            o.getTaskDiscountList().forEach((oTD) -> {
                                try {
                                    tdJpa.destroy(oTD.getTaskDiscountPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                        }
                    });
                    customerJpa.destroy(editAccount);
                    turnAllTextBlank();
                    editAccount = -1;
                    lsvAccounts.setItems(null);
                    lvContact.setItems(null);
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("Selected customer account has been deleted!");
                    alert.showAndWait();
                } catch (IllegalOrphanException ex) {
                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        btnDowngrade.setOnAction((event) -> {
            if (editAccount != -1) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount && o.getDiscountType() != (short) 0) {
                        o.getDiscountBandList().forEach((oDB) -> {
                            try {
                                dbJpa.destroy(oDB.getDiscountBandPK());
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        o.getTaskDiscountList().forEach((oTD) -> {
                            try {
                                tdJpa.destroy(oTD.getTaskDiscountPK());
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        o.setDiscountType((short) 0);
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Customer " + o.getAccountHolderName() + " has been downgrade!");
                        alert.showAndWait();
                    }
                });
            }
        });

        btnActivateAccount.setOnAction(((event) -> {
            customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                if (o.getAccountNumber() == editAccount && o.getLocked() != (short) 0) {
                    customerAccountServiceDao.setAccountActive(o, true);
                    try {
                        customerAccountServiceDao.updateAccount(o);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("Customer " + o.getAccountHolderName() + " account is no longer disable!");
                    alert.showAndWait();
                } else if (o.getAccountNumber() == editAccount && o.getLocked() == (short) 0) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("Customer " + o.getAccountHolderName() + " already activated!");
                    alert.showAndWait();
                }
            });

        }));

        lsvTasks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> ov, Integer oldvalue, Integer newvalue) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        o.getTaskDiscountList().forEach((oTD) -> {
                            if (oTD.getTask().getTaskId().equals(ov.getValue())) {
                                txtVariablePercentage.setText(String.valueOf(oTD.getPercentage()));
                            }
                        });
                    }

                });
            }

        });

        lsvFlexible.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> ov, Integer oldvalue, Integer newvalue) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        o.getDiscountBandList().forEach((oDB) -> {
                            if (oDB.getDiscountBandPK().getPrice() == (ov.getValue())) {
                                txtPrice.setText(String.valueOf(oDB.getDiscountBandPK().getPrice()));
                                txtFlexiblePercentage.setText(String.valueOf(oDB.getPercentage()));
                            }
                        });
                    }

                });
            }

        });

        rbVariable.setOnAction((event) -> {
            setDiscountClear();
        });

        rbFlexible.setOnAction((event) -> {
            setDiscountClear();
        });

        rbFixed.setOnAction((event) -> {
            setDiscountClear();
        });

        btnSetFixedDiscount.setOnAction((event) -> {
            if (!rbFixed.isSelected()) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Fixed discount is not selected!");
                alert.showAndWait();
            } else if (isEmpty(txtFixedPercentage)) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the percentage for fixed discount!");
                alert.showAndWait();
            } else if (isFloat(txtFixedPercentage.getText()) && Float.parseFloat(txtFixedPercentage.getText()) <= 100 && Float.parseFloat(txtFixedPercentage.getText()) >= 0) {

                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        if (o.getDiscountType() == (short) 1) {
                            o.getDiscountBandList().get(0).setPercentage(Float.parseFloat(txtFixedPercentage.getText()));
                            try {
                                dbJpa.edit(o.getDiscountBandList().get(0));
                                customerJpa.edit(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Fixed discount is Set!");
                                alert.showAndWait();
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (o.getDiscountType() == (short) 2) {
                            o.getTaskDiscountList().forEach((oTD) -> {
                                try {
                                    tdJpa.destroy(oTD.getTaskDiscountPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                            o.getTaskDiscountList().forEach((oa) -> {
                                System.out.println(oa.getTaskDiscountPK().getFkAccountNumber());
                            });
                            o.getTaskDiscountList().clear();

                            if (isFloat(txtFixedPercentage.getText())) {
                                List<DiscountBand> dbList = new ArrayList<DiscountBand>();
                                DiscountBand discountBand = new DiscountBand();
                                DiscountBandPK discountBandPK = new DiscountBandPK();
                                discountBandPK.setPrice(0);
                                discountBand.setCustomerAccount(o);
                                discountBand.setPercentage(Float.parseFloat(txtFixedPercentage.getText()));
                                discountBand.setDiscountBandPK(discountBandPK);
                                dbList.add(discountBand);
                                try {
                                    dbJpa.create(discountBand);
                                    o.setDiscountType((short) 1);
                                    o.getDiscountBandList().add(discountBand);
                                    customerAccountServiceDao.updateAccount(o);
                                    alert.setAlertType(Alert.AlertType.INFORMATION);
                                    alert.setContentText("Fixed discount is Set!");
                                    alert.showAndWait();
                                } catch (Exception ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        }

                    } else if (o.getDiscountType() == (short) 3) {
                        if (isFloat(txtFixedPercentage.getText())) {
                            DiscountBand discountBand = new DiscountBand();
                            DiscountBandPK discountBandPK = new DiscountBandPK();
                            discountBandPK.setPrice(0);
                            discountBand.setCustomerAccount(o);
                            discountBand.setPercentage(Float.parseFloat(txtFixedPercentage.getText()));
                            discountBand.setDiscountBandPK(discountBandPK);
                            o.getDiscountBandList().forEach((oDB) -> {
                                try {
                                    dbJpa.destroy(oDB.getDiscountBandPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                            o.getDiscountBandList().clear();
                            try {
                                dbJpa.create(discountBand);
                                o.setDiscountType((short) 1);
                                o.getDiscountBandList().add(discountBand);
                                customerJpa.edit(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Fixed discount is Set!");
                                alert.showAndWait();
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    } else if (o.getAccountNumber() == editAccount && o.getDiscountType() == (short) 0) {
                        if (isFloat(txtFixedPercentage.getText())) {
                            DiscountBand discountBand = new DiscountBand();
                            DiscountBandPK discountBandPK = new DiscountBandPK();
                            discountBandPK.setPrice(0);
                            discountBand.setCustomerAccount(o);
                            discountBand.setPercentage(Float.parseFloat(txtFixedPercentage.getText()));
                            discountBand.setDiscountBandPK(discountBandPK);
                            try {
                                dbJpa.create(discountBand);
                                o.setDiscountType((short) 1);
                                o.getDiscountBandList().add(discountBand);
                                customerJpa.edit(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Fixed discount is Set!");
                                alert.showAndWait();
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in percentage between 0-100!");
                alert.showAndWait();
            }
        }
        );

        btnUpdateVar.setOnAction((event) -> {
            if (rbVariable.isSelected() && !cbbVariable.getSelectionModel().isEmpty() && isFloat(txtVariablePercentage.getText())) {
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        if (o.getDiscountType() == (short) 0) {
                            TaskDiscount taskDiscount = new TaskDiscount();
                            TaskDiscountPK taskDiscountPK = new TaskDiscountPK();
                            taskDiscount.setCustomerAccount(o);
                            taskDiscount.setPercentage(Float.parseFloat(txtVariablePercentage.getText()));
                            taskDiscount.setTask(tJpa.findTask(Integer.parseInt(cbbVariable.getValue().toString())));
                            taskDiscount.setTaskDiscountPK(taskDiscountPK);
                            try {
                                tdJpa.create(taskDiscount);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            o.setDiscountType((short) 2);
                            try {
                                customerAccountServiceDao.updateAccount(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Variable discount is set!");
                                alert.showAndWait();
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (o.getDiscountType() == (short) 1) {
                            try {
                                dbJpa.destroy(o.getDiscountBandList().get(0).getDiscountBandPK());
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            o.getDiscountBandList().clear();

                            TaskDiscount taskDiscount = new TaskDiscount();
                            TaskDiscountPK taskDiscountPK = new TaskDiscountPK();
                            taskDiscount.setCustomerAccount(o);
                            taskDiscount.setPercentage(Float.parseFloat(txtVariablePercentage.getText()));
                            taskDiscount.setTask(tJpa.findTask(Integer.parseInt(cbbVariable.getValue().toString())));
                            taskDiscount.setTaskDiscountPK(taskDiscountPK);
                            try {
                                tdJpa.create(taskDiscount);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            o.setDiscountType((short) 2);
                            o.getTaskDiscountList().add(taskDiscount);

                            try {
                                customerAccountServiceDao.updateAccount(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Variable discount is set!");
                                alert.showAndWait();
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (o.getDiscountType() == (short) 2) {

                            o.getTaskDiscountList().forEach((oTD) -> {
                                if (oTD.getTaskDiscountPK().getFkTaskId() == Integer.parseInt(cbbVariable.getValue().toString())) {
                                    isUpdate = true;
                                    oTD.setPercentage(Float.parseFloat(txtVariablePercentage.getText()));
                                    try {
                                        tdJpa.edit(oTD);
                                    } catch (Exception ex) {
                                        Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });

                            if (isUpdate) {
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Variable discount is set!");
                                alert.showAndWait();
                                isUpdate = false;
                            } else {
                                TaskDiscount taskDiscount = new TaskDiscount();
                                TaskDiscountPK taskDiscountPK = new TaskDiscountPK();
                                taskDiscount.setCustomerAccount(o);
                                taskDiscount.setPercentage(Float.parseFloat(txtVariablePercentage.getText()));
                                taskDiscount.setTask(tJpa.findTask(Integer.parseInt(cbbVariable.getValue().toString())));
                                taskDiscount.setTaskDiscountPK(taskDiscountPK);
                                try {
                                    tdJpa.create(taskDiscount);
                                } catch (Exception ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                o.getTaskDiscountList().add(taskDiscount);
                                try {
                                    customerAccountServiceDao.updateAccount(o);
                                    alert.setAlertType(Alert.AlertType.INFORMATION);
                                    alert.setContentText("Variable discount is set!");
                                    alert.showAndWait();
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (Exception ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        } else if (o.getDiscountType() == (short) 3) {

                            o.getDiscountBandList().forEach((oDB -> {
                                try {
                                    dbJpa.destroy(oDB.getDiscountBandPK());
                                } catch (Exception ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }));
                            o.getDiscountBandList().clear();

                            TaskDiscount taskDiscount = new TaskDiscount();
                            TaskDiscountPK taskDiscountPK = new TaskDiscountPK();
                            taskDiscount.setCustomerAccount(o);
                            taskDiscount.setPercentage(Float.parseFloat(txtVariablePercentage.getText()));
                            taskDiscount.setTask(tJpa.findTask(Integer.parseInt(cbbVariable.getValue().toString())));
                            taskDiscount.setTaskDiscountPK(taskDiscountPK);
                            try {
                                tdJpa.create(taskDiscount);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            o.setDiscountType((short) 2);
                            o.getTaskDiscountList().add(taskDiscount);
                            try {
                                customerAccountServiceDao.updateAccount(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Variable discount is set!");
                                alert.showAndWait();
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                });

            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Please select the TaskID and fill in percentage between 0-100!");
                alert.showAndWait();
            }

        });

        btnDeleteVar.setOnAction((event) -> {
            if (!rbVariable.isSelected() || cbbVariable.getSelectionModel().isEmpty()) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("No Task is selected for delete!");
                alert.showAndWait();
            } else {
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        o.getTaskDiscountList().forEach((oTD) -> {
                            if (oTD.getTaskDiscountPK().getFkTaskId() == Integer.parseInt(cbbVariable.getValue().toString())) {
                                try {
                                    tdJpa.destroy(oTD.getTaskDiscountPK());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }

                });
                customerAccountServiceDao.getCustomerAccounts().forEach((o) -> {
                    if (o.getAccountNumber() == editAccount) {
                        if (o.getTaskDiscountList().isEmpty()) {
                            o.setDiscountType((short) 0);
                            try {
                                customerAccountServiceDao.updateAccount(o);
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Value Customer "+o.getAccountHolderName()+" has been downgrade due to lack of discount plan!");
                                alert.showAndWait();
                            } catch (NonexistentEntityException ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ManageCustomerAccountController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        else{
                            alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Task discount has been remove!");
                                alert.showAndWait();
                        }

                    }

                });
            }
        });

    }

    private void turnAllTextBlank() {
        txtRegion.clear();
        txtPostCode.clear();
        txtCity.clear();
        txtAddressLine1.clear();
        txtAddressLine2.clear();
        txtLandline.clear();
        txtEmail.clear();
        txtAccountName.clear();
        lvContact.setItems(null);
        txtFirstName.clear();
        txtSurname.clear();
        txtMobile.clear();

    }

    private void getAllText(CustomerAccount o) {
        List<Address> al = o.getAddressList();
        if (!al.isEmpty()) {
            txtRegion.setText(al.get(0).getRegion());
            txtPostCode.setText(al.get(0).getAddressPK().getPostcode());
            txtCity.setText(al.get(0).getAddressPK().getCity());
            txtAddressLine1.setText(al.get(0).getAddressPK().getAddressLine1());
            txtAddressLine2.setText(al.get(0).getAddressLine2());
        }
        txtLandline.setText(o.getLandline());
        txtEmail.setText(o.getEmail());
        txtAccountName.setText(o.getAccountHolderName());
        ObservableList<String> contactObservableList;
        List<String> contactList = new ArrayList<String>();
        o.getContactList().forEach((oC) -> {
            contactList.add(oC.getContactPK().getForename().concat(" ").concat(oC.getContactPK().getSurname()));
        });
        contactObservableList = FXCollections.observableArrayList(contactList);
        lvContact.setItems(contactObservableList);
        if (o.getDiscountType() == (short) 0) {
            setAllDiscountClear();
        }
        if (o.getDiscountType() != (short) 0) {
            if (o.getDiscountType() == (short) 1) {
                setAllDiscountClear();
                rbFixed.setSelected(true);
                txtFixedPercentage.setText(String.valueOf(o.getDiscountBandList().get(0).getPercentage()));
            } else if (o.getDiscountType() == (short) 2) {
                setAllDiscountClear();
                List<Integer> discountTaskIDList = new ArrayList<Integer>();
                ObservableList<Integer> taskIDObservableList;
                rbVariable.setSelected(true);
                o.getTaskDiscountList().forEach((oTD) -> {
                    discountTaskIDList.add(oTD.getTask().getTaskId());
                });
                taskIDObservableList = FXCollections.observableArrayList(discountTaskIDList);
                lsvTasks.setItems(taskIDObservableList);

            } else if (o.getDiscountType() == (short) 3) {
                setAllDiscountClear();
                List<Integer> discountPriceList = new ArrayList<Integer>();
                ObservableList<Integer> discountPriceObservableList;
                rbFlexible.setSelected(true);
                o.getDiscountBandList().forEach((oDB) -> {
                    discountPriceList.add(oDB.getDiscountBandPK().getPrice());
                });
                discountPriceObservableList = FXCollections.observableArrayList(discountPriceList);
                lsvFlexible.setItems(discountPriceObservableList);
            }

        }

    }

    private void getContact(Contact o) {
        txtFirstName.setText(o.getContactPK().getForename());
        txtSurname.setText(o.getContactPK().getSurname());
        txtMobile.setText(o.getMobile());

    }

    private boolean isEmpty(TextField tf) {
        return tf.getText().trim().equals("");
    }

    private boolean haveAddress(CustomerAccount o) {
        return !o.getAddressList().isEmpty();
    }

    private boolean finishAddress() {
        if (isEmpty(txtAddressLine1)) {
            return false;
        }
        if (isEmpty(txtCity)) {
            return false;
        }
        if (isEmpty(txtPostCode)) {
            return false;
        }
        if (isEmpty(txtRegion)) {
            return false;
        } else {
            return true;
        }

    }

    private void setAllDiscountClear() {
        rbVariable.setSelected(false);
        rbFixed.setSelected(false);
        rbFlexible.setSelected(false);
        lsvTasks.setItems(null);
        lsvFlexible.setItems(null);
        txtVariablePercentage.clear();
        txtFixedPercentage.clear();
        txtFlexiblePercentage.clear();
        txtPrice.clear();
        cbbVariable.getSelectionModel().clearSelection();
    }

    private void setDiscountClear() {
        lsvTasks.setItems(null);
        lsvFlexible.setItems(null);
        txtVariablePercentage.clear();
        txtFixedPercentage.clear();
        txtFlexiblePercentage.clear();
        txtPrice.clear();
        cbbVariable.getSelectionModel().clearSelection();
    }

    private static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
