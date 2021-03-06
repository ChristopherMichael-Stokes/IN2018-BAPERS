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
public class TotalShift {

    private SimpleStringProperty copyRoom;
    private SimpleStringProperty development;
    private SimpleStringProperty finishing;
    private SimpleStringProperty packing;

    /**
     *
     * @param resultSet,generated from the database that contains data object of Individual PerformanceReport.
     */
    public TotalShift(Object[] resultSet) {
        this.copyRoom = new SimpleStringProperty(stringResult(resultSet[0]));
        this.development = new SimpleStringProperty(stringResult(resultSet[1]));
        this.finishing = new SimpleStringProperty(stringResult(resultSet[2]));
        this.packing = new SimpleStringProperty(stringResult(resultSet[3]));
    }

    private String stringResult(Object o) {
        return o == null ? "No data" : o.toString();
    }
    
    /**
     *
     * @return String copyRoom which representing colum copyRoom in the Individual Performance Report.
     */
    public String getCopyRoom() {
        return copyRoom.get();
    }

    /**
     *
     * @param copyRoom, a SimpleStringProperty, in here is the data for colum copyRoom retrieve from the database for Individual Performance Report.
     */
    public void setCopyRoom(SimpleStringProperty copyRoom) {
        this.copyRoom = copyRoom;
    }

    /**
     *
     * @return String development which representing colum development in the Individual Performance Report.
     */
    public String getDevelopment() {
        return development.get();
    }

    /**
     *
     * @param development, a SimpleStringProperty, in here is the data for colum development retrieve from the database for Individual Performance Report.
     */
    public void setDevelopment(SimpleStringProperty development) {
        this.development = development;
    }

    /**
     *
     * @return String finishing which representing colum finishing in the Individual Performance Report.
     */
    public String getFinishing() {
        return finishing.get();
    }

    /**
     *
     * @param finishing, a SimpleStringProperty, in here is the data for colum finishing retrieve from the database for Individual Performance Report.
     */
    public void setFinishing(SimpleStringProperty finishing) {
        this.finishing = finishing;
    }

    /**
     *
     * @return String packing which representing colum packing in the Individual Performance Report.
     */
    public String getPacking() {
        return packing.get();
    }

    /**
     *
     * @param packing, a SimpleStringProperty, in here is the data for colum packing retrieve from the database for Individual Performance Report.
     */
    public void setPacking(SimpleStringProperty packing) {
        this.packing = packing;
    }

}
