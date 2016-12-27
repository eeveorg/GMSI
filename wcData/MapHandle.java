/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import program.misc.ArchiveHandle;
import program.misc.Log;
import program.misc.Misc;
import script.InternalScriptError;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.StructObject;
import script.names.IntVarSpace;
import script.names.StandardVarSpace;
import script.names.VarHandle;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.DoodDOO_File;
import wcData.IMP_File;
import wcData.MapFormatError;
import wcData.MapInfoIO;
import wcData.UnitDOO_File;
import wcData.VarSpaceWriter;
import wcData.W3E_File;
import wcData.W3R_File;
import wcData.W3S_File;
import wcData.WTS_File;
import wcData.objects.ObjectTableEntry;
import wcData.objects.W3O_File;
import wcData.objects.WC3DefaultValues;
import wcData.triggers.TriggerIO;

public class MapHandle {
    private ArchiveHandle io;
    private VarSpace strings;
    private HashMap<String, W3O_File> w3o_files = null;
    private TriggerIO trigger_files = null;
    MapInfoIO info_files = null;
    private UnitDOO_File placedObjects_file = null;
    private DoodDOO_File placedDoods_file = null;
    private W3E_File env_file = null;
    private W3R_File rects_file = null;
    private W3S_File sounds_file = null;
    private IMP_File imports_file = null;
    private StructObject struct;

    public String getAndRemoveTrigstr(int id) throws InternalScriptError {
        String result;
        try {
            result = this.strings.get(id).toString();
        }
        catch (NullPointerException e) {
            if (Log.doLog(1, 2)) {
                Log.println("Your map refers to a TRIGSTR (id: " + id + ") that is not present in your map file, the string will be replaced with an empty string");
            }
            return "";
        }
        this.strings.erase(id);
        return result;
    }

    public String toString() {
        return "<MapHandle>";
    }

    public MapHandle(ArchiveHandle a, StructObject struct) throws IOException, InternalScriptError {
        this.io = a;
        this.struct = struct;
        File str = this.io.getFile("war3map.wts");
        File w3i = this.io.getFile("war3map.w3i");
        if (str == null) {
            throw new InternalScriptError("war3map.wts missing in your map!");
        }
        if (w3i == null) {
            throw new InternalScriptError("war3map.w3i missing in your map!");
        }
        VarSpaceWriter v = new VarSpaceWriter(this, null, struct.getVarSpace());
        v = v.writeStruct("internal", "InternSection");
        ArrayObject ary = WTS_File.read(str);
        this.strings = ary.getIntSpace();
        v.getVarSpace().put("trigstrs", ary);
        this.info_files = new MapInfoIO(this);
        this.info_files.fromFiles(a.getMapFile(), w3i);
    }

    public void saveMap(File output) throws IOException, InternalScriptError {
        File wts = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.wts");
        WTS_File.write(wts, this.strings);
        this.io.copyMap(output);
        this.info_files.toFiles(output, new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/war3map.w3i"));
        this.io.updateFromTempOutput(output);
    }

    public void loadObjects(boolean loadUnchangedObjects) throws IOException, InternalScriptError {
        StandardVarSpace v = loadUnchangedObjects ? WC3DefaultValues.getDefaultValues().getAllObjects() : new StandardVarSpace();
        ArrayObject objects = new ArrayObject(v);
        this.struct.getEntry("objects").setValue(objects);
        W3O_File u = new W3O_File(this, "war3map.w3u", "unit", false, "");
        W3O_File a = new W3O_File(this, "war3map.w3a", "abil", true, "");
        W3O_File b = new W3O_File(this, "war3map.w3b", "destr", false, "");
        W3O_File d = new W3O_File(this, "war3map.w3d", "dood", true, "");
        W3O_File t = new W3O_File(this, "war3map.w3t", "item", false, "");
        W3O_File h = new W3O_File(this, "war3map.w3h", "buff", false, "");
        W3O_File q = new W3O_File(this, "war3map.w3q", "upgrade", true, "");
        this.w3o_files = new HashMap();
        this.w3o_files.put("unit", u);
        this.w3o_files.put("abil", a);
        this.w3o_files.put("destr", b);
        this.w3o_files.put("dood", d);
        this.w3o_files.put("item", t);
        this.w3o_files.put("buff", h);
        this.w3o_files.put("upgrade", q);
        File f = this.io.getFile("war3map.w3a");
        if (f != null) {
            a.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3b")) != null) {
            b.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3d")) != null) {
            d.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3t")) != null) {
            t.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3h")) != null) {
            h.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3q")) != null) {
            q.fromFile(f);
        }
        if ((f = this.io.getFile("war3map.w3u")) != null) {
            u.fromFile(f);
        }
    }

    public void saveObjects() throws InternalScriptError, IOException {
        if (this.w3o_files == null) {
            throw new InternalScriptError("You cannot save the objects of a map, if you haven't loaded them before!");
        }
        HashMap tables = new HashMap();
        for (String s : this.w3o_files.keySet()) {
            ArrayList l = new ArrayList();
            l.add(new LinkedList());
            l.add(new LinkedList());
            tables.put(s, l);
        }
        VarSpace v = this.getObjectSpace();
        for (Object o : v.getKeys()) {
            ObjectTableEntry curObj;
            if (!o.equals((curObj = (ObjectTableEntry)((ArrayObject)v.get((String)o)).getVarSpace()).getId())) {
                if (WC3DefaultValues.getDefaultValues().hasObject(o.toString())) {
                    curObj.setNewId(null);
                    curObj.setOrigId(o.toString());
                } else {
                    curObj.setNewId(o.toString());
                }
            }
            if (curObj.isOriginal()) {
                if (curObj.getKeys().size() == 0) continue;
                ((LinkedList)((ArrayList)tables.get(curObj.getCategory())).get(0)).add(curObj);
                continue;
            }
            ((LinkedList)((ArrayList)tables.get(curObj.getCategory())).get(1)).add(curObj);
        }
        for (Object s : tables.keySet()) {
            this.w3o_files.get(s).toFile(this.io.getTempOutPath(), (ArrayList)tables.get(s));
        }
    }

    public void loadTriggers() throws IOException, InternalScriptError, MapFormatError {
        File wtg = this.io.getFile("war3map.wtg");
        File wct = this.io.getFile("war3map.wct");
        if (wtg == null) {
            throw new MapFormatError("war3map.wtg does not exist in the map");
        }
        if (wct == null) {
            throw new MapFormatError("war3map.wct does not exist in the map");
        }
        this.trigger_files = new TriggerIO(this);
        this.trigger_files.readTriggerFiles(wct, wtg);
    }

    public void saveTriggers() throws InternalScriptError, IOException, MapFormatError {
        if (this.trigger_files == null) {
            throw new InternalScriptError("You cannot save the triggers of a map, if you haven't loaded them before!");
        }
        File wtg = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.wtg");
        File wct = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.wct");
        this.trigger_files.toTriggerFiles(wct, wtg);
    }

    public void loadPlacedObjects() throws IOException, InternalScriptError, MapFormatError {
        File doo = this.io.getFile("war3mapUnits.doo");
        if (doo == null) {
            throw new MapFormatError("war3mapUnits.doo does not exist in the map");
        }
        this.placedObjects_file = new UnitDOO_File(this);
        this.placedObjects_file.fromFile(doo);
    }

    public void savePlacedObjects() throws InternalScriptError, IOException, MapFormatError {
        if (this.placedObjects_file == null) {
            throw new InternalScriptError("You cannot save the placed objects of a map, if you haven't loaded them before!");
        }
        File doo = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3mapUnits.doo");
        this.placedObjects_file.toFile(doo);
    }

    public void loadPlacedDoodads() throws IOException, InternalScriptError, MapFormatError {
        File doo2 = this.io.getFile("war3map.doo");
        if (doo2 == null) {
            throw new MapFormatError("war3mapUnits.doo does not exist in the map");
        }
        this.placedDoods_file = new DoodDOO_File(this);
        this.placedDoods_file.fromFile(doo2);
    }

    public void savePlacedDoodads() throws InternalScriptError, IOException, MapFormatError {
        if (this.placedDoods_file == null) {
            throw new InternalScriptError("You cannot save the placed trees of a map, if you haven't loaded them before!");
        }
        File doo2 = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.doo");
        this.placedDoods_file.toFile(doo2);
    }

    public void loadEnvironment() throws IOException, InternalScriptError, MapFormatError {
        File w3e = this.io.getFile("war3map.w3e");
        if (w3e == null) {
            throw new MapFormatError("war3map.w3e does not exist in the map");
        }
        this.env_file = new W3E_File(this);
        this.env_file.fromFile(w3e);
    }

    public void saveEnvironment() throws InternalScriptError, IOException, MapFormatError {
        if (this.env_file == null) {
            throw new InternalScriptError("You cannot save the environment of a map, if you haven't loaded it before!");
        }
        File w3e = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.w3e");
        this.env_file.toFile(w3e);
    }

    public void loadRects() throws IOException, InternalScriptError, MapFormatError {
        File w3r = this.io.getFile("war3map.w3r");
        if (w3r == null) {
            throw new MapFormatError("war3map.w3r does not exist in the map");
        }
        this.rects_file = new W3R_File(this);
        this.rects_file.fromFile(w3r);
    }

    public void saveRects() throws InternalScriptError, IOException, MapFormatError {
        if (this.rects_file == null) {
            throw new InternalScriptError("You cannot save the environment of a map, if you haven't loaded it before!");
        }
        File w3r = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.w3r");
        this.rects_file.toFile(w3r);
    }

    public void loadSounds() throws IOException, InternalScriptError, MapFormatError {
        File w3s = this.io.getFile("war3map.w3s");
        this.sounds_file = new W3S_File(this);
        this.sounds_file.fromFile(w3s);
    }

    public void saveSounds() throws InternalScriptError, IOException, MapFormatError {
        if (this.sounds_file == null) {
            throw new InternalScriptError("You cannot save the sounds of a map, if you haven't loaded it before!");
        }
        File w3s = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.w3s");
        this.sounds_file.toFile(w3s);
    }

    public void loadImports() throws IOException, InternalScriptError, MapFormatError {
        File imp = this.io.getFile("war3map.imp");
        this.imports_file = new IMP_File(this);
        this.imports_file.fromFile(imp);
    }

    public void saveImports() throws InternalScriptError, IOException, MapFormatError {
        if (this.imports_file == null) {
            throw new InternalScriptError("You cannot save the imports of a map, if you haven't loaded it before!");
        }
        File imp = new File(String.valueOf(this.io.getTempOutPath().getAbsolutePath()) + "/" + "war3map.imp");
        this.imports_file.toFile(imp);
    }

    public VarSpace getObjectSpace() {
        try {
            return ((ArrayObject)this.struct.getEntry("objects").getValue()).getVarSpace();
        }
        catch (InternalScriptError e) {
            throw new Error(e);
        }
    }

    public VarSpace getMapSpace() {
        return this.struct.getVarSpace();
    }

    private String incString(String toInc) {
        int stelle;
        int val;
        block4 : {
            block5 : {
                stelle = toInc.length() - 1;
                do {
                    if ((val = toInc.charAt(stelle)) >= 48 && val < 57) {
                        ++val;
                        break block4;
                    }
                    if (val == 57) {
                        val = 65;
                        break block4;
                    }
                    if (val >= 65 && val < 90) {
                        ++val;
                        break block4;
                    }
                    if (val != 90) break block5;
                    val = 48;
                    toInc = String.valueOf(toInc.substring(0, stelle)) + (char)val + toInc.substring(stelle + 1, toInc.length());
                } while (--stelle != -1);
                return null;
            }
            return null;
        }
        return String.valueOf(toInc.substring(0, stelle)) + (char)val + toInc.substring(stelle + 1, toInc.length());
    }

    public String getFirstFreeKey(String origId) {
        if (origId == null || origId.equals("")) {
            return null;
        }
        if (!Misc.isIdValid(origId)) {
            return null;
        }
        String currentId = Misc.isIdCouraged(origId) ? origId.substring(1) : "000";
        origId = String.valueOf(origId.charAt(0));
        VarSpace v = this.getObjectSpace();
        while (v.hasName(String.valueOf(origId) + currentId)) {
            currentId = this.incString(currentId);
        }
        return String.valueOf(origId) + currentId;
    }

    public ArchiveHandle getArchiveHandle() {
        return this.io;
    }
}