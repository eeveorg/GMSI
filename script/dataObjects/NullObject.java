/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class NullObject
extends DataObject {
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        return new NullObject(d);
    }

    public NullObject() {
        this.type = new DataType(DataType.types.NONE);
    }

    public NullObject(DataType d) {
        this.type = d;
    }

    public String toString() {
        return "null";
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        throw new InternalScriptError("Cannot compare null");
    }
}

