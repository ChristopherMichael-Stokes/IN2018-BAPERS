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
public class Shift {
    private SimpleStringProperty date;
    private SimpleStringProperty copyRoom;
    private SimpleStringProperty development;
    private SimpleStringProperty finishing;
    private SimpleStringProperty packing;
    
    public Shift(Object[] resultSet){
    this.date = new SimpleStringProperty(resultSet[0].toString());
    this.copyRoom = new SimpleStringProperty(resultSet[1].toString());
    this.development = new SimpleStringProperty(resultSet[2].toString());
    this.finishing = new SimpleStringProperty(resultSet[3].toString());
    this.packing = new SimpleStringProperty(resultSet[4].toString());
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(SimpleStringProperty date) {
        this.date = date;
    }

    public String getCopyRoom() {
        return copyRoom.get();
    }

    public void setCopyRoom(SimpleStringProperty copyRoom) {
        this.copyRoom = copyRoom;
    }

    public String getDevelopment() {
        return development.get();
    }

    public void setDevelopment(SimpleStringProperty development) {
        this.development = development;
    }

    public String getFinishing() {
        return finishing.get();
    }

    public void setFinishing(SimpleStringProperty finishing) {
        this.finishing = finishing;
    }

    public String getPacking() {
        return packing.get();
    }

    public void setPacking(SimpleStringProperty packing) {
        this.packing = packing;
    }
    
    

}
