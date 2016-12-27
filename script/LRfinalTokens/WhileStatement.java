/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.WordWhile;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;

public class WhileStatement
extends Statement {
    Expression condition;
    Statement thenStatement;
    EvaluationResult skipListener;

    public WhileStatement(WordWhile w, OpenBracket ob, Expression e, CloseBracket b, Statement s) {
        super(w, w + ob + e + b + s);
        this.condition = e;
        this.thenStatement = s;
        this.skipListener = this.owner.getEvalResult();
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
        do {
            DataObject condResult;
            if (!((condResult = this.condition.eval()) instanceof BoolObject)) {
                throw new EvaluationError(this, "This expression in the if-condition doesn't return a boolean value, but a value of type " + condResult.getType() + ":" + this.condition.getCode());
            }
            if (!((BoolObject)condResult).getValue()) break;
            this.thenStatement.eval();
            if (this.skipListener.wantBreak()) {
                this.skipListener.clearFlags();
                break;
            }
            if (this.skipListener.wantReturn()) break;
            if (!this.skipListener.wantContinue()) continue;
            this.skipListener.clearFlags();
        } while (true);
        return voidResult;
    }
}

