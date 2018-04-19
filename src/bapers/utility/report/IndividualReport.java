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
package bapers.utility.report;

import bapers.utility.CurrencyFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextFormatter;

/**
 *
 * @author EdgarLaw
 */
public class IndividualReport {

    private final TextFormatter tf = new CurrencyFormat();
    private SimpleStringProperty code;
    private SimpleStringProperty price;
    private SimpleStringProperty task;
    private SimpleStringProperty department;
    private SimpleStringProperty startTime;
    private SimpleStringProperty timeTaken;
    private SimpleStringProperty completedBy;
    private SimpleStringProperty shelfOnCompletion;

    /**
     *
     * @param resultSet,generated from the database that contains data object of Individual PerformanceReport.
     */
    public IndividualReport(Object[] resultSet) {
        this.code = new SimpleStringProperty(resultSet[0].toString());
        int price_ = Integer.parseInt(resultSet[1].toString()) / 100;
        this.price = new SimpleStringProperty(tf.getValueConverter().toString((double) price_));
        this.task = new SimpleStringProperty(resultSet[2].toString());
        this.department = new SimpleStringProperty(resultSet[3].toString());
        this.startTime = new SimpleStringProperty(resultSet[4].toString());
        this.timeTaken = new SimpleStringProperty(resultSet[5].toString());
        this.completedBy = new SimpleStringProperty(resultSet[6].toString());
        this.shelfOnCompletion = new SimpleStringProperty(resultSet[7].toString());
    }

    /**
     *
     * @return String code which representing colum code in the Individual Performance Report.
     */
    public String getCode() {
        return code.get();
    }

    /**
     *
     * @param code, a SimpleStringProperty, in here is the data for colum code retrieve from the database for Individual Performance Report.
     */
    public void setCode(String code) {
        this.code = new SimpleStringProperty(code);
    }

    /**
     *
     * @return String price which representing colum price in the Individual Performance Report.
     */
    public String getPrice() {
        return price.get();
    }

    /**
     *
     * @param price, a SimpleStringProperty, in here is the data for colum price retrieve from the database for Individual Performance Report.
     */
    public void setPrice(double price) {
        this.price = new SimpleStringProperty(tf.getValueConverter().toString(price / 100));
    }

    /**
     *
     * @return String task which representing colum task in the Individual Performance Report.
     */
    public String getTask() {
        return task.get();
    }

    /**
     *
     * @param task, a SimpleStringProperty, in here is the data for colum task retrieve from the database for Individual Performance Report.
     */
    public void setTask(String task) {
        this.task = new SimpleStringProperty(task);
    }

    /**
     *
     * @return String department which representing colum department in the Individual Performance Report.
     */
    public String getDepartment() {
        return department.get();
    }

    /**
     *
     * @param department, a SimpleStringProperty, in here is the data for colum department retrieve from the database for Individual Performance Report.
     */
    public void setDepartment(String department) {
        this.department = new SimpleStringProperty(department);
    }

    /**
     *
     * @return String startTime which representing colum startTime in the Individual Performance Report.
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     *
     * @param startTime, a SimpleStringProperty, in here is the data for colum startTime retrieve from the database for Individual Performance Report.
     */
    public void setStartTime(SimpleStringProperty startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return String timeTaken which representing colum timeTaken in the Individual Performance Report.
     */
    public String getTimeTaken() {
        return timeTaken.get();
    }

    /**
     *
     * @param timeTaken, a SimpleStringProperty, in here is the data for colum timeTaken retrieve from the database for Individual Performance Report.
     */
    public void setTimeTaken(SimpleStringProperty timeTaken) {
        this.timeTaken = timeTaken;
    }

    /**
     *
     * @return String completedBy which representing colum completedBy in the Individual Performance Report.
     */
    public String getCompletedBy() {
        return completedBy.get();
    }

    /**
     *
     * @param completedBy, a SimpleStringProperty, in here is the data for colum completedBy retrieve from the database for Individual Performance Report.
     */
    public void setCompletedBy(SimpleStringProperty completedBy) {
        this.completedBy = completedBy;
    }

    /**
     *
     * @return String shelfOnCompletion which representing colum shelfOnCompletion in the Individual Performance Report.
     */
    public String getShelfOnCompletion() {
        return shelfOnCompletion.get();
    }

    /**
     *
     * @param shelfOnCompletion, a SimpleStringProperty, in here is the data for colum shelfOnCompletion retrieve from the database for Individual Performance Report.
     */
    public void setShelfOnCompletion(SimpleStringProperty shelfOnCompletion) {
        this.shelfOnCompletion = shelfOnCompletion;
    }
    
}
