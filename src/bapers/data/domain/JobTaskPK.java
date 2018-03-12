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
import javax.persistence.Embeddable;

/**
 *
 * @author chris
 */
@Embeddable
public class JobTaskPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "fk_job_id")
    private String fkJobId;
    @Basic(optional = false)
    @Column(name = "fk_task_id")
    private int fkTaskId;

    public JobTaskPK() {
    }

    public JobTaskPK(String fkJobId, int fkTaskId) {
        this.fkJobId = fkJobId;
        this.fkTaskId = fkTaskId;
    }

    public String getFkJobId() {
        return fkJobId;
    }

    public void setFkJobId(String fkJobId) {
        this.fkJobId = fkJobId;
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
        hash += (fkJobId != null ? fkJobId.hashCode() : 0);
        hash += (int) fkTaskId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobTaskPK)) {
            return false;
        }
        JobTaskPK other = (JobTaskPK) object;
        if ((this.fkJobId == null && other.fkJobId != null) || (this.fkJobId != null && !this.fkJobId.equals(other.fkJobId))) {
            return false;
        }
        if (this.fkTaskId != other.fkTaskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.JobTaskPK[ fkJobId=" + fkJobId + ", fkTaskId=" + fkTaskId + " ]";
    }
    
}
