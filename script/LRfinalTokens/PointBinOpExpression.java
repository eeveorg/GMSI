/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRterminals.PointBinOp;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class PointBinOpExpression
extends Expression {
    private Expression operand1;
    private Expression operand2;
    private PointBinOp operator;

    public PointBinOpExpression(Expression Op1, PointBinOp Operator, Expression Op2) {
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
        DataObject result;
        block5 : {
            super.eval();
            DataObject e1 = this.operand1.eval();
            DataObject e2 = this.operand2.eval();
            try {
                if (this.operator.getCode().equals("*")) {
                    result = e1.mul(e2);
                    break block5;
                }
                if (this.operator.getCode().equals("%")) {
                    result = e1.mod(e2);
                    break block5;
                }
                if (this.operator.getCode().equals("/")) {
                    result = e1.div(e2);
                    break block5;
                }
                throw new Error("WTF? Operator: " + this.code);
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, e.getMessage());
            }
        }
        return result;
    }
}

