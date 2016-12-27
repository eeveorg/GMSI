/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRterminals.BBinOp;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class BoolBinOpExpression
extends Expression {
    private Expression operand1;
    private Expression operand2;
    private BBinOp operator;

    public BoolBinOpExpression(Expression O1, BBinOp Operator, Expression O2) {
        super(O1, O1 + Operator + O2);
        this.operand1 = O1;
        this.operand2 = O2;
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
        try {
            if (this.operator.getCode().equals("&&")) {
                return e1.and(this.operand2);
            }
            if (this.operator.getCode().equals("||")) {
                return e1.or(this.operand2);
            }
            throw new EvaluationError(this, "unknown operator:" + this.operator);
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

