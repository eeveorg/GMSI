/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.File;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Archive;
import script.systemCalls.Trap;

public class Trap_AddFileToGSLArchive
extends Trap {
    public Trap_AddFileToGSLArchive(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String arch = this.getStrParam(0, true);
        String add = this.getStrParam(1, true);
        String name = this.getStrParam(2, true);
        try {
            Archive.addFile(this.strToFile(arch), this.strToFile(add), name);
        }
        catch (IOException e) {
            throw new InternalScriptError("Unable to add file to GSL archive:\n" + e.getMessage());
        }
        return voidResult;
    }
}

