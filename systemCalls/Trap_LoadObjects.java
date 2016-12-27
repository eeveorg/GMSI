/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

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

public class Trap_LoadObjects
extends Trap {
    public Trap_LoadObjects(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        StructObject map = this.getStructParam(0, true);
        boolean loadUnchangedObjects = this.getBoolParam(1);
        MapHandle m = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
        try {
            m.loadObjects(loadUnchangedObjects);
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

