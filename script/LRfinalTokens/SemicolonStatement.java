/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.Semicolon;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class SemicolonStatement
extends Statement {
    Expression theExpression;

    public SemicolonStatement(Expression e, Semicolon s) {
        super(e, e + s);
        this.theExpression = e;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.theExpression = (Expression)this.theExpression.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        this.theExpression.eval();
        return voidResult;
    }
}

