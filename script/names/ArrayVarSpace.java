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
import script.names.AlwaysTrueTest;
import script.names.TypeTest;
import script.names.VarSpace;

public class ArrayVarSpace
implements VarSpace {
    protected HashMap<String, DataObject> map;
    private TypeTest t;

    public ArrayVarSpace() {
        this.map = new HashMap();
        this.t = AlwaysTrueTest.getTest();
    }

    public ArrayVarSpace(int size) {
        this.map = new HashMap(size);
        this.t = AlwaysTrueTest.getTest();
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
        if (!this.t.test(content)) {
            throw new InternalScriptError("Trying to put a value of type " + content.getType() + " into an array that is restricted to the following types: " + this.t);
        }
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

    private void testAll(boolean eraseNonMatching) throws InternalScriptError {
        for (Map.Entry<String, DataObject> e : this.map.entrySet()) {
            if (this.t.test(e.getValue())) continue;
            if (eraseNonMatching) {
                this.erase(e.getKey());
                continue;
            }
            throw new InternalScriptError(String.valueOf(e.getKey()) + " => " + e.getValue() + " (type: " + e.getValue().getType() + ")");
        }
    }

    @Override
    public void setTypeTest(TypeTest t, boolean eraseNonMatching) throws InternalScriptError {
        if (!this.t.isAncestorOf(t)) {
            this.t = t;
            this.testAll(eraseNonMatching);
        } else {
            this.t = t;
        }
    }
}

