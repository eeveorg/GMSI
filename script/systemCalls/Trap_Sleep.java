/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_Sleep
extends Trap {
    int ms;

    public Trap_Sleep(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        this.ms = this.getIntParam(0);
        try {
            Thread.sleep(this.ms);
        }
        catch (InterruptedException e) {
            throw new InternalScriptError("Sleep was interrupted!");
        }
        return voidResult;
    }
}

