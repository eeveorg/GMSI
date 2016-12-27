/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.LastArrayIndex;
import script.dataObjects.NullObject;
import script.names.AlwaysTrueTest;
import script.names.TypeTest;
import script.names.VarSpace;

public class IntVarSpace
implements VarSpace {
    protected TreeMap<Integer, DataObject> map = new TreeMap();
    private TypeTest t = AlwaysTrueTest.getTest();

    @Override
    public boolean erase(Object key) {
        if (key instanceof LastArrayIndex) {
            if (this.map.isEmpty()) {
                return false;
            }
            if (this.map.remove(this.map.lastKey()) != null) {
                return true;
            }
            return false;
        }
        if (this.map.remove(key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public DataObject get(Object key) throws InternalScriptError {
        if (key instanceof LastArrayIndex) {
            if (this.map.isEmpty()) {
                return new NullObject();
            }
            return this.map.get(this.map.lastKey());
        }
        return this.map.get(key);
    }

    @Override
    public void put(Object key, DataObject content) throws InternalScriptError {
        if (!this.t.test(content)) {
            throw new InternalScriptError("Trying to put a value of type " + content.getType() + " into an array that is restricted to the following types: " + this.t);
        }
        if (key instanceof LastArrayIndex) {
            try {
                this.map.put(this.map.lastKey() + 1, content);
            }
            catch (NoSuchElementException e) {
                this.map.put(0, content);
            }
            return;
        }
        this.map.put((Integer)key, content);
    }

    @Override
    public boolean hasName(Object key) {
        if (key instanceof LastArrayIndex) {
            return true;
        }
        return this.map.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<Integer, DataObject> e : this.map.entrySet()) {
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
        return this.t;
    }

    private void testAll(boolean eraseNonMatching) throws InternalScriptError {
        for (Map.Entry<Integer, DataObject> e : this.map.entrySet()) {
            if (this.t.test(e.getValue())) continue;
            if (eraseNonMatching) {
                this.erase(e.getKey());
                continue;
            }
            throw new InternalScriptError(e.getKey() + " => " + e.getValue() + " (type: " + e.getValue().getType() + ")");
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

