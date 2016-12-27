/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.EvaluationResult;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.Semicolon;
import script.LRterminals.WordFor;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;

public class ForStatement
extends Statement {
    Statement beforeStatement;
    Expression condition;
    Expression afterLoopExpression;
    Statement thenStatement;
    private EvaluationResult skipListener;

    public ForStatement(WordFor w, OpenBracket bb, Statement b, Expression e, Semicolon se, Expression e2, CloseBracket bbb, Statement s) {
        super(w, w + bb + b + e + se + e2 + bbb + s);
        this.beforeStatement = b;
        this.condition = e;
        this.afterLoopExpression = e2;
        this.thenStatement = s;
        this.skipListener = this.owner.getEvalResult();
    }

    @Override
    public Token unwrap() throws ParseError {
        LocalNameMapper.getMapper().pushSpace();
        this.beforeStatement = (Statement)this.beforeStatement.unwrap();
        this.condition = (Expression)this.condition.unwrap();
        this.afterLoopExpression = (Expression)this.afterLoopExpression.unwrap();
        this.thenStatement = (Statement)this.thenStatement.unwrap();
        LocalNameMapper.getMapper().popSpace();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        this.beforeStatement.eval();
        do {
            BoolObject o;
            DataObject r = this.condition.eval();
            try {
                o = r instanceof BoolObject ? (BoolObject)r : (BoolObject)r.implicitCastTo(DataType.BOOL);
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this.condition, "The condition in the head of a for loop must return a boolean value, but it returns a " + r.getType());
            }
            if (!o.getValue()) break;
            this.thenStatement.eval();
            if (this.skipListener.wantBreak()) {
                this.skipListener.clearFlags();
                break;
            }
            if (this.skipListener.wantReturn()) break;
            if (this.skipListener.wantContinue()) {
                this.skipListener.clearFlags();
            }
            this.afterLoopExpression.eval();
        } while (true);
        return voidResult;
    }
}

