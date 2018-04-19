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

    /**
     *
     * @param job to query whether it is complete
     * @return true if job is complete
     */
    public boolean jobComplete(Job job);

    /**
     *
     * @param account acount to find jobs for
     * @param jobType - either complete, incomplete or all jobs
     * @return list of all jobs belonging to the account
     */
    public ObservableList<Job> getJobs(CustomerAccount account, Jobs jobType);

    /**
     *
     * @param taskId of task to lookup
     * @return true if the task is in the database
     */
    public boolean taskExists(int taskId);

    /**
     *
     * @param taskId of task to find from the database
     * @return task from database
     */
    public Task getTask(int taskId);

    /**
     *
     * @param ct component task to remove from the system
     * @throws NonexistentEntityException if it is not in the database
     */
    public void removeComponentTask(ComponentTask ct) throws NonexistentEntityException;

    /**
     *
     * @param ct component task to update
     * @throws NonexistentEntityException if the entity does not exist in the database
     * @throws Exception if db connection fails
     */
    public void updateTask(ComponentTask ct) throws NonexistentEntityException, Exception;

    /**
     *
     * @param componentId of job component
     * @param jobId of job
     * @return job component from the database with the same primary key
     */
    public JobComponent getComponent(String componentId, int jobId);

    /**
     *
     * @param ct component task to add to job component
     * @param t task to add to component task
     * @param jc job component
     * @throws PreexistingEntityException if component task exists
     * @throws Exception if database connection fails
     */
    public void addComponentTask(ComponentTask ct, Task t, JobComponent jc) throws PreexistingEntityException, Exception;

    /**
     *
     * @param j job to add to database
     * @return job that was added
     */
    public Job addJob(Job j);

    /**
     *
     * @param j job to update
     * @throws IllegalOrphanException if changes invalidate foreign keys
     * @throws NonexistentEntityException if job does not exist 
     * @throws Exception if database connection fails
     */
    public void updateJob(Job j) throws IllegalOrphanException, NonexistentEntityException, Exception;

    /**
     *
     * @param ct component task to add to the database
     * @throws PreexistingEntityException if it already exists
     * @throws Exception if database connection fails
     */
    public void addComponentTask(ComponentTask ct) throws PreexistingEntityException, Exception;

    /**
     *
     * @param jc job component to add to the database
     * @throws PreexistingEntityException if it already exists
     * @throws Exception if database connection fails
     */
    public void addJobComponent(JobComponent jc) throws PreexistingEntityException, Exception;
    /**
     * @param taskId id of task that is complete
     * @param jobId id of job
     * @param compId id of the job component
     * @param time time that the task was finished
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException 
     * if the component task does not already exist in the database
     * @throws java.lang.Exception if db connection fails
     */
    public void setTaskComplete(int jobId, String compId, int taskId, Date time) 
            throws NonexistentEntityException, Exception;

    /**
     *
     * @param jobId id of job to return components for
     * @return list of all components for given job
     */
    public ObservableList<JobComponent> getComponents(int jobId);

    /**
     *
     * @param jobId id of job to print label for
     */
    public void printLabel(int jobId);

}
