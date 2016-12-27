/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import java.io.File;
import java.io.IOException;
import program.Program;
import program.misc.ArchiveHandle;
import program.misc.Log;
import program.misc.MpqHandling;
import program.misc.WC3Files;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StructObject;
import script.names.VarHandle;
import script.systemCalls.Trap;
import wcData.MapHandle;

public class Trap_ExportFile
extends Trap {
    public Trap_ExportFile(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        block19 : {
            if (!WC3Files.isInited()) {
                try {
                    WC3Files.init();
                }
                catch (MpqHandling.MpqError e) {
                    throw new InternalScriptError(e.getMessage());
                }
                catch (IOException e) {
                    throw new InternalScriptError(e.getMessage());
                }
            }
            File exportFolder = new File(Program.getIni().getPropertyString("folders", "exportFolder", "./export"));
            File war3 = WC3Files.getWar3();
            File war3x = WC3Files.getWar3x();
            File war3xlocal = WC3Files.getWar3xlocal();
            File war3patch = WC3Files.getWar3patch();
            StructObject map = this.getStructParam(0, false);
            MapHandle m = null;
            if (map != null) {
                m = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
            }
            String path = this.getStrParam(1, true);
            String newpath = this.getStrParam(2, false);
            if (newpath == null) {
                newpath = path;
            }
            newpath = String.valueOf(exportFolder.getAbsolutePath()) + "/" + newpath;
            File dir = new File(new File(newpath).getParent());
            dir.mkdirs();
            try {
                if (m != null && MpqHandling.ExtractFile(m.getArchiveHandle().getMapFile().getAbsolutePath(), path, newpath)) {
                    if (Log.doLog(1, 3)) {
                        Log.println("Export info: File \"" + path + "\" found in map file");
                    }
                    break block19;
                }
                if (MpqHandling.ExtractFile(war3patch.getCanonicalPath(), path, newpath)) {
                    if (Log.doLog(1, 3)) {
                        Log.println("Export info: File \"" + path + "\" found in war3patch file");
                    }
                    break block19;
                }
                if (MpqHandling.ExtractFile(war3x.getCanonicalPath(), path, newpath)) {
                    if (Log.doLog(1, 3)) {
                        Log.println("Export info: File \"" + path + "\" found in war3x file");
                    }
                    break block19;
                }
                if (MpqHandling.ExtractFile(war3xlocal.getCanonicalPath(), path, newpath)) {
                    if (Log.doLog(1, 3)) {
                        Log.println("Export info: File \"" + path + "\" found in war3xlocal file");
                    }
                    break block19;
                }
                if (MpqHandling.ExtractFile(war3.getCanonicalPath(), path, newpath)) {
                    if (Log.doLog(1, 3)) {
                        Log.println("Export info: File \"" + path + "\" found in war3 file");
                    }
                    break block19;
                }
                return BoolObject.getBool(false);
            }
            catch (MpqHandling.MpqError e) {
                throw new InternalScriptError(e.getMessage());
            }
            catch (IOException e) {
                throw new InternalScriptError(e.getMessage());
            }
        }
        return BoolObject.getBool(true);
    }
}

