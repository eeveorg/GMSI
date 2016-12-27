/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.systemCalls.Trap;

public class Trap_Sin
extends Trap {
    public Trap_Sin(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Float input = Float.valueOf(this.getFloatParam(0));
        return new FloatObject((float)Math.sin(input.floatValue()));
    }
}

