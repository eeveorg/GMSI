/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Name;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.names.NameResolver;
import script.names.VarHandle;

public class Identifyer
extends Name {
    private NameResolver nr;
    public int localIndex = -1;

    public Identifyer(Token old) {
        super(old);
        this.nr = this.owner.getNameResolver();
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject o;
        super.eval();
        try {
            o = this.resolve().getValue();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return o;
    }

    @Override
    public VarHandle resolve() throws EvaluationError {
        try {
            if (this.localIndex == -1) {
                return this.nr.resolveName(this.code);
            }
            return this.nr.resolveLocal(this.localIndex);
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

