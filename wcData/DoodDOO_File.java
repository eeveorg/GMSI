/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import program.Program;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class DoodDOO_File {
    private MapHandle h;
    private String fileId = "W3do";
    private int fileVersion = 8;
    private int fileSubVersion = 11;
    private int specialDoodFormatVersion = 0;

    public DoodDOO_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        VarSpaceWriter out;
        StructObject s;
        VarSpace curSpace;
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        this.fileId = in.readCharsAsString(4);
        this.fileVersion = in.readInt();
        this.fileSubVersion = in.readInt();
        int numEntries = in.readInt();
        VarSpaceWriter w = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeStruct("placed", "PlacedObjects").writeArray("doods");
        int i = 0;
        while (i < numEntries) {
            s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("PlacedDood")).getNewInstance();
            curSpace = s.getVarSpace();
            out = new VarSpaceWriter(this.h, in, curSpace);
            out.writeNCharsAsString("typeID", 4);
            out.writeInt("variation");
            out.writeFloat("x");
            out.writeFloat("y");
            out.writeFloat("z");
            out.writeFloat("rotation");
            out.writeFloat("scaleX");
            out.writeFloat("scaleY");
            out.writeFloat("scaleZ");
            out.writeByte("flags", false);
            out.writeByte("hitPointsPercent", false);
            out.writeInt("mapItemTablePointer");
            int numDroppedSets = in.readInt();
            VarSpaceWriter cur = null;
            if (numDroppedSets > 0) {
                cur = out.writeArray("droppedItemSets");
            }
            int j = 0;
            while (j < numDroppedSets) {
                int numDroppedItems = in.readInt();
                VarSpaceWriter cur2 = cur.writeArray(j);
                int k = 0;
                while (k < numDroppedItems) {
                    VarSpaceWriter cur3 = cur2.writeStruct(k, "Po_droppedItem");
                    cur3.writeNCharsAsString("itemID", 4);
                    cur3.writeInt("percentage");
                    ++k;
                }
                ++j;
            }
            int number = in.readInt();
            w.getVarSpace().put(number, s);
            ++i;
        }
        this.specialDoodFormatVersion = in.readInt();
        numEntries = in.readInt();
        w = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeStruct("placed", "PlacedObjects").writeArray("specialDoods");
        i = 0;
        while (i < numEntries) {
            s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("PlacedSpecialDoodad")).getNewInstance();
            curSpace = s.getVarSpace();
            out = new VarSpaceWriter(this.h, in, curSpace);
            out.writeNCharsAsString("typeID", 4);
            out.writeInt("z");
            out.writeInt("x");
            out.writeInt("y");
            w.getVarSpace().put(i, s);
            ++i;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        VarSpaceReader out;
        BlizzardDataOutputStream outWrite = new BlizzardDataOutputStream(doo);
        outWrite.writeFourByteString(this.fileId);
        outWrite.writeInt(this.fileVersion);
        outWrite.writeInt(this.fileSubVersion);
        VarSpaceReader w = new VarSpaceReader(this.h, outWrite, this.h.getMapSpace()).writeStruct("placed").writeArray("doods");
        outWrite.writeInt(w.getVarSpaceSize());
        for (Object o : w.getVarSpaceKeys()) {
            out = w.writeStruct(o);
            out.writeNCharsAsString("typeID", 4);
            out.writeInt("variation");
            out.writeFloat("x");
            out.writeFloat("y");
            out.writeFloat("z");
            out.writeFloat("rotation");
            out.writeFloat("scaleX");
            out.writeFloat("scaleY");
            out.writeFloat("scaleZ");
            out.writeByte("flags");
            out.writeByte("hitPointsPercent");
            out.writeInt("mapItemTablePointer");
            VarSpaceReader cur = out.writeArray("droppedItemSets");
            outWrite.writeInt(cur.getVarSpaceSize());
            for (Object j : cur.getVarSpaceKeys()) {
                VarSpaceReader cur2 = cur.writeArray(j);
                outWrite.writeInt(cur2.getVarSpaceSize());
                for (Object k : cur2.getVarSpaceKeys()) {
                    VarSpaceReader cur3 = cur2.writeStruct(k);
                    cur3.writeNCharsAsString("itemID", 4);
                    cur3.writeInt("percentage");
                }
            }
            outWrite.writeInt((Integer)o);
        }
        outWrite.writeInt(this.specialDoodFormatVersion);
        w = new VarSpaceReader(this.h, outWrite, this.h.getMapSpace()).writeStruct("placed").writeArray("specialDoods");
        outWrite.writeInt(w.getVarSpaceSize());
        for (Object o : w.getVarSpaceKeys()) {
            out = w.writeStruct(o);
            out.writeNCharsAsString("typeID", 4);
            out.writeInt("z");
            out.writeInt("x");
            out.writeInt("y");
        }
        outWrite.close();
    }
}

