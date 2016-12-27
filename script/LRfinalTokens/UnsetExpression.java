/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.WordUnset;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.names.VarHandle;

public class UnsetExpression
extends Expression {
    public Name toUnset;

    public UnsetExpression(WordUnset b, OpenBracket ob, Name a, CloseBracket cb) throws ParseError {
        super(b, b + ob + a + cb);
        this.toUnset = a;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.toUnset = (Name)this.toUnset.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject result;
        VarHandle h = this.toUnset.resolve();
        try {
            result = h.getValue();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        h.erase();
        return result;
    }
}

