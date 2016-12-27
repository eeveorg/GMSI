/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class MapInfoIO {
    private String headerVersion;
    private int w3iVersion;
    private int headerUnknownInt;
    private String mapName;
    private int flags;
    private int maxPlayers;
    String tileSet;
    private String[] prologScreen = new String[4];
    private byte[] restOfW3I;
    private int lengthOfRestOfW3I;
    private MapHandle h;

    public MapInfoIO(MapHandle hh) {
        this.h = hh;
    }

    public void fromFiles(File header, File w3iIn) throws IOException, InternalScriptError {
        BlizzardDataInputStream head = new BlizzardDataInputStream(new FileInputStream(header));
        BlizzardDataInputStream w3i = new BlizzardDataInputStream(new FileInputStream(w3iIn));
        VarSpaceWriter out = new VarSpaceWriter(this.h, w3i, this.h.getMapSpace()).writeStruct("info", "InfoSection");
        VarSpaceWriter misc = out.writeStruct("misc", "MiscInfoSection");
        this.headerVersion = head.readCharsAsString(4);
        this.headerUnknownInt = head.readInt();
        this.mapName = head.readString();
        this.flags = head.readInt();
        this.maxPlayers = head.readInt();
        this.w3iVersion = w3i.readInt();
        out.writeInt("numSaves");
        out.writeInt("editorVersion");
        out.writeString("name");
        out.writeString("author");
        out.writeString("description");
        out.writeString("playersRecommended");
        out.writeArray("cameraBounds", 8, FloatObject.class);
        out.writeArray("cameraBoundComplements", 4, IntObject.class);
        out.writeInt("playableWidth");
        out.writeInt("playableHeight");
        VarSpaceWriter flags = out.writeStruct("flags", "StructMapFlags");
        flags.writeBitfield(4, new Object[]{"hideMinimapInPreviewScreens", "modifyAllyPriorities", "meleeMap", "playableMapSizeWasLargeAndHasNeverBeenReducedToMedium", "maskedAreasArePartiallyVisible", "fixedPlayerSettingsForCustomForces", "useCustomForces", "useCustomTechtree", "useCustomAbilities", "useCustomUpgrades", "mapPropertiesMenuOpenedAtLeastOnceSinceMapCreation", "showWaterWavesOnCliffShores", "showWaterWavesOnRollingShores", "unknown1", "unknown2", "unknown3"});
        this.tileSet = w3i.readCharsAsString(1);
        VarSpaceWriter cur = out.writeStruct("loadScreen", "LoadScreenSection");
        cur.writeInt("presetNum");
        cur.writeString("customPath");
        cur.writeString("text");
        cur.writeString("title");
        cur.writeString("subtitle");
        misc.writeInt("usedGameDataSet");
        int i = 0;
        while (i < 4) {
            this.prologScreen[i] = w3i.readString();
            ++i;
        }
        VarSpaceWriter sFX = out.writeStruct("effects", "EffectsInfoSection");
        sFX.writeInt("useTerrainFog");
        sFX.writeFloat("fogStartZ");
        sFX.writeFloat("fogEndZ");
        sFX.writeFloat("fogDensity");
        cur = sFX.writeStruct("fogColor", "Color");
        cur.writeByte("red", false);
        cur.writeByte("green", false);
        cur.writeByte("blue", false);
        cur.writeByte("alpha", false);
        sFX.writeInt("globalWeatherId");
        sFX.writeString("customSoundEnvironment");
        sFX.writeNCharsAsString("customLightEnvironment", 1);
        cur = sFX.writeStruct("waterTinting", "Color");
        cur.writeByte("red", false);
        cur.writeByte("green", false);
        cur.writeByte("blue", false);
        cur.writeByte("alpha", false);
        int numPlayers = w3i.readInt();
        cur = null;
        if (numPlayers > 0) {
            cur = out.writeArray("players");
        }
        int i2 = 0;
        while (i2 < numPlayers) {
            VarSpaceWriter player = cur.writeStruct(i2, "Info_player");
            player.writeInt("number");
            player.writeInt("typ");
            player.writeInt("race");
            player.writeBool("fixedStartPosition");
            player.writeString("name");
            player.writeFloat("startX");
            player.writeFloat("startY");
            VarSpaceWriter arry = player.writeArray("allyLowPriorityFlags");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
            arry = player.writeArray("allyHighPriorityFlags");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
            ++i2;
        }
        int numForces = w3i.readInt();
        cur = null;
        if (numForces > 0) {
            cur = out.writeArray("forces");
        }
        int i3 = 0;
        while (i3 < numForces) {
            VarSpaceWriter force = cur.writeStruct(i3, "Info_force");
            Object[] arrobject = new Object[6];
            arrobject[0] = "allied";
            arrobject[1] = "alliedVictory";
            arrobject[3] = "shareVision";
            arrobject[4] = "shareUnitControl";
            arrobject[5] = "shareAdvancedUnitControl";
            force.writeBitfield(4, arrobject);
            VarSpaceWriter arry = force.writeArray("playersInForce");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31});
            force.writeString("name");
            ++i3;
        }
        this.restOfW3I = new byte[4096];
        this.lengthOfRestOfW3I = w3i.read(this.restOfW3I);
        head.close();
        w3i.close();
    }

    public void toFiles(File header, File w3iFile) throws IOException, InternalScriptError {
        VarSpaceReader arry;
        RandomAccessFile f = new RandomAccessFile(header, "rw");
        BlizzardDataOutputStream w3i = new BlizzardDataOutputStream(w3iFile);
        VarSpaceReader out = new VarSpaceReader(this.h, w3i, this.h.getMapSpace()).writeStruct("info");
        VarSpaceReader misc = out.writeStruct("misc");
        w3i.writeInt(this.w3iVersion);
        out.writeInt("numSaves");
        out.writeInt("editorVersion");
        out.writeString("name");
        out.writeString("author");
        out.writeString("description");
        out.writeString("playersRecommended");
        out.writeArray("cameraBounds", FloatObject.class);
        out.writeArray("cameraBoundComplements", IntObject.class);
        out.writeInt("playableWidth");
        out.writeInt("playableHeight");
        VarSpaceReader flags = out.writeStruct("flags");
        int flagsBits = flags.writeBitfield(4, new Object[]{"hideMinimapInPreviewScreens", "modifyAllyPriorities", "meleeMap", "playableMapSizeWasLargeAndHasNeverBeenReducedToMedium", "maskedAreasArePartiallyVisible", "fixedPlayerSettingsForCustomForces", "useCustomForces", "useCustomTechtree", "useCustomAbilities", "useCustomUpgrades", "mapPropertiesMenuOpenedAtLeastOnceSinceMapCreation", "showWaterWavesOnCliffShores", "showWaterWavesOnRollingShores", "unknown1", "unknown2", "unknown3"});
        w3i.writeNByteString(this.tileSet, 1);
        VarSpaceReader cur = out.writeStruct("loadScreen");
        cur.writeInt("presetNum");
        cur.writeString("customPath");
        cur.writeString("text");
        cur.writeString("title");
        cur.writeString("subtitle");
        misc.writeInt("usedGameDataSet");
        int i = 0;
        while (i < 4) {
            w3i.writeString(this.prologScreen[i]);
            ++i;
        }
        VarSpaceReader sFX = out.writeStruct("effects");
        sFX.writeInt("useTerrainFog");
        sFX.writeFloat("fogStartZ");
        sFX.writeFloat("fogEndZ");
        sFX.writeFloat("fogDensity");
        cur = sFX.writeStruct("fogColor");
        cur.writeByte("red");
        cur.writeByte("green");
        cur.writeByte("blue");
        cur.writeByte("alpha");
        sFX.writeInt("globalWeatherId");
        sFX.writeString("customSoundEnvironment");
        sFX.writeNCharsAsString("customLightEnvironment", 1);
        cur = sFX.writeStruct("waterTinting");
        cur.writeByte("red");
        cur.writeByte("green");
        cur.writeByte("blue");
        cur.writeByte("alpha");
        cur = out.writeArray("players");
        w3i.writeInt(cur.getVarSpaceSize());
        for (Object i2 : cur.getVarSpaceKeys()) {
            VarSpaceReader player = cur.writeStruct(i2);
            player.writeInt("number");
            player.writeInt("typ");
            player.writeInt("race");
            player.writeBool("fixedStartPosition");
            player.writeString("name");
            player.writeFloat("startX");
            player.writeFloat("startY");
            arry = player.writeArray("allyLowPriorityFlags");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
            arry = player.writeArray("allyHighPriorityFlags");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        }
        cur = out.writeArray("forces");
        w3i.writeInt(cur.getVarSpaceSize());
        for (Object i2 : cur.getVarSpaceKeys()) {
            VarSpaceReader force = cur.writeStruct(i2);
            Object[] arrobject = new Object[6];
            arrobject[0] = "allied";
            arrobject[1] = "alliedVictory";
            arrobject[3] = "shareVision";
            arrobject[4] = "shareUnitControl";
            arrobject[5] = "shareAdvancedUnitControl";
            force.writeBitfield(4, arrobject);
            arry = force.writeArray("playersInForce");
            arry.writeBitfield(4, new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31});
            force.writeString("name");
        }
        w3i.write(this.restOfW3I, 0, this.lengthOfRestOfW3I);
        w3i.close();
        f.writeBytes(this.headerVersion);
        byte[] b = BlizzardDataOutputStream.convertIntToByteArray(this.headerUnknownInt);
        f.write(b);
        f.writeBytes(out.getVarSpace().get("name").toString());
        f.writeByte(0);
        b = BlizzardDataOutputStream.convertIntToByteArray(flagsBits);
        f.write(b);
        b = BlizzardDataOutputStream.convertIntToByteArray(this.maxPlayers);
        f.write(b);
        int toWrite = 512 - (int)f.getFilePointer();
        while (toWrite > 0) {
            --toWrite;
            f.writeByte(0);
        }
        f.close();
    }
}

