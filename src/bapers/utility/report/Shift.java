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
package bapers.utility.report;

import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author chris
 */
public class Shift extends TotalShift{
    
    private SimpleStringProperty date;
    
    /**
     *
     * @param resultSet,generated from the database that contains data object of Individual PerformanceReport.
     */
    public Shift(Object[] resultSet) {
        super(Arrays.copyOfRange(resultSet, 1, resultSet.length));
        this.date = new SimpleStringProperty(stringResult(resultSet[0]));        
    }

    private String stringResult(Object o) {
        return o == null ? "No data" : o.toString();
    }
    
    /**
     *
     * @return String code which representing colum date in the Individual Performance Report.
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
    
}
