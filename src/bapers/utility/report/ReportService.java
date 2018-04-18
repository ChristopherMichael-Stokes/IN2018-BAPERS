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
package bapers.utility.report;

import static bapers.BAPERS.EMF;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author EdgarLaw
 */
public class ReportService {

    private static final EntityManager EM = EMF.createEntityManager();

    public static List<IndividualReport> getIndividualReport(String account_no, String date_start, String date_end) {
        StoredProcedureQuery query = EM.createStoredProcedureQuery("ir")
                .registerStoredProcedureParameter("account_no", char[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_start", char[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_end", char[].class, ParameterMode.IN);
        query.setParameter("account_no", account_no.toCharArray());
        query.setParameter("date_start", date_start.toCharArray());
        query.setParameter("date_end", date_end.toCharArray());
        query.execute();

        List<Object[]> rs = query.getResultList();
        return rs.stream().map(IndividualReport::new)
                .collect(Collectors.toList());
    }

    public static IprResultSet getIndividualPerformanceReport(String name, String date_start, String date_end) {
        StoredProcedureQuery query = EM.createStoredProcedureQuery("ipr")
                .registerStoredProcedureParameter("ID", char[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_start", char[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("date_end", char[].class, ParameterMode.IN);
        query.setParameter("ID", name.toCharArray());
        query.setParameter("date_start", date_start.toCharArray());
        query.setParameter("date_end", date_end.toCharArray());
        boolean isResultSet = query.execute(); // returns true when we have a result set from the proc
        List<Object[]> results1 = query.getResultList(); // get the first result set
        List<Object[]> results2 = query.getResultList(); // get the second result set
        Object results3 = query.getSingleResult();      
        
        return new IprResultSet(results1, results2, results3);
    }
}
