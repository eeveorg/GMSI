/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.TypeTest;
import script.names.VarSpace;

public class StructVarSpace
implements VarSpace {
    private LinkedHashMap<String, DataObject> map;
    private HashMap<String, DataType> typeMap;

    public StructVarSpace(HashMap<String, DataType> typeMap) {
        this.map = new LinkedHashMap();
        this.typeMap = typeMap;
    }

    public StructVarSpace(int size, HashMap<String, DataType> typeMap) {
        this.map = new LinkedHashMap(size);
        this.typeMap = typeMap;
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
        if (!this.map.containsKey(key)) {
            throw new InternalScriptError(key + " is not a member");
        }
        this.map.put((String)key, content.implicitCastTo(this.typeMap.get(key)));
    }

    public void assign(Object key, DataObject content) throws InternalScriptError {
        this.map.put((String)key, content);
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

