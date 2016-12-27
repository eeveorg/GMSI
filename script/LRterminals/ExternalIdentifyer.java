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

public class ExternalIdentifyer
extends Name {
    public ExternalIdentifyer(Token old) {
        super(old);
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
            return this.owner.getNameResolver().resolveExternalName(this);
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

