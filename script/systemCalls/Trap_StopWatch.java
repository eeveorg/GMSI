/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.Stopwatch;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_StopWatch
extends Trap {
    public Trap_StopWatch(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        long l;
        String watchName = this.getStrParam(0, true);
        try {
            l = Stopwatch.stop(watchName);
        }
        catch (RuntimeException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return new IntObject((int)(l / 1000000));
    }
}

