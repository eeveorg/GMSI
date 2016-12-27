/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.systemCalls.Trap;
import wcData.objects.WC3DefaultValues;

public class Trap_IsIdPredefined
extends Trap {
    public Trap_IsIdPredefined(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String id = this.getStrParam(0, true);
        return BoolObject.getBool(WC3DefaultValues.getDefaultValues().hasObject(id));
    }
}

