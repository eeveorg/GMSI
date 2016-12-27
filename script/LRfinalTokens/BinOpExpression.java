/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRterminals.BinOp;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class BinOpExpression
extends Expression {
    private Expression operand1;
    private Expression operand2;
    private BinOp operator;

    public BinOpExpression(Expression Op1, BinOp Operator, Expression Op2) {
        super(Op1, String.valueOf(Op1.toString()) + Operator.toString() + Op2.toString());
        this.operand1 = Op1;
        this.operand2 = Op2;
        this.operator = Operator;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand1 = (Expression)this.operand1.unwrap();
        this.operand2 = (Expression)this.operand2.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject e1 = this.operand1.eval();
        DataObject e2 = this.operand2.eval();
        if (e1.isVoid()) {
            throw new EvaluationError(this, "Argument " + this.operand1 + " is void. Cannot apply an operator on void objects. Code: " + this.getCode());
        }
        if (e2.isVoid()) {
            throw new EvaluationError(this, "Argument " + this.operand2 + " is void. Cannot apply an operator on void objects. Code: " + this.getCode());
        }
        try {
            if (this.operator.getCode().equals("+")) {
                return e1.add(e2);
            }
            if (this.operator.getCode().equals("-")) {
                return e1.sub(e2);
            }
            throw new EvaluationError(this, "unknown operator: " + this.operator);
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

