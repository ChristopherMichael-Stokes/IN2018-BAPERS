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
package bapers.utility.report;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author chris
 */
public class IprResultSet {
    
    /**
     * A list use to store the items IndividualReport in order to generate report from it.
     */
    public final List<IndividualPerformanceReport> individualEffort;

    /**
     * A list use to store the items IprTotalIndividual in order to generate report from it.
     */
    public final List<IprTotalIndividual> totalIndividualEffort;

    /**
     *String to store the value for totalOverallEffort according to the data retrieved.
     */
    public final String totalOverallEffort; 
    
    /**
     *
     * @param individual list of object contain IndividualReport object,retrieve values from the item to completed the table for report.
     * @param totalIndividual list of object contain totalIndividualEffort object,retrieve values from the item to completed the table for report.
     * @param total an object representing the totalOverallEffort generate using above List.
     */
    public IprResultSet(List<Object[]> individual, List<Object[]> totalIndividual,
            Object total) {
        
        individualEffort = individual.stream().map(IndividualPerformanceReport::new)
                .collect(Collectors.toList());
        
        totalIndividualEffort = totalIndividual.stream()
                .map(o -> new IprTotalIndividual(o[0].toString(), o[1].toString()))
                .collect(Collectors.toList());
        
        totalOverallEffort = total.toString();
    }
}
