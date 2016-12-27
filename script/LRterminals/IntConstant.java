/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;

public class IntConstant
extends Expression {
    private DataObject value;

    public IntConstant(Token old) {
        super(old);
        this.value = new IntObject(Integer.parseInt(this.code));
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

