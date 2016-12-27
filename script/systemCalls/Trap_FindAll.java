/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.patterns.PatternHandler;
import script.systemCalls.Trap;

public class Trap_FindAll
extends Trap {
    public Trap_FindAll(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        return this.owner.getPatternHandler().findAll(this.getStrParam(0, true), this.getStrParam(1, true));
    }
}

