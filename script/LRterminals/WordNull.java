/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;

public class WordNull
extends Expression {
    private static NullObject nul = new NullObject();

    public WordNull(Token old) {
        super(old);
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return nul;
    }
}

