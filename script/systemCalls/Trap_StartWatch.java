/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.Stopwatch;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_StartWatch
extends Trap {
    public Trap_StartWatch(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String watchName = this.getStrParam(0, true);
        Stopwatch.start(watchName);
        return voidResult;
    }
}

