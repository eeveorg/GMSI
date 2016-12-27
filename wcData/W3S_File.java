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
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class W3S_File {
    private MapHandle h;
    private int fileVersion = 1;

    public W3S_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        if (doo == null) {
            new VarSpaceWriter(this.h, null, this.h.getMapSpace()).writeArray("sounds");
            return;
        }
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        VarSpaceWriter out = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeArray("sounds");
        this.fileVersion = in.readInt();
        int numSounds = in.readInt();
        int i = 0;
        while (i < numSounds) {
            VarSpaceWriter cur = out.writeStruct(i, "Sound");
            cur.writeString("name");
            cur.writeString("file");
            cur.writeString("eaxEffects");
            cur.writeBitfield(4, new Object[]{"looping", "is3dSound", "stopWhenOutOfRange", "music", "unknownFlag"});
            cur.writeInt("fadeInRate");
            cur.writeInt("fadeOutRate");
            cur.writeInt("volume");
            cur.writeFloat("pitch");
            cur.writeFloat("unknownFloat1");
            cur.writeInt("unknownInt1");
            cur.writeInt("channel");
            cur.writeFloat("minDistance");
            cur.writeFloat("maxDistance");
            cur.writeFloat("distanceCutoff");
            cur.writeFloat("unknownFloat2");
            cur.writeFloat("unknownFloat3");
            cur.writeInt("unknownInt2");
            cur.writeFloat("unknownFloat4");
            cur.writeFloat("unknownFloat5");
            cur.writeFloat("unknownFloat6");
            ++i;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        BlizzardDataOutputStream ou = new BlizzardDataOutputStream(doo);
        ou.writeInt(this.fileVersion);
        VarSpaceReader out = new VarSpaceReader(this.h, ou, this.h.getMapSpace()).writeArray("sounds");
        ou.writeInt(out.getVarSpaceSize());
        for (Object i : out.getVarSpaceKeys()) {
            VarSpaceReader cur = out.writeStruct(i);
            cur.writeString("name");
            cur.writeString("file");
            cur.writeString("eaxEffects");
            cur.writeBitfield(4, new Object[]{"looping", "is3dSound", "stopWhenOutOfRange", "music", "unknownFlag"});
            cur.writeInt("fadeInRate");
            cur.writeInt("fadeOutRate");
            cur.writeInt("volume");
            cur.writeFloat("pitch");
            cur.writeFloat("unknownFloat1");
            cur.writeInt("unknownInt1");
            cur.writeInt("channel");
            cur.writeFloat("minDistance");
            cur.writeFloat("maxDistance");
            cur.writeFloat("distanceCutoff");
            cur.writeFloat("unknownFloat2");
            cur.writeFloat("unknownFloat3");
            cur.writeInt("unknownInt2");
            cur.writeFloat("unknownFloat4");
            cur.writeFloat("unknownFloat5");
            cur.writeFloat("unknownFloat6");
        }
        ou.close();
    }
}

