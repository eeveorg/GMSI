/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Statement;
import script.LRterminals.Semicolon;
import script.LRterminals.WordContinue;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class ContinueStatement
extends Statement {
    private EvaluationResult continueListener;

    public ContinueStatement(WordContinue b, Semicolon s) throws ParseError {
        super(b, "continue;");
        this.continueListener = this.owner.getEvalResult();
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        this.continueListener.setContinue();
        return voidResult;
    }
}

