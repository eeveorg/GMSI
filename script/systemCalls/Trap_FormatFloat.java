/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_FormatFloat
extends Trap {
    public Trap_FormatFloat(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Float input = Float.valueOf(this.getFloatParam(0));
        Integer numDecimals = this.getIntParam(1);
        Boolean appendZeros = this.getBoolParam(2);
        String format = "#";
        if (numDecimals > 0) {
            format = String.valueOf(format) + ".";
            int i = 0;
            while (i < numDecimals) {
                format = appendZeros != false ? String.valueOf(format) + "0" : String.valueOf(format) + "#";
                ++i;
            }
        }
        DecimalFormat f = new DecimalFormat(format);
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(s);
        return new StringObject(f.format(input.floatValue()));
    }
}

