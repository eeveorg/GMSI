/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.File;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_SetScriptPath
extends Trap {
    public Trap_SetScriptPath(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String path = this.getStrParam(0, true);
        File p = new File(path);
        if (p.isFile()) {
            p = p.getAbsoluteFile().getParentFile();
        }
        this.owner.updateCurrentPath(p);
        return voidResult;
    }
}

