/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import program.misc.Misc;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.systemCalls.Trap;

public class Trap_IsIdValid
extends Trap {
    public Trap_IsIdValid(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String id = this.getStrParam(0, true);
        return BoolObject.getBool(Misc.isIdValid(id));
    }
}

