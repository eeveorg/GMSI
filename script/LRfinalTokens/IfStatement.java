/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.WordIf;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;

public class IfStatement
extends Statement {
    Expression condition;
    Statement thenStatement;

    public IfStatement(WordIf i, OpenBracket o, Expression e, CloseBracket b, Statement s) {
        super(i, i + o + e + b + s);
        this.condition = e;
        this.thenStatement = s;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.condition = (Expression)this.condition.unwrap();
        this.thenStatement = (Statement)this.thenStatement.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject condResult = this.condition.eval();
        if (!(condResult instanceof BoolObject)) {
            throw new EvaluationError(this, "This expression in the if-condition doesn't return a boolean value, but a value of type " + condResult.getType() + ":\n" + this.condition.getCode());
        }
        if (((BoolObject)condResult).getValue()) {
            this.thenStatement.eval();
        }
        return voidResult;
    }
}

