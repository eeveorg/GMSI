/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;

public class BoolConstant
extends Expression {
    private BoolObject value;

    public BoolConstant(Token old) {
        super(old);
        this.value = BoolObject.getBool(Boolean.parseBoolean(this.code));
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

