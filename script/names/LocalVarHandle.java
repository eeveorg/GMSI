/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.NullObject;
import script.dataObjects.TypedDatafield;
import script.names.VarHandle;

public class LocalVarHandle
extends VarHandle {
    protected TypedDatafield[] space;
    protected int num;

    public LocalVarHandle(TypedDatafield[] cont, int num) {
        this.space = cont;
        this.num = num;
    }

    @Override
    public boolean erase() {
        boolean result = this.space[this.num] != null;
        this.space[this.num] = null;
        return result;
    }

    @Override
    public DataObject getValue() throws InternalScriptError {
        DataObject result = this.space[this.num].content;
        if (result == null) {
            return new NullObject();
        }
        return result;
    }

    @Override
    public void setValue(DataObject newValue) throws InternalScriptError {
        this.space[this.num].content = this.space[this.num] != null ? newValue.implicitCastTo(this.space[this.num].type) : newValue;
    }
}

