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
import script.patterns.ScriptMatchHandler;
import script.systemCalls.Trap;

public class Trap_AddMacro
extends Trap {
    public Trap_AddMacro(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        MatcherBuilder m = (MatcherBuilder)this.getObjParam(0, true);
        String searchFor = this.getStrParam(1, true);
        String replaceWith = this.getStrParam(2, true);
        m.addMatch(searchFor, new ScriptMatchHandler(replaceWith, this.owner));
        return voidResult;
    }
}

