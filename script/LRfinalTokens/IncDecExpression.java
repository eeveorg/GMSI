/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.IncDecOp;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.names.VarHandle;

public class IncDecExpression
extends Expression {
    public Name operand;
    public IncDecOp operator;
    private int increment;

    public IncDecExpression(Name name, IncDecOp operator) {
        super(name, name + operator);
        this.operand = name;
        this.operator = operator;
        this.increment = operator.getCode().equals("++") ? 1 : -1;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand = (Name)this.operand.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject e1 = this.operand.eval();
        if (e1.isVoid()) {
            throw new EvaluationError(this, "Trying to apply the operator " + this.operator + " on a void argument in code " + this.getCode());
        }
        try {
            DataObject inc = e1.increment(this.increment);
            this.operand.resolve().setValue(inc);
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return e1;
    }
}

