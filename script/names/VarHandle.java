/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.names.VarSpace;

public class VarHandle {
    private Object name;
    private VarSpace space;

    protected VarHandle() {
    }

    public VarHandle(VarSpace cont, Object name) {
        this.name = name;
        this.space = cont;
    }

    public boolean erase() {
        return this.space.erase(this.name);
    }

    public DataObject getValue() throws InternalScriptError {
        DataObject result = this.space.get(this.name);
        if (result == null) {
            return new NullObject();
        }
        return result;
    }

    public void setValue(DataObject newValue) throws InternalScriptError {
        this.space.put(this.name, newValue);
    }
}

