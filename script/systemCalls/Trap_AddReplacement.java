/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.patterns.MatchHandler;
import script.patterns.MatcherBuilder;
import script.patterns.SimpleMatchHandler;
import script.systemCalls.Trap;

public class Trap_AddReplacement
extends Trap {
    public Trap_AddReplacement(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        MatcherBuilder m = (MatcherBuilder)this.getObjParam(0, true);
        String searchFor = this.getStrParam(1, true);
        String replaceWith = this.getStrParam(2, true);
        m.addMatch(searchFor, new SimpleMatchHandler(replaceWith));
        return voidResult;
    }
}

