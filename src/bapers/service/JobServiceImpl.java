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
import bapers.data.domain.ComponentTask;
import bapers.data.domain.ComponentTaskPK;
import bapers.data.domain.JobComponent;
import java.util.Date;
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
