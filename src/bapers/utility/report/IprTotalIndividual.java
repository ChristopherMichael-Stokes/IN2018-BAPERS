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

/**
 *
 * @author chris
 */
public class IprTotalIndividual {
    private String name, time;

    /**
     *
     * @param name, a String, in here is the data for colum name retrieve from the database for Individual Performance Report.
     * @param time, a String, in here is the data for colum time retrieve from the database for Individual Performance Report.
     */
    public IprTotalIndividual(String name, String time) {
        this.name = name;
        this.time = time;
    }

    /**
     *
     * @return String name which representing colum name in the Individual Performance Report.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name,String, in here is the data for colum name retrieve from the database for Individual Performance Report.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return String time which representing colum time in the Individual Performance Report.
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time, a String, in here is the data for colum time retrieve from the database for Individual Performance Report.
     */
    public void setTime(String time) {
        this.time = time;
    }
}
