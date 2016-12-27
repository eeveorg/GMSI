/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.Assignment;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.names.NameResolver;
import script.names.VarHandle;

public class AssignExpression
extends Expression {
    public Name lValue;
    public Expression rValue;
    public Assignment operator;

    public AssignExpression(Name n, Assignment a, Expression e) {
        super(n, n + a + e);
        this.lValue = n;
        this.operator = a;
        this.rValue = e;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.rValue = (Expression)this.rValue.unwrap();
        this.lValue = (Name)this.lValue.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject result;
        block13 : {
            super.eval();
            DataObject e = this.rValue.eval();
            if (e.isVoid()) {
                throw new EvaluationError(this, "Cannot apply a void argument to a variable in code: " + this.code);
            }
            if (this.operator.getCode().equals("=")) {
                try {
                    this.owner.getNameResolver().setCreateArrayIndex(true);
                    this.lValue.resolve().setValue(e);
                    this.owner.getNameResolver().setCreateArrayIndex(false);
                }
                catch (InternalScriptError e1) {
                    throw new EvaluationError(this, e1.getMessage());
                }
                return e;
            }
            DataObject o = this.lValue.eval();
            try {
                if (this.operator.getCode().equals("+=")) {
                    result = o.add(e);
                    break block13;
                }
                if (this.operator.getCode().equals("*=")) {
                    result = o.mul(e);
                    break block13;
                }
                if (this.operator.getCode().equals("/=")) {
                    result = o.div(e);
                    break block13;
                }
                if (this.operator.getCode().equals("-=")) {
                    result = o.sub(e);
                    break block13;
                }
                if (this.operator.getCode().equals("%=")) {
                    result = o.mod(e);
                    break block13;
                }
                throw new EvaluationError(this, "Unknown operator:" + this.operator);
            }
            catch (InternalScriptError ee) {
                throw new EvaluationError(this, ee.getMessage());
            }
        }
        try {
            this.lValue.resolve().setValue(result);
        }
        catch (InternalScriptError e1) {
            throw new EvaluationError(this, e1.getMessage());
        }
        return result;
    }
}

