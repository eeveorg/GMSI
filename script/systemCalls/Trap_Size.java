/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.util.Set;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_Size
extends Trap {
    public Trap_Size(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        ArrayObject ary = this.getArrayParam(0, true);
        return new IntObject(ary.getIntKeys().size() + ary.getStrKeys().size());
    }
}

