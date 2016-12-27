/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.patterns.MatcherBuilder;
import script.patterns.NullTransformer;
import script.patterns.PatternTransformer;
import script.systemCalls.Trap;

public class Trap_GetMatcher
extends Trap {
    public Trap_GetMatcher(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Matcher"));
        ObjObject o = new ObjObject(new MatcherBuilder(new NullTransformer()));
        DataObject i = o.explicitCastTo(e);
        return i;
    }
}

