/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.systemCalls.Trap;

public class Trap_Rand
extends Trap {
    public Trap_Rand(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() {
        return new FloatObject((float)Math.random());
    }
}

