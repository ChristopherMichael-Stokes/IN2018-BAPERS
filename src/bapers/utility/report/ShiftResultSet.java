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
import java.util.stream.Collectors;
/**
 *
 * @author chris
 */
public class ShiftResultSet {
    
    /**
     *List which contains shift object where it stores the value retrieve from the database to generate report.
     */
    public final List<Shift> dayShift1,

    /**
     *List which contains shift object where it stores the value retrieve from the database to generate report.
     */
    dayShift2,

    /**
     *List which contains shift object where it stores the value retrieve from the database to generate report.
     */
    nightShift;

    /**
     *TotalShift object which contain the total value from the data retrieve from the database to generate report.
     */
    public final TotalShift dayShift1Total, 

    /**
     *TotalShift object which contain the total value from the data retrieve from the database to generate report.
     */
    dayShift2Total, 

    /**
     *TotalShift object which contain the total value from the data retrieve from the database to generate report.
     */
    nightShiftTotal, 

    /**
     *TotalShift object which contain the total value from the data retrieve from the database to generate report.
     */
    summaryShiftTotal;

    /**
     *List which contains SummaryShift object where it stores the value retrieve from the database to generate report.
     */
    public final List<SummaryShift> summaryShift;
    
    /**
     *
     * @param dayShift1,List,contains objects which holds data value for report generation.
     * @param dayShift2,List,contains objects which holds data value for report generation.
     * @param nightShift,List,contains objects which holds data value for report generation.
     * @param dayShift1Total,objects,contains value which use to generate report.
     * @param dayShift2Total,objects,contains value which use to generate report.
     * @param nightShiftTotal,objects,contains value which use to generate report.
     * @param summaryShift,List,contains objects which holds data value for report generation.
     * @param summaryShiftTotal,objects,contains value which use to generate report.
     */
    public ShiftResultSet(List<Object[]> dayShift1, List<Object[]> dayShift2, 
            List<Object[]> nightShift, Object[] dayShift1Total, 
            Object[] dayShift2Total, Object[] nightShiftTotal,
            List<Object[]> summaryShift, Object[] summaryShiftTotal) {
        this.dayShift1 = shift(dayShift1);
        this.dayShift2 = shift(dayShift1);
        this.dayShift1Total = total(dayShift1Total);
        this.dayShift2Total = total(dayShift2Total);
        this.nightShift = shift(nightShift);
        this.nightShiftTotal = total(nightShiftTotal);
        this.summaryShiftTotal = total(summaryShiftTotal);
        this.summaryShift = summaryShift.stream().map(SummaryShift::new).collect(Collectors.toList());

    }
    private List<Shift> shift(List<Object[]> list) {
        return list.stream().map(Shift::new).collect(Collectors.toList());
    }
    
    private TotalShift total(Object[] array) {
        return new TotalShift(array);
    }
    
    
    
    
}
