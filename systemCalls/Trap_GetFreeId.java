/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.VarHandle;
import script.systemCalls.Trap;
import wcData.MapHandle;

public class Trap_GetFreeId
extends Trap {
    public Trap_GetFreeId(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        StructObject map = this.getStructParam(0, true);
        String baseId = this.getStrParam(1, false);
        MapHandle h = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
        String result = h.getFirstFreeKey(baseId);
        if (result == null) {
            throw new InternalScriptError("Unable to call getFreeId from the id '" + baseId + "'. This id is invalid!");
        }
        return new StringObject(result);
    }
}

