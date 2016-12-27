/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.Semicolon;
import script.LRterminals.WordReturn;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class ReturnStatement
extends Statement {
    public Expression returnResult;
    private EvaluationResult returnListener;

    public ReturnStatement(WordReturn b, Semicolon s) throws ParseError {
        super(b, "return;");
        this.returnResult = null;
        this.returnListener = this.owner.getEvalResult();
    }

    public ReturnStatement(WordReturn b, Expression e, Semicolon s) throws ParseError {
        super(b, "return " + e + ";");
        this.returnResult = e;
        this.returnListener = this.owner.getEvalResult();
    }

    @Override
    public Token unwrap() throws ParseError {
        if (this.returnResult == null) {
            return this;
        }
        this.returnResult = (Expression)this.returnResult.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        if (this.returnResult == null) {
            this.returnListener.setReturn();
        } else {
            this.returnListener.setReturn(this.returnResult.eval());
        }
        return voidResult;
    }
}

