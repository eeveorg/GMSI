/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.List;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;

public class StringObject
extends DataObject {
    private final String value;

    public StringObject(String s) {
        this.type = new DataType(DataType.types.STRING);
        this.value = s;
    }

    @Override
    public DataObject clone() {
        return new StringObject(String.valueOf(this.value));
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (this.getType().isDerivedFrom(d)) {
            return this;
        }
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.STRING) {
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
        if (d.getBasicType() == DataType.types.INT) {
            try {
                return new IntObject(Integer.parseInt(this.value));
            }
            catch (NumberFormatException e) {
                throw new InternalScriptError("Typecast error: Unable to cast the string \"" + this.value + "\" to int.");
            }
        }
        if (d.getBasicType() == DataType.types.FLOAT) {
            try {
                return new FloatObject(Float.parseFloat(this.value));
            }
            catch (NumberFormatException e) {
                throw new InternalScriptError("Typecast error: Unable to cast the string \"" + this.value + "\" to float.");
            }
        }
        if (d.getBasicType() == DataType.types.BOOL) {
            try {
                return BoolObject.getBool(Boolean.parseBoolean(this.value));
            }
            catch (NumberFormatException e) {
                throw new InternalScriptError("Typecast error: Unable to cast the string \"" + this.value + "\" to bool.");
            }
        }
        throw new InternalScriptError("Explicit cast error: Cannot cast from " + this.type + " to " + d);
    }

    @Override
    public DataObject add(DataObject o) throws InternalScriptError {
        return new StringObject(this + o);
    }

    public String toString() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (!(o instanceof StringObject)) {
            return false;
        }
        return ((StringObject)o).value.equals(this.value);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof StringObject)) {
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
        throw new InternalScriptError("Evaluation Error: Cannot compare Strings with " + operator);
    }
}

