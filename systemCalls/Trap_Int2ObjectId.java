/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_Int2ObjectId
extends Trap {
    public Trap_Int2ObjectId(Script s) {
        super(s, "int2ObjectId");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        int input = this.getIntParam(0, true);
        String result = "";
        int i = 3;
        while (i >= 0) {
            result = String.valueOf((char)(input % 256)) + result;
            input /= 256;
            --i;
        }
        return new StringObject(result);
    }
}

