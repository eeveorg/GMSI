/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.CloseArrayBracket;
import script.LRterminals.OpenArrayBracket;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.IntObject;
import script.dataObjects.LastArrayIndex;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarHandle;

public class ArrayName
extends Name {
    public Expression name;
    public Expression index;
    public boolean isDefault = false;

    public ArrayName(Expression o, OpenArrayBracket ob, Expression index, CloseArrayBracket cab) {
        super(o, o + ob + index + cab);
        this.name = o;
        this.index = index;
    }

    public ArrayName(Expression o, OpenArrayBracket ob, CloseArrayBracket cab) {
        super(o, o + ob + cab);
        this.name = o;
        this.index = null;
        this.isDefault = true;
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

    public void createIndices(boolean doIt) {
    }

    @Override
    public VarHandle resolve() throws EvaluationError {
        DataObject lValue = this.name.eval();
        if ((lValue == null || lValue instanceof NullObject) && this.owner.getNameResolver().wantCreateArrayIndex()) {
            if (this.name instanceof ArrayName) {
                try {
                    ((ArrayName)this.name).resolve().setValue(new ArrayObject());
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this, e.getMessage());
                }
            }
            lValue = this.name.eval();
        }
        DataObject rValue = this.index != null ? this.index.eval() : new LastArrayIndex();
        if (lValue instanceof ArrayObject) {
            if (rValue instanceof StringObject) {
                return ((ArrayObject)lValue).getEntry(((StringObject)rValue).getValue());
            }
            if (rValue instanceof IntObject) {
                return ((ArrayObject)lValue).getEntry(((IntObject)rValue).getValue());
            }
            if (rValue instanceof LastArrayIndex) {
                return ((ArrayObject)lValue).getHighEntry();
            }
            throw new EvaluationError(this, "The index of an array must be of type int or string, but it is a " + rValue.getType());
        }
        if (lValue instanceof StructObject) {
            if (rValue instanceof StringObject) {
                try {
                    return ((StructObject)lValue).getEntry(((StringObject)rValue).getValue());
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this, e.getMessage());
                }
            }
            throw new EvaluationError(this, "The index of a struct must be of type string, but it is a " + rValue.getType());
        }
        if (lValue instanceof NullObject) {
            throw new EvaluationError(this, "The left side of an x[y] expression is null!\nLeft side: " + this.name.getCode());
        }
        throw new EvaluationError(this, "The left side of an x[y] expression must be either an array or a struct\nLeft side: " + this.name.getCode() + "\nType of left side: " + (lValue == null ? "null" : lValue.getType()));
    }

    @Override
    public Token unwrap() throws ParseError {
        this.name = (Expression)this.name.unwrap();
        if (this.index != null) {
            this.index = (Expression)this.index.unwrap();
        }
        return this;
    }
}

