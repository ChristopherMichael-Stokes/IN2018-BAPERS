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
package bapers.utility;

import java.text.DecimalFormat;
import java.text.ParseException;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author chris
 */
public class CurrencyFormat extends TextFormatter<Double> {
    private static final DecimalFormat strictZeroDecimalFormat  
                = new DecimalFormat("\u00A3###,###.##");

    public CurrencyFormat() {
        super(
            new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return strictZeroDecimalFormat.format(value);
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return strictZeroDecimalFormat.parse(string).doubleValue();
                    } catch (ParseException e) {
                        return Double.NaN;
                    }
                }
            }, 0d,
            // change filter rejects text input if it cannot be parsed.
            change -> {
                try {
                    strictZeroDecimalFormat.parse(change.getControlNewText());
                    return change;
                } catch (ParseException e) {
                    return null;
                }
            }
        );      
    }
    
}

