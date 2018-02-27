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
package bapers.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author chris
 */
@Embeddable
public class TaskDiscountPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "fk_account_number")
    private String fkAccountNumber;
    @Basic(optional = false)
    @Column(name = "fk_task_id")
    private int fkTaskId;

    public TaskDiscountPK() {
    }

    public TaskDiscountPK(String fkAccountNumber, int fkTaskId) {
        this.fkAccountNumber = fkAccountNumber;
        this.fkTaskId = fkTaskId;
    }

    public String getFkAccountNumber() {
        return fkAccountNumber;
    }

    public void setFkAccountNumber(String fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    public int getFkTaskId() {
        return fkTaskId;
    }

    public void setFkTaskId(int fkTaskId) {
        this.fkTaskId = fkTaskId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fkAccountNumber != null ? fkAccountNumber.hashCode() : 0);
        hash += (int) fkTaskId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskDiscountPK)) {
            return false;
        }
        TaskDiscountPK other = (TaskDiscountPK) object;
        if ((this.fkAccountNumber == null && other.fkAccountNumber != null) || (this.fkAccountNumber != null && !this.fkAccountNumber.equals(other.fkAccountNumber))) {
            return false;
        }
        if (this.fkTaskId != other.fkTaskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.TaskDiscountPK[ fkAccountNumber=" + fkAccountNumber + ", fkTaskId=" + fkTaskId + " ]";
    }
    
}
