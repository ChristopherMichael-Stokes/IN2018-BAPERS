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
     * @param resultSet
     */
    public IndividualPerformanceReport(Object[] resultSet) {
        if (!(resultSet.length < 7)) {
            this.name = new SimpleStringProperty(resultSet[0].toString());
            this.code = new SimpleStringProperty(resultSet[1].toString());
            this.taskID = new SimpleStringProperty(resultSet[2].toString());
            this.department = new SimpleStringProperty(resultSet[3].toString());
            this.date = new SimpleStringProperty(resultSet[4].toString());
            this.startTime = new SimpleStringProperty(resultSet[5].toString());
            this.timeTaken = new SimpleStringProperty(resultSet[6].toString());
        }
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param name
     */
    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code.get();
    }

    /**
     *
     * @param code
     */
    public void setCode(SimpleStringProperty code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getTaskID() {
        return taskID.get();
    }

    /**
     *
     * @param taskID
     */
    public void setTaskID(SimpleStringProperty taskID) {
        this.taskID = taskID;
    }

    /**
     *
     * @return
     */
    public String getDepartment() {
        return department.get();
    }

    /**
     *
     * @param department
     */
    public void setDepartment(SimpleStringProperty department) {
        this.department = department;
    }

    /**
     *
     * @return
     */
    public String getDate() {
        return date.get();
    }

    /**
     *
     * @param date
     */
    public void setDate(SimpleStringProperty date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     *
     * @param startTime
     */
    public void setStartTime(SimpleStringProperty startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     */
    public String getTimeTaken() {
        return timeTaken.get();
    }

    /**
     *
     * @param timeTaken
     */
    public void setTimeTaken(SimpleStringProperty timeTaken) {
        this.timeTaken = timeTaken;
    }

}
