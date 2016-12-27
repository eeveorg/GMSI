/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_StrPos
extends Trap {
    public Trap_StrPos(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String toCut = this.getStrParam(0, true);
        String substring = this.getStrParam(1, true);
        if (toCut == null) {
            return new IntObject(-1);
        }
        if (substring == null) {
            return new IntObject(-1);
        }
        return new IntObject(toCut.indexOf(substring));
    }
}

