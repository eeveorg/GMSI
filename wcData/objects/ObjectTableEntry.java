/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.TypeTest;
import script.names.VarSpace;
import wcData.objects.AttrIdentifier;
import wcData.objects.MetaData;
import wcData.objects.ObjectAttrEntry;
import wcData.objects.WC3DefaultValues;
import wcData.objects.WC3Object;

public class ObjectTableEntry
implements VarSpace,
WC3Object {
    private HashMap<String, ObjectAttrEntry> table = null;
    private String category;
    private String origId;
    private String newId;

    public ObjectTableEntry(String type, String origId, int size) {
        this.table = new HashMap(size);
        this.category = type;
        this.origId = origId;
        this.newId = null;
    }

    public ObjectTableEntry(String type, String origId, String newId, int size) {
        this.table = new HashMap(size);
        this.category = type;
        this.origId = origId;
        this.newId = newId;
    }

    @Override
    public String getOrigId() {
        return this.origId;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getId() {
        if (this.newId == null) {
            return this.origId;
        }
        return this.newId;
    }

    public String getNewId() {
        return this.newId;
    }

    public boolean isOriginal() {
        if (this.newId == null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<String, ObjectAttrEntry> e : this.table.entrySet()) {
            b.append(e.getKey());
            b.append(" => ");
            b.append(e.getValue().getData());
            b.append("\n");
        }
        return b.toString();
    }

    @Override
    public DataObject get(Object key) throws InternalScriptError {
        if (!this.table.containsKey(key)) {
            ObjectAttrEntry entry = WC3DefaultValues.getDefaultValues().getDefaultValue(this, (String)key);
            if (entry == null) {
                return null;
            }
            return entry.getData();
        }
        return this.table.get(key).getData();
    }

    @Override
    public void put(Object key, DataObject content) throws InternalScriptError {
        ObjectAttrEntry oldValue;
        boolean doWrite = false;
        if (!this.table.containsKey(key)) {
            oldValue = WC3DefaultValues.getDefaultValues().getDefaultValue(this, (String)key);
            doWrite = true;
        } else {
            oldValue = this.table.get(key);
        }
        if (oldValue != null) {
            oldValue.setData(content.implicitCastTo(oldValue.getData().getType()));
            if (doWrite) {
                this.table.put((String)key, oldValue);
            }
        } else {
            try {
                this.table.put((String)key, new ObjectAttrEntry(WC3DefaultValues.getDefaultValues().gameNameToIdentifier((String)key, this), content));
            }
            catch (MetaData.UnknownAttributeException e) {
                throw new InternalScriptError("Trying to set a non existing attribute for a WC3 object!\nObject: " + this.getId() + "(" + this.getCategory() + ")\nAttribute:" + key);
            }
        }
    }

    public void putRawEntry(String key, ObjectAttrEntry e) {
        this.table.put(key, e);
    }

    public ObjectAttrEntry getRawEntry(String key) {
        return this.table.get(key);
    }

    @Override
    public boolean hasName(Object key) {
        if (!this.table.containsKey(key) && !WC3DefaultValues.getDefaultValues().hasDefaultValue(this, (String)key)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean erase(Object key) {
        if (this.table.remove(key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Set<? extends Object> getKeys() {
        return this.table.keySet();
    }

    @Override
    public TypeTest getTypeTest() {
        return null;
    }

    @Override
    public void setTypeTest(TypeTest t, boolean eraseNonMatching) throws InternalScriptError {
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public void setOrigId(String newId) {
        this.origId = newId;
    }
}

