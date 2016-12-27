/*
 * Decompiled with CFR 0_119.
 */
package wcData.triggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.IntVarSpace;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapFormatError;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;
import wcData.triggers.Data_Category;
import wcData.triggers.Data_GlobalVar;
import wcData.triggers.Data_Trigger;

public class TriggerIO {
    private String wtg_header1;
    private int wtg_header2;
    private int wct_fileVersion;
    private int wtg_unknownInt;
    private MapHandle h;

    public TriggerIO(MapHandle h) {
        this.h = h;
    }

    public void readTriggerFiles(File wct_File, File wtg_File) throws IOException, InternalScriptError, MapFormatError {
        int wct_numTriggers;
        BlizzardDataInputStream wct = new BlizzardDataInputStream(new FileInputStream(wct_File));
        BlizzardDataInputStream wtg = new BlizzardDataInputStream(new FileInputStream(wtg_File));
        VarSpace v = new VarSpaceWriter(this.h, wct, this.h.getMapSpace()).writeStruct("script", "ScriptSection").getVarSpace();
        VarSpaceWriter guiWrite = new VarSpaceWriter(null, null, this.h.getMapSpace()).writeStruct("internal", "InternSection").writeArray("guiStrings");
        Data_Trigger.currentGuiStringSpace = guiWrite.getVarSpace();
        Data_Trigger.currentGuiStringIndex = 0;
        this.wtg_header1 = wtg.readCharsAsString(4);
        this.wtg_header2 = wtg.readInt();
        this.wct_fileVersion = wct.readInt();
        String customScriptComment = wct.readString();
        int headersize = wct.readInt();
        String headercode = "";
        if (headersize > 0) {
            headercode = wct.readString();
        }
        v.put("headerComment", new StringObject(customScriptComment));
        v.put("headerCode", new StringObject(headercode));
        IntVarSpace catSpace = ((ArrayObject)v.get("categories")).getIntSpace();
        int numCategories = wtg.readInt();
        int i = 0;
        while (i < numCategories) {
            Data_Category c = new Data_Category();
            c.fromFile(wtg);
            catSpace.put(i, c.toObject());
            ++i;
        }
        this.wtg_unknownInt = wtg.readInt();
        VarSpace varSpace = ((ArrayObject)v.get("variables")).getVarSpace();
        int numVars = wtg.readInt();
        int i2 = 0;
        while (i2 < numVars) {
            Data_GlobalVar c = new Data_GlobalVar();
            c.fromFile(wtg);
            varSpace.put(c.getName(), c.toObject());
            ++i2;
        }
        int wtg_numTriggers = wtg.readInt();
        if (wtg_numTriggers != (wct_numTriggers = wct.readInt())) {
            throw new MapFormatError("The number of triggers in the wtg file does not match the number in the wct file!");
        }
        IntVarSpace trigSpace = ((ArrayObject)v.get("triggers")).getIntSpace();
        int i3 = 0;
        while (i3 < wtg_numTriggers) {
            Data_Trigger c = new Data_Trigger();
            c.fromFiles(wtg, wct);
            trigSpace.put(i3, c.toObject());
            ++i3;
        }
        wct.close();
        wtg.close();
    }

    public void toTriggerFiles(File wct_File, File wtg_File) throws IOException, InternalScriptError, MapFormatError {
        BlizzardDataOutputStream wct = new BlizzardDataOutputStream(wct_File);
        BlizzardDataOutputStream wtg = new BlizzardDataOutputStream(wtg_File);
        VarSpace v = new VarSpaceReader(this.h, wct, this.h.getMapSpace()).writeStruct("script").getVarSpace();
        wtg.writeFourByteString("WTG!");
        wtg.writeInt(7);
        wct.writeInt(1);
        wct.writeString(((StringObject)v.get("headerComment")).getValue());
        String headerCode = ((StringObject)v.get("headerCode")).getValue();
        wct.writeInt(headerCode.length() > 0 ? headerCode.length() + 1 : 0);
        if (headerCode.length() > 0) {
            wct.writeString(headerCode);
        }
        IntVarSpace catSpace = ((ArrayObject)v.get("categories")).getIntSpace();
        wtg.writeInt(catSpace.getKeys().size());
        for (Object o : catSpace.getKeys()) {
            Data_Category c = new Data_Category();
            c.fromObject(catSpace.get(o));
            c.toFile(wtg);
        }
        wtg.writeInt(this.wtg_unknownInt);
        VarSpace varSpace = ((ArrayObject)v.get("variables")).getVarSpace();
        wtg.writeInt(varSpace.getKeys().size());
        for (Object o : varSpace.getKeys()) {
            Data_GlobalVar c = new Data_GlobalVar();
            c.fromObject(varSpace.get(o));
            c.toFile(wtg);
        }
        IntVarSpace trigSpace = ((ArrayObject)v.get("triggers")).getIntSpace();
        int numTriggers = trigSpace.getKeys().size();
        wtg.writeInt(numTriggers);
        wct.writeInt(numTriggers);
        for (Object o : trigSpace.getKeys()) {
            Data_Trigger c = new Data_Trigger();
            c.fromObject(trigSpace.get(o));
            c.toFiles(wtg, wct);
        }
        wct.close();
        wtg.close();
    }
}

