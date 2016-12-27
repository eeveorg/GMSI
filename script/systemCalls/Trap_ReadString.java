/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.BufferedReader;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_ReadString
extends Trap {
    public Trap_ReadString(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        BufferedReader r = (BufferedReader)this.getObjParam(0, true);
        StringBuilder result = new StringBuilder();
        try {
            int i;
            while ((i = r.read()) > 0) {
                result.append((char)i);
            }
            if (i == -1 && result.length() == 0) {
                return nullResult;
            }
            return new StringObject(result.toString());
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
    }
}

