/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;

public class FloatConstant
extends Expression {
    private FloatObject value;

    public FloatConstant(Token old) {
        super(old);
        this.value = new FloatObject(Float.parseFloat(this.code));
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

