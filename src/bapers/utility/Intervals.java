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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author chris
 */
public class Intervals implements Serializable{

    private static final String URI_ = "intervals.dat";
    private static final int DAYS = 0, HOURS = 1, MINUTES = 0;
    private Times reportIntervals, backupIntervals;

    /**
     * set default backup and report intervals
     *
     */
    public Intervals() {
        reportIntervals = new Times(DAYS, HOURS, MINUTES);
        backupIntervals = new Times(DAYS, HOURS, MINUTES);
    }

    /**
     *
     * @return either the intervals object that has been serialized to disk, or 
     * a new intervals object
     */
    public static Intervals readIntervals() {
        Intervals intervals = null;
        
        try (FileInputStream fis = new FileInputStream(URI_);
                ObjectInputStream ois = new ObjectInputStream(fis);) {
            intervals = (Intervals) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Could not read intervals, using defaults instead\n");
            intervals = new Intervals();
            
            writeIntervals(intervals);
        } finally {
            return intervals;
        }
    }

    /**
     *
     * @param intervals the intervals object to serialize
     * @return true if the write was successful
     * @throws IOException if the file cannot be made
     */
    public static boolean writeIntervals(Intervals intervals) throws IOException {
        File file = new File(URI_);
        
        if (!file.exists())
            file.createNewFile();
        
        
        try (FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(intervals);
            return true;
        } catch (IOException ex) {
            System.err.println("Could not write intervals to file\n");
            return false;
        }
    }

    /**
     *
     * @param times - the intervals between database backups
     */
    public static void setMainBackupIntervals(Times times){
        bapers.BAPERS.intervals.setBackupIntervals(times);
    }
    
    /**
     *
     * @param times - the intervals between report generations
     */
    public static void setMainReportIntervals(Times times) {
        bapers.BAPERS.intervals.setReportIntervals(times);
    }
    
    /**
     *  sets the time when the backup was last generated
     */
    public void setBackupGenerated() {
        backupIntervals.setLastSet();
    }
    
    /**
     *  sets the time when the reports were last generated
     */
    public void setReportGenerated() {
        reportIntervals.setLastSet();
    }
    
    /**
     *
     * @return intervals between reports
     */
    public Times getReportIntervals() {
        return reportIntervals;
    }

    /**
     *
     * @param reportIntervals set the intervals between reports
     */
    public void setReportIntervals(Times reportIntervals) {
        this.reportIntervals = reportIntervals;
    }

    /**
     *
     * @return the intervals between backups
     */
    public Times getBackupIntervals() {
        return backupIntervals;
    }

    /**
     *
     * @param backupIntervals set the intervals between backups
     */
    public void setBackupIntervals(Times backupIntervals) {
        this.backupIntervals = backupIntervals;
    }

}
