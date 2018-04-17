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
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.ComponentTaskJpaController;
import bapers.data.dataAccess.JobComponentJpaController;
import bapers.data.dataAccess.JobJpaController;
import bapers.data.dataAccess.TaskJpaController;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.ComponentTaskPK;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.JobComponentPK;
import bapers.data.domain.Task;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author EdgarLaw
 */
public class JobServiceImpl implements JobService {

    private final JobJpaController jobController;
    private final JobComponentJpaController jobComponentController;
    private final ComponentTaskJpaController componentController;
    private final TaskJpaController taskController;
    
    
    /**
     *
     */
    public JobServiceImpl() {
        jobController = new JobJpaController(EMF);
        jobComponentController = new JobComponentJpaController(EMF);
        taskController = new TaskJpaController(EMF);
        componentController = new ComponentTaskJpaController(EMF);
    }
    
    @Override
    public boolean jobComplete(Job job) {
        return !job.getJobComponentList().stream().flatMap(jc -> jc.getComponentTaskList().stream())
            .anyMatch(ct -> ct.getEndTime() == null);        
    }
    
    @Override
    public ObservableList<Job> getJobs(CustomerAccount account, Jobs jobType) {
        List<Job> jobs = account.getContactList().stream().flatMap(x -> x.getJobList().stream())
                .collect(Collectors.toList());
                
        Predicate<ComponentTask> tempPred;
        switch(jobType) {
            default:
            case all:
                return FXCollections.observableArrayList(jobs);
            case complete:
                tempPred = ct -> !(ct.getEndTime() == null);
                break;
            case incomplete:
                tempPred = ct -> ct.getEndTime() == null;  
                break;
        }
        Predicate<ComponentTask> p = tempPred;
        return FXCollections.observableArrayList(jobs.stream().filter(j -> j.getJobComponentList().stream()
                            .flatMap(jc -> jc.getComponentTaskList().stream())
                            .anyMatch(p)
                        ).collect(Collectors.toList()));
    }
    
    @Override
    public void updateTask(ComponentTask ct) throws NonexistentEntityException, Exception {
        componentController.edit(ct);
    }
    @Override
    public boolean taskExists(int taskId) {
        return taskController.findTask(taskId) != null;
    }
    @Override
    public Task getTask(int taskId) {
        return taskController.findTask(taskId);
    }
    @Override
    public void removeComponentTask(ComponentTask ct) throws NonexistentEntityException {
        componentController.destroy(ct.getComponentTaskPK());
    }
    
    /**
     * @param taskId
     * @param jobId
     * @param compId
     * @param time
     */
    @Override
    public void setTaskComplete(int jobId, String compId, int taskId, Date time) 
            throws NonexistentEntityException, Exception {
        ComponentTask task = componentController.findComponentTask(new ComponentTaskPK(jobId, compId, taskId));
        task.setEndTime(time);
        componentController.edit(task);
    }
    @Override
    public JobComponent getComponent(String componentId, int jobId) {
        return jobComponentController.findJobComponent(new JobComponentPK(jobId, componentId));
    }
    
    @Override
    public void addComponentTask(ComponentTask ct, Task t, JobComponent jc) 
            throws PreexistingEntityException, Exception {
        t.getComponentTaskList().add(ct);
        jc.getComponentTaskList().add(ct);
        ct.setTask(t);
        ct.setJobComponent(jc);
        
        componentController.create(ct);
    }

    /**
     *
     * @param jobId
     * @return
     */
    @Override
    public ObservableList<JobComponent> getComponents(int jobId) {
        return jobComponentController.findJobComponentEntities().stream()
                .filter(j -> j.getJob().getJobId() == jobId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
   }

    /**
     *
     * @param jobId
     */
    @Override
    public void printLabel(int jobId) {
//        https://docs.oracle.com/javase/8/javafx/api/javafx/print/PrinterJob.html
//        Node node = new Circle(100, 200, 200);
//        PrinterJob job = PrinterJob.createPrinterJob();
//        if (job != null) {
//            boolean success = job.printPage(node);
//            if (success) {
//                job.endJob();
//            }
//        }
    }


}
