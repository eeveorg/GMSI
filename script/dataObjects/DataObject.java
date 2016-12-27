/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.dataObjects.BoolObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;

public abstract class DataObject {
    protected DataType type;
    protected DataType contentType;
    private Object additionalData;

    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        throw new ImplicitCastError("Cannot cast from var to anything implicitly!");
    }

    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        return this.implicitCastTo(d);
    }

    public DataObject clone() {
        return null;
    }

    public boolean isStruct() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public static DataObject wrap(Object o) {
        if (o instanceof String) {
            return new StringObject((String)o);
        }
        if (o instanceof Float) {
            return new FloatObject(((Float)o).floatValue());
        }
        if (o instanceof Boolean) {
            return BoolObject.getBool((Boolean)o);
        }
        if (o instanceof Integer) {
            return new IntObject((Integer)o);
        }
        return new StringObject("WTFDataObject " + o.toString());
    }

    public DataType getType() {
        return this.type;
    }

    public void setType(DataType t) {
        this.type = t;
    }

    protected DataObject throwUndefinedError(DataObject o, String operator) throws InternalScriptError {
        if (o == null) {
            if (this.isNull()) {
                throw new InternalScriptError("Trying to apply the operator " + operator + " onto a null value!");
            }
            throw new InternalScriptError("The operator " + operator + " is undefined for the type " + this.type);
        }
        if (this.isNull() && o.isNull()) {
            throw new InternalScriptError("Both sides of a binary expression (" + operator + ") are null!");
        }
        if (this.isNull()) {
            throw new InternalScriptError("The left side of a binary expression (" + operator + ") is null!");
        }
        if (o.isNull()) {
            throw new InternalScriptError("The right side of a binary expression (" + operator + ") is null!");
        }
        throw new InternalScriptError("The operator " + operator + " is undefined for the types " + this.type + ", " + o.type);
    }

    public DataObject add(DataObject o) throws InternalScriptError {
        if (o.getType().getBasicType() == DataType.types.STRING) {
            return new StringObject(this + o);
        }
        return this.throwUndefinedError(o, "+");
    }

    public DataObject sub(DataObject o) throws InternalScriptError {
        return this.throwUndefinedError(o, "-");
    }

    public DataObject mul(DataObject o) throws InternalScriptError {
        return this.throwUndefinedError(o, "*");
    }

    public DataObject div(DataObject o) throws InternalScriptError {
        return this.throwUndefinedError(o, "/");
    }

    public DataObject increment(int i) throws InternalScriptError {
        return this.throwUndefinedError(null, i == 1 ? "++" : "--");
    }

    public boolean isVoid() {
        return false;
    }

    public DataObject mod(DataObject o) throws InternalScriptError {
        return this.throwUndefinedError(o, "%");
    }

    public DataObject and(Expression e) throws InternalScriptError {
        return this.throwUndefinedError(e.eval(), "&&");
    }

    public DataObject or(Expression e) throws InternalScriptError {
        return this.throwUndefinedError(e.eval(), "||");
    }

    public DataObject not() throws InternalScriptError {
        return this.throwUndefinedError(null, "!");
    }

    public DataObject negate() throws InternalScriptError {
        return this.throwUndefinedError(null, "-");
    }

    public boolean isNullPointer() {
        return false;
    }

    public DataType getContentDataType() {
        return this.type;
    }

    public Object getAdditionalData() {
        return this.additionalData;
    }

    public void setAdditionalData(Object o) {
        this.additionalData = o;
    }

    public abstract boolean compare(DataObject var1, int var2) throws InternalScriptError;
}

