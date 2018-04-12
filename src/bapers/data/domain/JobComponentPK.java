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
public class JobComponentPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "fk_job_id")
    private int fkJobId;
    @Basic(optional = false)
    @Column(name = "component_id")
    private int componentId;

    public JobComponentPK() {
    }

    public JobComponentPK(int fkJobId, int componentId) {
        this.fkJobId = fkJobId;
        this.componentId = componentId;
    }

    public int getFkJobId() {
        return fkJobId;
    }

    public void setFkJobId(int fkJobId) {
        this.fkJobId = fkJobId;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkJobId;
        hash += (int) componentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobComponentPK)) {
            return false;
        }
        JobComponentPK other = (JobComponentPK) object;
        if (this.fkJobId != other.fkJobId) {
            return false;
        }
        if (this.componentId != other.componentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.JobComponentPK[ fkJobId=" + fkJobId + ", componentId=" + componentId + " ]";
    }
    
}
