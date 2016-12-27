/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.List;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;

public class BoolObject
extends DataObject {
    private final boolean value;
    public static BoolObject TRUE = new BoolObject(true);
    public static BoolObject FALSE = new BoolObject(false);

    private BoolObject(boolean b) {
        this.type = new DataType(DataType.types.BOOL);
        this.value = b;
    }

    public static BoolObject getBool(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public DataObject clone() {
        return this.value ? TRUE : FALSE;
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (this.getType().isDerivedFrom(d)) {
            return this;
        }
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (d.getBasicType() == DataType.types.STRING) {
            return new StringObject((String)((Object)this));
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.BOOL) {
            return this;
        }
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (d.getBasicType() == DataType.types.USER) {
            DataObject result = this.explicitCastTo(d.getDefinition().getParentTypes().get(0));
            result.type = d;
            return result;
        }
        if (d.getBasicType() == DataType.types.STRING) {
            return new StringObject((String)((Object)this));
        }
        if (d.getBasicType() == DataType.types.INT) {
            return new IntObject(this.value ? 1 : 0);
        }
        if (d.getBasicType() == DataType.types.FLOAT) {
            return new FloatObject(this.value ? 1.0f : 0.0f);
        }
        throw new InternalScriptError("Explicit cast error: Cannot cast from " + this.type + " to " + d);
    }

    @Override
    public DataObject and(Expression e) throws InternalScriptError {
        if (!this.value) {
            return FALSE;
        }
        DataObject o = e.eval();
        if (o instanceof BoolObject) {
            return this.value && ((BoolObject)o).value ? TRUE : FALSE;
        }
        return this.throwUndefinedError(o, "||");
    }

    @Override
    public DataObject or(Expression e) throws InternalScriptError {
        if (this.value) {
            return TRUE;
        }
        DataObject o = e.eval();
        if (o instanceof BoolObject) {
            return this.value || ((BoolObject)o).value ? TRUE : FALSE;
        }
        return this.throwUndefinedError(o, "||");
    }

    @Override
    public DataObject not() throws InternalScriptError {
        return !this.value ? TRUE : FALSE;
    }

    public String toString() {
        if (this.value) {
            return "true";
        }
        return "false";
    }

    public boolean equals(Object o) {
        if (!(o instanceof BoolObject)) {
            return false;
        }
        if (((BoolObject)o).value == this.value) {
            return true;
        }
        return false;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof BoolObject)) {
            throw new InternalScriptError("Cannot compare " + this.getType() + " with " + o.getType());
        }
        switch (operator) {
            case 0: {
                return o.equals(this);
            }
            case 1: {
                return !o.equals(this);
            }
        }
        throw new InternalScriptError("Evaluation Error: Cannot compare booleans with " + operator);
    }
}

