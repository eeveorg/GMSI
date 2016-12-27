/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.systemCalls.Trap;

public class Trap_Fail
extends Trap {
    public Trap_Fail(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String message = this.getStrParam(0, "-no error message-");
        throw new InternalScriptError("FAIL: " + message);
    }
}

