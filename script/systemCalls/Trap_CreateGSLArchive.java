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

public class Trap_CreateGSLArchive
extends Trap {
    public Trap_CreateGSLArchive(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String path = this.getStrParam(0, true);
        boolean overwrite = this.getBoolParam(1, true);
        File f = this.strToFile(path);
        if (f.exists()) {
            if (overwrite) {
                if (f.isDirectory()) {
                    throw new InternalScriptError("Unable to create GSL archive. A file with that name already exists and is a directory (directories cannot be overwritten)");
                }
            } else {
                throw new InternalScriptError("Unable to create GSL archive. A file with that name already exists and overwrite was not set!");
            }
        }
        if (f.getParentFile() != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            Archive.create(f);
        }
        catch (IOException e) {
            throw new InternalScriptError("Unable to create GSL archive:\n" + e.getMessage());
        }
        return voidResult;
    }
}

