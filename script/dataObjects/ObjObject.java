/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.List;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.StringObject;

public class ObjObject
extends DataObject {
    private Object value;

    public ObjObject(Object v) {
        this.type = new DataType(DataType.types.OBJECT);
        this.value = v;
    }

    @Override
    public DataObject clone() {
        throw new Error("CANNOT CLONE OBJECTS!");
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
        if (d.getBasicType() == DataType.types.OBJECT) {
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
        throw new InternalScriptError("Explicit cast error: Cannot cast from " + this.type + " to " + d);
    }

    public String toString() {
        return this.value.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof ObjObject)) {
            return false;
        }
        if (((ObjObject)o).value == this.value) {
            return true;
        }
        return false;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof ObjObject)) {
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
        throw new InternalScriptError("Evaluation Error: Cannot compare objects with " + operator);
    }
}

