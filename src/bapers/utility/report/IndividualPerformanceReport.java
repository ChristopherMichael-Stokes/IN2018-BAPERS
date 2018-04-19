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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author EdgarLaw
 */
public class IndividualPerformanceReport {

    private SimpleStringProperty name;
    private SimpleStringProperty code;
    private SimpleStringProperty taskID;
    private SimpleStringProperty department;
    private SimpleStringProperty date;
    private SimpleStringProperty startTime;
    private SimpleStringProperty timeTaken;

    /**
     *
     * @param resultSet,generated from the database that contains data object of Individual PerformanceReport.
     */
    public IndividualPerformanceReport(Object[] resultSet) {
        if (!(resultSet.length < 7)) {
            this.name = new SimpleStringProperty(getValue(resultSet[0]));
            this.code = new SimpleStringProperty(getValue(resultSet[1]));
            this.taskID = new SimpleStringProperty(getValue(resultSet[2]));
            this.department = new SimpleStringProperty(getValue(resultSet[3]));
            this.date = new SimpleStringProperty(getValue(resultSet[4]));
            this.startTime = new SimpleStringProperty(getValue(resultSet[5]));
            this.timeTaken = new SimpleStringProperty(getValue(resultSet[6]));
        }
    }
    
    private String getValue(Object o) {
        if (o == null) {
            return "No data";
        } else {
            return o.toString();
        }
    }

    /**
     *
     * @return String name which representing colum name in the Individual Performance Report.
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param name, a SimpleStringProperty, in here is the data for colum name retrieve from the database for Individual Performance Report.
     */
    public void setName(SimpleStringProperty name) {
        this.name = name;
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
    public void setCode(SimpleStringProperty code) {
        this.code = code;
    }

    /**
     *
     * @return String taskID which representing colum taskID in the Individual Performance Report.
     */
    public String getTaskID() {
        return taskID.get();
    }

    /**
     *
     * @param taskID, a SimpleStringProperty, in here is the data for colum taskID retrieve from the database for Individual Performance Report.
     */
    public void setTaskID(SimpleStringProperty taskID) {
        this.taskID = taskID;
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
    public void setDepartment(SimpleStringProperty department) {
        this.department = department;
    }

    /**
     *
     * @return String date which representing colum date in the Individual Performance Report.
     */
    public String getDate() {
        return date.get();
    }

    /**
     *
     * @param date, a SimpleStringProperty, in here is the data for colum date retrieve from the database for Individual Performance Report.
     */
    public void setDate(SimpleStringProperty date) {
        this.date = date;
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

}
