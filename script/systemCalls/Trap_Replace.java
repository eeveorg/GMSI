/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.patterns.PatternHandler;
import script.systemCalls.Trap;

public class Trap_Replace
extends Trap {
    public Trap_Replace(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        return this.owner.getPatternHandler().replace(this.getStrParam(0, true), this.getStrParam(1, true), this.getStrParam(2, true));
    }
}

