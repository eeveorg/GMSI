/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import script.EvaluationError;
import script.InternalScriptError;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.MapInfoIO;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class W3E_File {
    private MapHandle h;
    private String fileId = "W3E!";
    private int fileVersion = 11;

    public W3E_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        this.fileId = in.readCharsAsString(4);
        this.fileVersion = in.readInt();
        VarSpaceWriter out = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeStruct("environment", "EnvSection");
        out.writeNCharsAsString("tileset", 1);
        out.writeBool("useCustomTileset");
        int numGroundTilesetsUsed = in.readInt();
        VarSpaceWriter cur = null;
        if (numGroundTilesetsUsed > 0) {
            cur = out.writeArray("groundTilesUsed");
        }
        int i = 0;
        while (i < numGroundTilesetsUsed) {
            cur.writeNCharsAsString(i, 4);
            ++i;
        }
        int numCliffTilesUsed = in.readInt();
        cur = null;
        if (numCliffTilesUsed > 0) {
            cur = out.writeArray("cliffTilesUsed");
        }
        int i2 = 0;
        while (i2 < numCliffTilesUsed) {
            cur.writeNCharsAsString(i2, 4);
            ++i2;
        }
        int width = out.writeInt("mapWidth");
        int height = out.writeInt("mapHeight");
        out.writeFloat("centerOffsetX");
        out.writeFloat("centerOffsetY");
        cur = out.writeArray("tiles");
        int i3 = 0;
        while (i3 < width) {
            VarSpaceWriter cur2 = cur.writeArray(i3);
            int j = 0;
            while (j < height) {
                VarSpaceWriter cur3 = cur2.writeStruct(j, "Tile");
                VarSpace space = cur3.writeTo;
                cur3.writeNBytesInt("height", 2);
                int misc = in.readNByteInt(2);
                space.put("waterLevel", new IntObject(misc & 16383));
                space.put("isBoundary1", BoolObject.getBool((misc & 16384) == 16384));
                byte flagsAndGround = in.readByte();
                space.put("isBoundary2", BoolObject.getBool((flagsAndGround & 128) == 128));
                space.put("waterEnabled", BoolObject.getBool((flagsAndGround & 64) == 64));
                space.put("isBlight", BoolObject.getBool((flagsAndGround & 32) == 32));
                space.put("isRamp", BoolObject.getBool((flagsAndGround & 16) == 16));
                space.put("texture", new IntObject(flagsAndGround & 15));
                cur3.writeByte("textureDetails", false);
                byte cliffAndLayer = in.readByte();
                space.put("cliffTexture", new IntObject((cliffAndLayer & 240) >>> 4));
                space.put("layerHeight", new IntObject(cliffAndLayer & 15));
                ++j;
            }
            ++i3;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        BlizzardDataOutputStream ou = new BlizzardDataOutputStream(doo);
        ou.writeNByteString(this.fileId, 4);
        ou.writeInt(this.fileVersion);
        VarSpaceReader out = new VarSpaceReader(this.h, ou, this.h.getMapSpace()).writeStruct("environment");
        this.h.info_files.tileSet = out.writeNCharsAsString("tileset", 1);
        out.writeBool("useCustomTileset");
        VarSpaceReader cur = out.writeArray("groundTilesUsed");
        ou.writeInt(cur.getVarSpaceSize());
        for (Object i : cur.getVarSpaceKeys()) {
            cur.writeNCharsAsString(i, 4);
        }
        cur = out.writeArray("cliffTilesUsed");
        ou.writeInt(cur.getVarSpaceSize());
        for (Object i : cur.getVarSpaceKeys()) {
            cur.writeNCharsAsString(i, 4);
        }
        int width = out.writeInt("mapWidth");
        int height = out.writeInt("mapHeight");
        out.writeFloat("centerOffsetX");
        out.writeFloat("centerOffsetY");
        cur = out.writeArray("tiles");
        int i = 0;
        while (i < width) {
            VarSpaceReader cur2 = cur.writeArray(i);
            int j = 0;
            while (j < height) {
                VarSpaceReader cur3 = cur2.writeStruct(j);
                VarSpace space = cur3.readFrom;
                cur3.writeNBytesInt("height", 2);
                int misc = ((IntObject)space.get("waterLevel")).getValue() & 16383;
                ou.writeNByteInt(misc |= ((BoolObject)space.get("isBoundary1")).getValue() ? 16384 : 0, 2);
                int flagsAndGround = ((BoolObject)space.get("isBoundary2")).getValue() ? 128 : 0;
                flagsAndGround |= ((BoolObject)space.get("waterEnabled")).getValue() ? 64 : 0;
                flagsAndGround |= ((BoolObject)space.get("isBlight")).getValue() ? 32 : 0;
                flagsAndGround |= ((BoolObject)space.get("isRamp")).getValue() ? 16 : 0;
                ou.writeByte(flagsAndGround |= ((IntObject)space.get("texture")).getValue() & 15);
                cur3.writeByte("textureDetails");
                int cliffAndLayer = (((IntObject)space.get("cliffTexture")).getValue() & 15) << 4;
                ou.writeByte(cliffAndLayer |= ((IntObject)space.get("layerHeight")).getValue() & 15);
                ++j;
            }
            ++i;
        }
        ou.close();
    }
}

