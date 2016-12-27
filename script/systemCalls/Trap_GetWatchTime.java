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

public class Trap_GetWatchTime
extends Trap {
    public Trap_GetWatchTime(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        long l;
        String watchName = this.getStrParam(0, true);
        try {
            l = Stopwatch.get(watchName);
        }
        catch (RuntimeException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return new IntObject((int)(l / 1000000));
    }
}

