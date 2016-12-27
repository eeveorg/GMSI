/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Statement;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class Semicolon
extends Statement {
    public Semicolon(Token old) {
        super(old);
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return voidResult;
    }
}

