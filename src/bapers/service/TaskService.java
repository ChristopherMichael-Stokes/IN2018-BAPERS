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
import bapers.data.domain.Task;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface TaskService {

    /**
     *
     * @return all tasks from the system
     */
    public ObservableList<Task> getTasks();

    /**
     *
     * @param task to add to the system
     * @throws PreexistingEntityException if task already exists
     * @throws Exception if database connection fails
     */
    public void addTask(Task task) throws PreexistingEntityException, Exception;

    /**
     *
     * @param task to update in the system
     * @throws IllegalOrphanException if foreign keys are invalidated
     * @throws NonexistentEntityException if task does not exist
     * @throws Exception if database connection fails
     */
    public void updateTask(Task task) throws IllegalOrphanException, NonexistentEntityException, Exception;
    
    /**
     *
     * @param taskId of task to remove
     * @throws IllegalOrphanException if foreign keys are invalidated
     * @throws NonexistentEntityException if task does not already exist
     */
    public void removeTask(int taskId) throws IllegalOrphanException, NonexistentEntityException;
}
