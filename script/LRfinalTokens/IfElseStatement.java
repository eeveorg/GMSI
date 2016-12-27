/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRfinalTokens.IfStatement;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.WordElse;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;

public class IfElseStatement
extends Statement {
    Expression condition;
    Statement thenStatement;
    Statement elseStatement;

    public IfElseStatement(IfStatement i, WordElse we, Statement s2) {
        super(i, i + we + s2);
        this.condition = i.condition;
        this.thenStatement = i.thenStatement;
        this.elseStatement = s2;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.condition = (Expression)this.condition.unwrap();
        this.thenStatement = (Statement)this.thenStatement.unwrap();
        this.elseStatement = (Statement)this.elseStatement.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject condResult = this.condition.eval();
        if (!(condResult instanceof BoolObject)) {
            throw new EvaluationError(this, "This expression in the if-condition doesn't return a boolean value, but a value of type " + condResult.getType() + ":" + this.condition.getCode());
        }
        if (((BoolObject)condResult).getValue()) {
            this.thenStatement.eval();
        } else {
            this.elseStatement.eval();
        }
        return voidResult;
    }
}

