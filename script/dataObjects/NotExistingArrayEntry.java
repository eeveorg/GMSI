/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;

public class NotExistingArrayEntry {
    public NotExistingArrayEntry(Script s) throws InternalScriptError {
    }

    public DataObject getValue() throws InternalScriptError {
        return new NullObject();
    }

    public void setValue(DataObject newValue) throws InternalScriptError {
        throw new InternalScriptError("Trying to set the value of a null pointer!");
    }

    public String toString() {
        return "null";
    }
}

