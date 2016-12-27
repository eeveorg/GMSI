/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.TypeCast;
import script.LRhighTokens.TypeName;
import script.LRterminals.UnOp;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class UnOpExpression
extends Expression {
    public Expression operand1;
    public String operator;
    private TypeName castTo;

    public UnOpExpression(UnOp Operator, Expression Op1) {
        super(Operator, Operator + Op1);
        this.operand1 = Op1;
        this.operator = Operator;
        this.castTo = null;
    }

    public UnOpExpression(TypeCast Operator, Expression Op1) {
        super(Operator, Operator + Op1);
        this.operand1 = Op1;
        this.operator = Operator;
        this.castTo = Operator.typeName;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand1 = (Expression)this.operand1.unwrap();
        if (this.castTo != null) {
            this.castTo = (TypeName)this.castTo.unwrap();
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject e1 = this.operand1.eval();
        if (e1.isVoid()) {
            throw new EvaluationError(this, "Trying to apply the operator " + this.operator + " on a void argument in code " + this.getCode());
        }
        if (this.castTo != null) {
            try {
                return e1.explicitCastTo(this.castTo.getType());
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, e.getMessage());
            }
        }
        if (this.operator.equals("$")) {
            this.owner.addScriptReplacement(e1.toString());
            return e1;
        }
        if (this.operator.equals("+") || this.operator.equals("-")) {
            if (this.operator.equals("-")) {
                try {
                    return e1.negate();
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this, e.getMessage());
                }
            }
            return e1;
        }
        if (this.operator.equals("!")) {
            try {
                return e1.not();
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, e.getMessage());
            }
        }
        throw new Error("WTF?:" + this.code);
    }
}

