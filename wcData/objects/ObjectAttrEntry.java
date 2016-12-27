/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import script.dataObjects.DataObject;
import wcData.objects.AttrIdentifier;

public class ObjectAttrEntry {
    private AttrIdentifier ident;
    private DataObject data;

    public ObjectAttrEntry(AttrIdentifier i, DataObject o) {
        this.ident = i;
        this.data = o;
    }

    public DataObject getData() {
        return this.data;
    }

    public void setData(DataObject data) {
        this.data = data;
    }

    public AttrIdentifier getIdent() {
        return this.ident;
    }
}

