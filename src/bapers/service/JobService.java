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

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
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
    
    /**
     *
     */
    public static enum Jobs {

        /**
         *
         */
        all() {{j = "All jobs";}}, 

        /**
         *
         */
        complete() {{j = "Only complete jobs";}}, 

        /**
         *
         */
        incomplete() {{j = "Only incomplete jobs";}};

        /**
         *
         */
        protected String j;
        
        @Override
        public String toString() {
            return j;
        }
        
        /**
         *
         * @param jobsType
         * @return
         */
        public Jobs getJobsType(String jobsType) {
            switch(jobsType) {
                case "All jobs": return all;
                case "Only complete jobs": return complete;
                case "Only incomplete jobs": return incomplete;
                default: return all;
            }
        }
    }

    /**
     *
     * @param job
     * @return
     */
    public boolean jobComplete(Job job);

    /**
     *
     * @param account
     * @param jobType
     * @return
     */
    public ObservableList<Job> getJobs(CustomerAccount account, Jobs jobType);

    /**
     *
     * @param taskId
     * @return
     */
    public boolean taskExists(int taskId);

    /**
     *
     * @param taskId
     * @return
     */
    public Task getTask(int taskId);

    /**
     *
     * @param ct
     * @throws NonexistentEntityException
     */
    public void removeComponentTask(ComponentTask ct) throws NonexistentEntityException;

    /**
     *
     * @param ct
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void updateTask(ComponentTask ct) throws NonexistentEntityException, Exception;

    /**
     *
     * @param componentId
     * @param jobId
     * @return
     */
    public JobComponent getComponent(String componentId, int jobId);

    /**
     *
     * @param ct
     * @param t
     * @param jc
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void addComponentTask(ComponentTask ct, Task t, JobComponent jc) throws PreexistingEntityException, Exception;

    /**
     *
     * @param j
     * @return
     */
    public Job addJob(Job j);

    /**
     *
     * @param j
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void updateJob(Job j) throws IllegalOrphanException, NonexistentEntityException, Exception;

    /**
     *
     * @param ct
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void addComponentTask(ComponentTask ct) throws PreexistingEntityException, Exception;

    /**
     *
     * @param jc
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void addJobComponent(JobComponent jc) throws PreexistingEntityException, Exception;
    /**
     * @param taskId
     * @param jobId
     * @param compId
     * @param time
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     * @throws java.lang.Exception
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
