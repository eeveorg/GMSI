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
import script.dataObjects.StringObject;

public class IntObject
extends DataObject {
    private final int value;

    public IntObject(int i) {
        this.type = DataType.INT;
        this.value = i;
    }

    @Override
    public DataObject clone() {
        return new IntObject(this.value);
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
        if (d.getBasicType() == DataType.types.FLOAT) {
            return new FloatObject(this.value);
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.INT) {
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
        if (d.getBasicType() == DataType.types.FLOAT) {
            return new FloatObject(this.value);
        }
        if (d.getBasicType() == DataType.types.BOOL) {
            return BoolObject.getBool(this.value != 0);
        }
        throw new InternalScriptError("Explicit cast error: Cannot cast from " + this.type + " to " + d);
    }

    @Override
    public DataObject add(DataObject o) throws InternalScriptError {
        if (o instanceof StringObject) {
            return new StringObject(this + o);
        }
        if (o instanceof IntObject) {
            return new IntObject(this.value + ((IntObject)o).value);
        }
        if (o instanceof FloatObject) {
            return new FloatObject((float)this.value + ((FloatObject)o).getValue());
        }
        return super.add(o);
    }

    @Override
    public DataObject increment(int i) {
        return new IntObject(this.value + i);
    }

    @Override
    public DataObject sub(DataObject o) throws InternalScriptError {
        if (o instanceof IntObject) {
            return new IntObject(this.value - ((IntObject)o).value);
        }
        if (o instanceof FloatObject) {
            return new FloatObject((float)this.value - ((FloatObject)o).getValue());
        }
        return super.sub(o);
    }

    @Override
    public DataObject mul(DataObject o) throws InternalScriptError {
        if (o instanceof IntObject) {
            return new IntObject(this.value * ((IntObject)o).value);
        }
        if (o instanceof FloatObject) {
            return new FloatObject((float)this.value * ((FloatObject)o).getValue());
        }
        return super.mul(o);
    }

    @Override
    public DataObject div(DataObject o) throws InternalScriptError {
        if (o instanceof IntObject) {
            if (((IntObject)o).value == 0) {
                throw new InternalScriptError("Division by zero!");
            }
            return new IntObject(this.value / ((IntObject)o).value);
        }
        if (o instanceof FloatObject) {
            if (((FloatObject)o).getValue() == 0.0f) {
                throw new InternalScriptError("Division by zero!");
            }
            return new FloatObject((float)this.value / ((FloatObject)o).getValue());
        }
        return super.div(o);
    }

    @Override
    public DataObject mod(DataObject o) throws InternalScriptError {
        if (o instanceof IntObject) {
            return new IntObject(this.value % ((IntObject)o).value);
        }
        if (o instanceof FloatObject) {
            return new FloatObject((float)this.value % ((FloatObject)o).getValue());
        }
        return super.mod(o);
    }

    @Override
    public DataObject negate() throws InternalScriptError {
        return new IntObject(- this.value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (o instanceof IntObject) {
            int i1 = this.value;
            int i2 = ((IntObject)o).value;
            switch (operator) {
                case 3: {
                    if (i1 < i2) {
                        return true;
                    }
                    return false;
                }
                case 5: {
                    if (i1 > i2) {
                        return true;
                    }
                    return false;
                }
                case 2: {
                    if (i1 <= i2) {
                        return true;
                    }
                    return false;
                }
                case 4: {
                    if (i1 >= i2) {
                        return true;
                    }
                    return false;
                }
                case 0: {
                    if (i1 == i2) {
                        return true;
                    }
                    return false;
                }
                case 1: {
                    if (i1 != i2) {
                        return true;
                    }
                    return false;
                }
            }
        } else if (o instanceof FloatObject) {
            float i1 = this.value;
            float i2 = ((FloatObject)o).getValue();
            switch (operator) {
                case 3: {
                    if (i1 < i2) {
                        return true;
                    }
                    return false;
                }
                case 5: {
                    if (i1 > i2) {
                        return true;
                    }
                    return false;
                }
                case 2: {
                    if (i1 <= i2) {
                        return true;
                    }
                    return false;
                }
                case 4: {
                    if (i1 >= i2) {
                        return true;
                    }
                    return false;
                }
                case 0: {
                    if (i1 == i2) {
                        return true;
                    }
                    return false;
                }
                case 1: {
                    if (i1 != i2) {
                        return true;
                    }
                    return false;
                }
            }
        } else {
            throw new InternalScriptError("Cannot compare " + this.getType() + " with " + o.getType());
        }
        throw new InternalScriptError("Unknown operator " + operator);
    }
}

