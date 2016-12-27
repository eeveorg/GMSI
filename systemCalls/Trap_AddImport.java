/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import java.io.File;
import java.io.IOException;
import program.misc.ArchiveHandle;
import program.misc.Tools;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StructObject;
import script.dataObjects.VoidObject;
import script.names.VarHandle;
import script.systemCalls.Trap;
import wcData.MapHandle;

public class Trap_AddImport
extends Trap {
    public Trap_AddImport(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        StructObject map = this.getStructParam(0, true);
        String file = this.getStrParam(1, true);
        String nameInArchive = this.getStrParam(2, true);
        MapHandle m = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
        File f = new File(file);
        if (!f.exists()) {
            throw new InternalScriptError("Trap error: Cannot add the file " + file + " as import because it doesn't exist!");
        }
        if (f.isDirectory()) {
            throw new InternalScriptError("Trap error: Cannot add the file " + file + " as import because it is a directory, not a file!");
        }
        File outpath = new File(m.getArchiveHandle().getTempOutPath() + "/" + nameInArchive);
        outpath.mkdirs();
        try {
            Tools.copyFile(f, outpath, true);
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

