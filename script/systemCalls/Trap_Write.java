/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.BufferedWriter;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_Write
extends Trap {
    public Trap_Write(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        BufferedWriter w = (BufferedWriter)this.getObjParam(0, true);
        String message = this.getStrParam(1, true);
        try {
            w.write(message);
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

