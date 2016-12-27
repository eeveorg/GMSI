/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_StrLen
extends Trap {
    public Trap_StrLen(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String toCut = this.getStrParam(0, "");
        return new IntObject(toCut.length());
    }
}

