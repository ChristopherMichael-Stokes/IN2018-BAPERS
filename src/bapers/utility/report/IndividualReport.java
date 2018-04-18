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
import javafx.beans.property.Property;
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

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code = new SimpleStringProperty(code);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price = new SimpleStringProperty(tf.getValueConverter().toString(price / 100));
    }

    public String getTask() {
        return task.get();
    }

    public void setTask(String task) {
        this.task = new SimpleStringProperty(task);
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department = new SimpleStringProperty(department);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(SimpleStringProperty startTime) {
        this.startTime = startTime;
    }

    public String getTimeTaken() {
        return timeTaken.get();
    }

    public void setTimeTaken(SimpleStringProperty timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getCompletedBy() {
        return completedBy.get();
    }

    public void setCompletedBy(SimpleStringProperty completedBy) {
        this.completedBy = completedBy;
    }

    public String getShelfOnCompletion() {
        return shelfOnCompletion.get();
    }

    public void setShelfOnCompletion(SimpleStringProperty shelfOnCompletion) {
        this.shelfOnCompletion = shelfOnCompletion;
    }
    
}
