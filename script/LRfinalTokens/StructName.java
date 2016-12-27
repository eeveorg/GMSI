/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.Identifyer;
import script.LRterminals.Point;
import script.ParseError;
import script.Token;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.NullObject;
import script.dataObjects.StructObject;
import script.names.VarHandle;

public class StructName
extends Name {
    public Expression l;
    public Identifyer r;

    public StructName(Expression l, Point p, Identifyer r) {
        super(l, l + "." + r);
        this.l = l;
        this.r = r;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.l = (Expression)this.l.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject o;
        super.eval();
        try {
            o = this.resolve().getValue();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return o;
    }

    @Override
    public VarHandle resolve() throws EvaluationError {
        DataObject lValue = this.l.eval();
        if (lValue instanceof StructObject) {
            try {
                return ((StructObject)lValue).getEntry(this.r.getCode());
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, e.getMessage());
            }
        }
        if (lValue instanceof ArrayObject) {
            return ((ArrayObject)lValue).getEntry(this.r.getCode());
        }
        if (lValue instanceof NullObject) {
            throw new EvaluationError(this, "The left side of a \".\" operator is null!\nLeft side: " + this.l.getCode());
        }
        throw new EvaluationError(this, "The left side of a \".\" operator must be an array or a struct!\nLeft side: " + this.l.getCode() + "\nType of left side: " + lValue.getType());
    }
}

