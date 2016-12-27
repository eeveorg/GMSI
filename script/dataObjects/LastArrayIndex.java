/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class LastArrayIndex
extends DataObject {
    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        throw new InternalScriptError("WTF DefaultArrayIndex");
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        throw new InternalScriptError("WTF LastArrayIndex:compare");
    }
}

