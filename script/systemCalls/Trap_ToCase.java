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

public class Trap_ToCase
extends Trap {
    public Trap_ToCase(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String in = this.getStrParam(0, false);
        boolean uppercase = this.getBoolParam(1);
        if (in == null) {
            return nullResult;
        }
        in = uppercase ? in.toUpperCase() : in.toLowerCase();
        return new StringObject(in);
    }
}

