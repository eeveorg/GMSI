/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.VarSpace;
import script.systemCalls.Trap;
import wcData.objects.ObjectTableEntry;

public class Trap_GetBaseId
extends Trap {
    public Trap_GetBaseId(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        ArrayObject o = this.getArrayParam(0, true);
        ObjectTableEntry e = (ObjectTableEntry)o.getVarSpace();
        return new StringObject(e.getOrigId());
    }
}

