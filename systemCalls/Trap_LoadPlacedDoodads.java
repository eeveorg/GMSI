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
import wcData.MapFormatError;
import wcData.MapHandle;

public class Trap_LoadPlacedDoodads
extends Trap {
    public Trap_LoadPlacedDoodads(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        StructObject map = this.getStructParam(0, true);
        MapHandle m = (MapHandle)((ObjObject)map.getEntry("mapHandle").getValue()).getValue();
        try {
            m.loadPlacedDoodads();
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        catch (MapFormatError e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

