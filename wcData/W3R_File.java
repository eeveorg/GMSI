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
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.DataObject;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class W3R_File {
    private MapHandle h;
    private int fileVersion = 5;

    public W3R_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        this.fileVersion = in.readInt();
        VarSpaceWriter out = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeArray("rects");
        int numRects = in.readInt();
        VarSpaceWriter cur = null;
        int i = 0;
        while (i < numRects) {
            StructObject result = new StructObject((StructDefinition)Program.getScript().getNameResolver().getUserDef("Rect"));
            cur = new VarSpaceWriter(this.h, in, result.getVarSpace());
            cur.writeFloat("left");
            cur.writeFloat("bottom");
            cur.writeFloat("right");
            cur.writeFloat("top");
            cur.writeString("name");
            int number = in.readInt();
            out.getVarSpace().put(number, result);
            cur.writeNCharsAsString("weatherEffectID", 4);
            cur.writeString("ambientSound");
            cur = cur.writeStruct("color", "Color");
            cur.writeByte("blue", false);
            cur.writeByte("green", false);
            cur.writeByte("red", false);
            cur.writeByte("alpha", false);
            ++i;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        BlizzardDataOutputStream ou = new BlizzardDataOutputStream(doo);
        ou.writeInt(this.fileVersion);
        VarSpaceReader out = new VarSpaceReader(this.h, ou, this.h.getMapSpace()).writeArray("rects");
        ou.writeInt(out.getVarSpaceSize());
        VarSpaceReader cur = null;
        for (Object i : out.getVarSpaceKeys()) {
            cur = out.writeStruct(i);
            cur.writeFloat("left");
            cur.writeFloat("bottom");
            cur.writeFloat("right");
            cur.writeFloat("top");
            cur.writeString("name");
            ou.writeInt((Integer)i);
            cur.writeNCharsAsString("weatherEffectID", 4);
            cur.writeString("ambientSound");
            cur = cur.writeStruct("color");
            cur.writeByte("blue");
            cur.writeByte("green");
            cur.writeByte("red");
            cur.writeByte("alpha");
        }
        ou.close();
    }
}

