/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import java.io.File;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StructObject;
import script.dataObjects.VoidObject;
import script.names.VarHandle;
import script.systemCalls.Trap;
import wcData.MapHandle;

public class Trap_SaveMap
extends Trap {
    public Trap_SaveMap(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String archiveName = this.getStrParam(1, true);
        StructObject map = this.getStructParam(0, true);
        MapHandle m = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
        try {
            m.saveMap(new File(archiveName));
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

