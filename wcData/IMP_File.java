/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.sun.media.Log
 */
package wcData;

import com.sun.media.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import script.EvaluationError;
import script.InternalScriptError;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.VarSpaceReader;
import wcData.VarSpaceWriter;

public class IMP_File {
    private MapHandle h;
    private int fileVersion = 1;

    public IMP_File(MapHandle h) {
        this.h = h;
    }

    public void fromFile(File doo) throws IOException, EvaluationError, InternalScriptError {
        if (doo == null) {
            new VarSpaceWriter(this.h, null, this.h.getMapSpace()).writeArray("imports");
            return;
        }
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(doo));
        this.fileVersion = in.readInt();
        VarSpaceWriter out = new VarSpaceWriter(this.h, in, this.h.getMapSpace()).writeArray("imports");
        int numImports = in.readInt();
        int i = 0;
        while (i < numImports) {
            VarSpaceWriter cur = out.writeStruct(i, "Import");
            byte useStandardPath = in.readByte();
            boolean result = true;
            if (useStandardPath == 8 || useStandardPath == 5) {
                result = true;
            } else if (useStandardPath == 10 || useStandardPath == 13) {
                result = false;
            } else {
                Log.warning((Object)("Import format warning: Use standardpath was set to " + useStandardPath + "! Normally, only 8,5,10,13 are used! Assuming standard path"));
            }
            cur.getVarSpace().put("useStandardPath", BoolObject.getBool(result));
            cur.writeString("path");
            ++i;
        }
        in.close();
    }

    public void toFile(File doo) throws InternalScriptError, IOException {
        BlizzardDataOutputStream ou = new BlizzardDataOutputStream(doo);
        ou.writeInt(this.fileVersion);
        VarSpaceReader out = new VarSpaceReader(this.h, ou, this.h.getMapSpace()).writeArray("imports");
        ou.writeInt(out.getVarSpaceSize());
        for (Object i : out.getVarSpaceKeys()) {
            VarSpaceReader cur = out.writeStruct(i);
            int result = ((BoolObject)cur.getVarSpace().get("useStandardPath")).getValue() ? 8 : 13;
            ou.writeByte(result);
            cur.writeString("path");
        }
        ou.close();
    }
}

