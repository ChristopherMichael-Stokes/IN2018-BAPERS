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
package bapers.service;

import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.Task;
import java.util.Date;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface JobService {
    
    public static enum Jobs {
        all() {{j = "All jobs";}}, complete() {{j = "Only complete jobs";}}, 
        incomplete() {{j = "Only incomplete jobs";}};
        protected String j;
        
        @Override
        public String toString() {
            return j;
        }
        
        public Jobs getJobsType(String jobsType) {
            switch(jobsType) {
                case "All jobs": return all;
                case "Only complete jobs": return complete;
                case "Only incomplete jobs": return incomplete;
                default: return all;
            }
        }
    }
    public boolean jobComplete(Job job);
    public ObservableList<Job> getJobs(CustomerAccount account, Jobs jobType);
    public boolean taskExists(int taskId);
    public Task getTask(int taskId);
    public void removeComponentTask(ComponentTask ct) throws NonexistentEntityException;
    public void updateTask(ComponentTask ct) throws NonexistentEntityException, Exception;
    public JobComponent getComponent(String componentId, int jobId);
    public void addComponentTask(ComponentTask ct, Task t, JobComponent jc) throws PreexistingEntityException, Exception;
    /**
     * @param taskId
     * @param jobId
     * @param compId
     * @param time
     */
    public void setTaskComplete(int jobId, String compId, int taskId, Date time) 
            throws NonexistentEntityException, Exception;

    /**
     *
     * @param jobId
     * @return
     */
    public ObservableList<JobComponent> getComponents(int jobId);

    /**
     *
     * @param jobId
     */
    public void printLabel(int jobId);

}
