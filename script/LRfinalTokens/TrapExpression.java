/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.StringConstant;
import script.LRterminals.WordTrap;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;
import script.systemCalls.TrapHandler;

public class TrapExpression
extends Expression {
    Trap trap;

    public TrapExpression(WordTrap b, OpenBracket o, StringConstant c, CloseBracket cl) throws ParseError {
        super(b, b + o + c + cl);
        try {
            this.trap = this.owner.getTrapHandler().getTrap(((StringObject)c.eval()).getValue());
        }
        catch (EvaluationError e) {
            throw new ParseError(e.getMessage());
        }
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        try {
            return this.trap.apply();
        }
        catch (InternalScriptError e) {
            if (e instanceof EvaluationError) {
                throw (EvaluationError)e;
            }
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

