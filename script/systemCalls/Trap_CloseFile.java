/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.Closeable;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_CloseFile
extends Trap {
    public Trap_CloseFile(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Closeable r = (Closeable)this.getObjParam(0, true);
        try {
            r.close();
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

