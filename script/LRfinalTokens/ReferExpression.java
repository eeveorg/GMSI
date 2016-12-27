/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.ReferOperator;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.ReferenceObject;
import script.names.VarHandle;

public class ReferExpression
extends Expression {
    Name operand;

    public ReferExpression(ReferOperator Operator, Name Op1) {
        super(Operator, Operator + Op1);
        this.operand = Op1;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand = (Name)this.operand.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        ReferenceObject o;
        super.eval();
        try {
            o = new ReferenceObject(this.operand.resolve());
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return o;
    }
}

