/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class TypedDatafield {
    public DataObject content;
    public DataType type;

    public TypedDatafield(DataObject content, DataType type) {
        this.content = content;
        this.type = type;
    }
}

