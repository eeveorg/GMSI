/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.systemCalls.Trap;

public class Trap_Pow
extends Trap {
    public Trap_Pow(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Float base = Float.valueOf(this.getFloatParam(0));
        Float exponent = Float.valueOf(this.getFloatParam(1));
        return new FloatObject((float)Math.pow(base.floatValue(), exponent.floatValue()));
    }
}

