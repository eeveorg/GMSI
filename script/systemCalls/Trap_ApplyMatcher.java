/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.patterns.MatcherBuilder;
import script.systemCalls.Trap;

public class Trap_ApplyMatcher
extends Trap {
    public Trap_ApplyMatcher(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        MatcherBuilder m = (MatcherBuilder)this.getObjParam(0, true);
        String applyTo = this.getStrParam(1, false);
        if (applyTo == null) {
            return nullResult;
        }
        return new StringObject(m.applyMatching(applyTo));
    }
}

