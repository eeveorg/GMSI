/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Statement;
import script.LRterminals.Semicolon;
import script.LRterminals.WordBreak;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class BreakStatement
extends Statement {
    private EvaluationResult breakListener;

    public BreakStatement(WordBreak b, Semicolon s) throws ParseError {
        super(b, "break;");
        this.breakListener = this.owner.getEvalResult();
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        this.breakListener.setBreak();
        return voidResult;
    }
}

