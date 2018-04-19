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
package bapers.utility;

import java.io.Serializable;
import java.util.Date;

/**
 * holds information for a time period, in days, hours and minutes.
 * times are represented in milliseconds.
 * @author chris
 */
public class Times implements Serializable {
    private static final long DAY = 86_400_000, HOUR = 3_600_000,
            MINUTE = 60_000;
    private long days, hours, minutes;
    private Date lastSet;
    
    /**
     *
     * @param days for the period
     * @param hours for the period
     * @param minutes for the period
     */
    public Times(int days, int hours, int minutes) {
        this.days = days * DAY;
        this.hours = hours * HOUR;
        this.minutes = minutes * MINUTE;
        lastSet = new Date();
    }
    
    /**
     *
     * @param hours for the period
     * @param minutes for the periods
     */
    public Times(int hours, int minutes) {
        this(0, hours, minutes);
    }
    
    /**
     *
     * @param minutes for the period
     */
    public Times(int minutes) {
        this(0, 0, minutes);
    }

    /**
     *
     * @param days value to assign to current days field (in number of actual 
     * days, not milliseconds)
     */
    public void setDays(int days) {
        this.days = days * DAY;        
        setLastSet();

    }

    /**
     *
     * @param hours value to assign to current hours field (in number of actual 
     * hours, not milliseconds)
     */
    public void setHours(int hours) {
        this.hours = hours * HOUR;
        lastSet = new Date();
        setLastSet();

    }

    /**
     *
     * @param minutes value to assign to current minutes field (in number of actual 
     * minutes, not milliseconds)
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes * MINUTE;
        setLastSet();
    }
    
    /**
     *
     * @return the value of the times object in milliseconds
     */
    public long getMilliseconds() {
        return days + hours + minutes;
    }

    /**
     * set the last time the times object was accessed
     */
    public void setLastSet() {
        lastSet = new Date();
    }
    
    /**
     *
     * @return when the times object was last accessed
     */
    public Date getLastSet() {
        return lastSet;
    }
}
