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
package bapers.data.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author chris
 */
@Entity
@Table(name = "task_discount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaskDiscount.findAll", query = "SELECT t FROM TaskDiscount t")
    , @NamedQuery(name = "TaskDiscount.findByFkAccountNumber", query = "SELECT t FROM TaskDiscount t WHERE t.taskDiscountPK.fkAccountNumber = :fkAccountNumber")
    , @NamedQuery(name = "TaskDiscount.findByFkTaskId", query = "SELECT t FROM TaskDiscount t WHERE t.taskDiscountPK.fkTaskId = :fkTaskId")
    , @NamedQuery(name = "TaskDiscount.findByPercentage", query = "SELECT t FROM TaskDiscount t WHERE t.percentage = :percentage")})
public class TaskDiscount implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaskDiscountPK taskDiscountPK;
    @Basic(optional = false)
    @Column(name = "percentage")
    private float percentage;
    @JoinColumn(name = "fk_task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;
    @JoinColumn(name = "fk_account_number", referencedColumnName = "fk_account_number", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Discount discount;

    public TaskDiscount() {
    }

    public TaskDiscount(TaskDiscountPK taskDiscountPK) {
        this.taskDiscountPK = taskDiscountPK;
    }

    public TaskDiscount(TaskDiscountPK taskDiscountPK, float percentage) {
        this.taskDiscountPK = taskDiscountPK;
        this.percentage = percentage;
    }

    public TaskDiscount(int fkAccountNumber, int fkTaskId) {
        this.taskDiscountPK = new TaskDiscountPK(fkAccountNumber, fkTaskId);
    }

    public TaskDiscountPK getTaskDiscountPK() {
        return taskDiscountPK;
    }

    public void setTaskDiscountPK(TaskDiscountPK taskDiscountPK) {
        this.taskDiscountPK = taskDiscountPK;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskDiscountPK != null ? taskDiscountPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskDiscount)) {
            return false;
        }
        TaskDiscount other = (TaskDiscount) object;
        if ((this.taskDiscountPK == null && other.taskDiscountPK != null) || (this.taskDiscountPK != null && !this.taskDiscountPK.equals(other.taskDiscountPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.TaskDiscount[ taskDiscountPK=" + taskDiscountPK + " ]";
    }
    
}
