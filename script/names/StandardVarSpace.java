/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.TypeTest;
import script.names.VarSpace;

public class StandardVarSpace
implements VarSpace {
    protected HashMap<String, DataObject> map;

    public StandardVarSpace() {
        this.map = new HashMap();
    }

    public StandardVarSpace(int size) {
        this.map = new HashMap(size);
    }

    @Override
    public boolean erase(Object key) {
        if (this.map.remove(key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public DataObject get(Object key) throws InternalScriptError {
        return this.map.get(key);
    }

    @Override
    public void put(Object key, DataObject content) throws InternalScriptError {
        DataObject oldValue = this.map.get(key);
        if (oldValue != null) {
            this.map.put((String)key, content.implicitCastTo(oldValue.getType()));
        } else {
            this.map.put((String)key, content);
        }
    }

    @Override
    public boolean hasName(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<String, DataObject> e : this.map.entrySet()) {
            b.append(e.getKey());
            b.append(" => ");
            b.append(e.getValue());
            b.append("\n");
        }
        return b.toString();
    }

    @Override
    public Set<? extends Object> getKeys() {
        return this.map.keySet();
    }

    @Override
    public TypeTest getTypeTest() {
        return null;
    }

    @Override
    public void setTypeTest(TypeTest t, boolean b) {
    }
}

