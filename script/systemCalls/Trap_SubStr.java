/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_SubStr
extends Trap {
    public Trap_SubStr(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String toCut = this.getStrParam(0, false);
        int start = this.getIntParam(1);
        int end = this.getIntParam(2);
        if (toCut == null) {
            return nullResult;
        }
        if (end == -1) {
            end = toCut.length();
        }
        if (start > end) {
            throw new InternalScriptError("Trap error: Invalid call of substr! The startindex " + start + " is greater than the end index" + end);
        }
        if (end > toCut.length()) {
            end = toCut.length();
        }
        if (start > toCut.length()) {
            start = toCut.length();
        }
        return new StringObject(toCut.substring(start, end));
    }
}

