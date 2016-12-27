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

public class UnitDOO_File {
    private MapHandle h;
    private String fileId = "W3do";
    private int fileVersion = 8;
    private int fileSubVersion = 11;

    public UnitDOO_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        this.fileId = in.readCharsAsString(4);
        this.fileVersion = in.readInt();
        this.fileSubVersion = in.readInt();
        int numEntries = in.readInt();
        VarSpaceWriter w = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeStruct("placed", "PlacedObjects").writeArray("objects");
        int i = 0;
        while (i < numEntries) {
            VarSpaceWriter cur2;
            StructObject s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("PlacedObject")).getNewInstance();
            VarSpace curSpace = s.getVarSpace();
            VarSpaceWriter out = new VarSpaceWriter(this.h, in, curSpace);
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
            out.writeInt("owner");
            out.writeNBytesInt("unknownBytes", 2);
            out.writeInt("hitPoints");
            out.writeInt("manaPoints");
            out.writeInt("mapItemTablePointer");
            int numDroppedSets = in.readInt();
            VarSpaceWriter cur = null;
            if (numDroppedSets > 0) {
                cur = out.writeArray("droppedItemSets");
            }
            int j = 0;
            while (j < numDroppedSets) {
                int numDroppedItems = in.readInt();
                cur2 = cur.writeArray(j);
                int k = 0;
                while (k < numDroppedItems) {
                    VarSpaceWriter cur3 = cur2.writeStruct(k, "Po_droppedItem");
                    cur3.writeNCharsAsString("itemID", 4);
                    cur3.writeInt("percentage");
                    ++k;
                }
                ++j;
            }
            out.writeInt("goldAmount");
            out.writeFloat("targetAquisition");
            out.writeInt("heroLevel");
            out.writeInt("STR");
            out.writeInt("AGI");
            out.writeInt("INT");
            int numInventoryItems = in.readInt();
            if (numInventoryItems > 0) {
                cur = out.writeArray("itemsInInventory");
            }
            int j2 = 0;
            while (j2 < numInventoryItems) {
                cur2 = cur.writeStruct(j2, "Po_inventoryItem");
                cur2.writeInt("slot");
                cur2.writeNCharsAsString("itemID", 4);
                ++j2;
            }
            int numAbilModis = in.readInt();
            if (numAbilModis > 0) {
                cur = out.writeArray("abilityModifications");
            }
            int j3 = 0;
            while (j3 < numAbilModis) {
                VarSpaceWriter cur22 = cur.writeStruct(j3, "Po_abilModification");
                cur22.writeNCharsAsString("abilID", 4);
                cur22.writeBool("active");
                cur22.writeInt("level");
                ++j3;
            }
            int rndType = out.writeInt("randUnitFlag");
            if (rndType == 0) {
                out.writeNBytesInt("level", 3);
                out.writeByte("itemClass", false);
            } else if (rndType == 1) {
                out.writeInt("rndUnitGrpNum");
                out.writeInt("rndUnitGrpPos");
            } else if (rndType == 2) {
                int numAvailUnits = in.readInt();
                if (numAvailUnits > 0) {
                    cur = out.writeArray("availableUnits");
                }
                int j4 = 0;
                while (j4 < numAvailUnits) {
                    VarSpaceWriter cur23 = cur.writeStruct(j4, "Po_randomUnit");
                    cur23.writeNCharsAsString("unitID", 4);
                    cur23.writeInt("percentage");
                    ++j4;
                }
            } else {
                throw new Error("WTF UnitsDOO!" + rndType + "\n" + curSpace);
            }
            out.writeInt("customColor");
            out.writeInt("waygateDestination");
            int number = in.readInt();
            w.getVarSpace().put(number, s);
            ++i;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        BlizzardDataOutputStream outWrite = new BlizzardDataOutputStream(doo);
        outWrite.writeFourByteString(this.fileId);
        outWrite.writeInt(this.fileVersion);
        outWrite.writeInt(this.fileSubVersion);
        VarSpaceReader w = new VarSpaceReader(this.h, outWrite, this.h.getMapSpace()).writeStruct("placed").writeArray("objects");
        outWrite.writeInt(w.getVarSpaceSize());
        for (Object o : w.getVarSpaceKeys()) {
            Object cur2;
            VarSpaceReader out = w.writeStruct(o);
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
            out.writeInt("owner");
            out.writeNBytesInt("unknownBytes", 2);
            out.writeInt("hitPoints");
            out.writeInt("manaPoints");
            out.writeInt("mapItemTablePointer");
            VarSpaceReader cur = out.writeArray("droppedItemSets");
            outWrite.writeInt(cur.getVarSpaceSize());
            for (Object j : cur.getVarSpaceKeys()) {
                cur2 = cur.writeArray(j);
                outWrite.writeInt(cur2.getVarSpaceSize());
                for (Object k : cur2.getVarSpaceKeys()) {
                    VarSpaceReader cur3 = cur2.writeStruct(k);
                    cur3.writeNCharsAsString("itemID", 4);
                    cur3.writeInt("percentage");
                }
            }
            out.writeInt("goldAmount");
            out.writeFloat("targetAquisition");
            out.writeInt("heroLevel");
            out.writeInt("STR");
            out.writeInt("AGI");
            out.writeInt("INT");
            cur = out.writeArray("itemsInInventory");
            outWrite.writeInt(cur.getVarSpaceSize());
            for (Object j : cur.getVarSpaceKeys()) {
                cur2 = cur.writeStruct(j);
                cur2.writeInt("slot");
                cur2.writeNCharsAsString("itemID", 4);
            }
            cur = out.writeArray("abilityModifications");
            outWrite.writeInt(cur.getVarSpaceSize());
            for (Object j : cur.getVarSpaceKeys()) {
                cur2 = cur.writeStruct(j);
                cur2.writeNCharsAsString("abilID", 4);
                cur2.writeBool("active");
                cur2.writeInt("level");
            }
            int rndType = out.writeInt("randUnitFlag");
            if (rndType == 0) {
                out.writeNBytesInt("level", 3);
                out.writeByte("itemClass");
            } else if (rndType == 1) {
                out.writeInt("rndUnitGrpNum");
                out.writeInt("rndUnitGrpPos");
            } else if (rndType == 2) {
                cur = out.writeArray("availableUnits");
                outWrite.writeInt(cur.getVarSpaceSize());
                for (Object j : cur.getVarSpaceKeys()) {
                    VarSpaceReader cur22 = cur.writeStruct(j);
                    cur22.writeNCharsAsString("unitID", 4);
                    cur22.writeInt("percentage");
                }
            } else {
                throw new InternalScriptError("placed Objects save error: randUnitFlag must be between 0 and 2, but it is " + rndType);
            }
            out.writeInt("customColor");
            out.writeInt("waygateDestination");
            outWrite.writeInt((Integer)o);
        }
        outWrite.close();
    }
}

