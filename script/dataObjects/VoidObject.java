/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.ImplicitCastError;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class VoidObject
extends DataObject {
    private static VoidObject theObject = new VoidObject();

    public static VoidObject getVoidObject() {
        return theObject;
    }

    private VoidObject() {
        this.type = new DataType(DataType.types.VOID);
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VOID) {
            return this;
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    public String toString() {
        return "void";
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        throw new ImplicitCastError("Cannot compare to void!");
    }
}

