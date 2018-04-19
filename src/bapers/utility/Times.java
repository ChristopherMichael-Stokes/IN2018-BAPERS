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
 *
 * @author chris
 */
public class Times implements Serializable {
    private static final long DAY = 86_400_000, HOUR = 3_600_000,
            MINUTE = 60_000;
    private long days, hours, minutes;
    private Date lastSet;
    
    /**
     *
     * @param days
     * @param hours
     * @param minutes
     */
    public Times(int days, int hours, int minutes) {
        this.days = days * DAY;
        this.hours = hours * HOUR;
        this.minutes = minutes * MINUTE;
        lastSet = new Date();
    }
    
    /**
     *
     * @param hours
     * @param minutes
     */
    public Times(int hours, int minutes) {
        this(0, hours, minutes);
    }
    
    /**
     *
     * @param minutes
     */
    public Times(int minutes) {
        this(0, 0, minutes);
    }

    /**
     *
     * @param days
     */
    public void setDays(int days) {
        this.days = days * DAY;        
        setLastSet();

    }

    /**
     *
     * @param hours
     */
    public void setHours(int hours) {
        this.hours = hours * HOUR;
        lastSet = new Date();
        setLastSet();

    }

    /**
     *
     * @param minutes
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes * MINUTE;
        setLastSet();
    }
    
    /**
     *
     * @return
     */
    public long getMilliseconds() {
        return days + hours + minutes;
    }

    /**
     *
     */
    public void setLastSet() {
        lastSet = new Date();
    }
    
    /**
     *
     * @return
     */
    public Date getLastSet() {
        return lastSet;
    }
}
