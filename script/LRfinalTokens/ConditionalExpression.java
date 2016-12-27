/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.LRterminals.Colon;
import script.LRterminals.Questionmark;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class ConditionalExpression
extends Expression {
    private Expression condition;
    private Expression thenExpression;
    private Expression elseExpression;

    public ConditionalExpression(Expression cond, Questionmark q, Expression thenE, Colon c, Expression elseE) {
        super(cond, cond + "?" + thenE + ":" + elseE);
        this.condition = cond;
        this.thenExpression = thenE;
        this.elseExpression = elseE;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.condition = (Expression)this.condition.unwrap();
        this.thenExpression = (Expression)this.thenExpression.unwrap();
        this.elseExpression = (Expression)this.elseExpression.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject condResult = this.condition.eval();
        if (!(condResult instanceof BoolObject)) {
            throw new EvaluationError(this, "The condition in a conditional expression must be boolean, but it is a " + condResult.getType() + ". Code: " + this.code);
        }
        if (((BoolObject)condResult).getValue()) {
            return this.thenExpression.eval();
        }
        return this.elseExpression.eval();
    }
}

