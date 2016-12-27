/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;

public class StringConstant
extends Expression {
    private StringObject value;

    public StringConstant(Token old) {
        super(old);
        this.value = new StringObject(this.code.substring(1, this.code.length() - 1));
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return this.value;
    }
}

