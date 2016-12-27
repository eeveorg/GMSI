/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.File;
import java.io.IOException;
import program.misc.ArchiveHandle;
import program.misc.Tools;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarHandle;
import script.names.VarSpace;
import wcData.MapHandle;

public class MapLoader {
    private File tempPath;
    private Script structLookup;
    private int curId = 0;

    public MapLoader(File tempPath, Script structLookup) {
        this.tempPath = tempPath;
        this.structLookup = structLookup;
    }

    public StructObject openMap(File f) throws IOException, EvaluationError, InternalScriptError {
        File inFolder = new File(String.valueOf(this.tempPath.getAbsolutePath()) + "/in/" + this.curId);
        File outFolder = new File(String.valueOf(this.tempPath.getAbsolutePath()) + "/out/" + this.curId);
        this.createOrTruncate(inFolder);
        this.createOrTruncate(outFolder);
        File tmp = new File(String.valueOf(inFolder.getAbsolutePath()) + "/" + f.getName());
        Tools.copyFile(f, tmp, false);
        ArchiveHandle a = new ArchiveHandle(tmp, inFolder, outFolder);
        StructObject result = new StructObject((StructDefinition)this.structLookup.getNameResolver().getUserDef("Map"));
        result.getEntry("fileName").setValue(new StringObject(f.getName()));
        MapHandle m = new MapHandle(a, result);
        result.getVarSpace().put("mapHandle", new ObjObject(m));
        ++this.curId;
        return result;
    }

    public void reset() {
        this.curId = 0;
    }

    private void createOrTruncate(File folder) {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new Error("Couldn't create temp folder!");
            }
        } else {
            if (!folder.isDirectory()) {
                throw new Error(String.valueOf(folder.getAbsolutePath()) + " is not a directory! Delete this file, that the directory can be created!");
            }
            String[] arrstring = folder.list();
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String toDelete = arrstring[n2];
                Tools.deleteFileRecursive(new File(String.valueOf(folder.getAbsolutePath()) + "/" + toDelete));
                ++n2;
            }
        }
    }
}

