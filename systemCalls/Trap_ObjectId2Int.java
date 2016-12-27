/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_ObjectId2Int
extends Trap {
    public Trap_ObjectId2Int(Script s) {
        super(s, "objectId2Int");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String s = this.getStrParam(0, true);
        if (s.length() != 4) {
            throw new InternalScriptError("ObjectId2Int error: The handed string's length was not 4 chars!");
        }
        int result = 0;
        int factor = 1;
        int i = 3;
        while (i >= 0) {
            result += factor * s.charAt(i);
            factor *= 256;
            --i;
        }
        return new IntObject(result);
    }
}

