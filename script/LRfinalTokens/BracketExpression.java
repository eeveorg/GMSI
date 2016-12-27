/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class BracketExpression
extends Expression {
    private Expression operand;

    public BracketExpression(OpenBracket B1, Expression Operand, CloseBracket B2) {
        super(B1, "(" + Operand + ")");
        this.operand = Operand;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand = (Expression)this.operand.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return this.operand.eval();
    }
}

