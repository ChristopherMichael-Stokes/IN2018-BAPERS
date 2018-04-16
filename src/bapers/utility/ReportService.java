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
package bapers.utility;

import static bapers.BAPERS.EMF;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author EdgarLaw
 */
@NamedStoredProcedureQuery(
        name = "IndividualReport",
        procedureName = "ir",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN,type = char.class ,name = "account_no"),
            @StoredProcedureParameter(mode = ParameterMode.IN,type = char.class ,name = "date_start"),
            @StoredProcedureParameter(mode = ParameterMode.IN,type = char.class ,name = "date_end")
        }
)
public class ReportService {
    EntityManager em =null;
    StoredProcedureQuery query;
    
    public ReportService() {
    }
    

    
    public void IndividualReport() throws Exception{
        try{
        em = EMF.createEntityManager();
        query = em.createNamedStoredProcedureQuery("IndividualReport")
                .registerStoredProcedureParameter("account_no", char.class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_start", char.class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_end", char.class, ParameterMode.IN);
        query.setParameter("account_no","1".toCharArray());
        query.setParameter("date_start","2017-09-01".toCharArray());
        query.setParameter("date_end","2018-02-20".toCharArray());
        query.execute();
        List<Object[]> result = query.getResultList();
        result.forEach((o)->{
            System.out.println(Arrays.asList(o).toString());
        });
        }
        catch(Exception ex){
        throw ex;
    }
    }
}
