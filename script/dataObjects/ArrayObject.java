/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Tools;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.LastArrayIndex;
import script.names.AlwaysTrueTest;
import script.names.ArrayVarSpace;
import script.names.IntVarSpace;
import script.names.TypeListTest;
import script.names.TypeTest;
import script.names.VarHandle;
import script.names.VarSpace;

public class ArrayObject
extends ContainerObject {
    protected VarSpace stringMap;
    protected IntVarSpace intMap;

    @Override
    public VarSpace getVarSpace() {
        return this.stringMap;
    }

    public ArrayObject() {
        this.type = DataType.ARRAY;
        this.stringMap = new ArrayVarSpace();
        this.intMap = new IntVarSpace();
    }

    public ArrayObject(VarSpace s) {
        this.type = DataType.ARRAY;
        this.stringMap = s;
        this.intMap = new IntVarSpace();
    }

    public VarHandle getHighEntry() {
        return new VarHandle(this.intMap, new LastArrayIndex());
    }

    @Override
    public VarHandle getEntry(String s) {
        return new VarHandle(this.stringMap, s);
    }

    public VarHandle getEntry(Integer i) {
        return new VarHandle(this.intMap, i);
    }

    protected ArrayObject(ArrayObject a, DataType d) {
        this.type = d;
        this.stringMap = a.stringMap;
        this.intMap = a.intMap;
    }

    public Set<? extends Object> getIntKeys() {
        return this.intMap.getKeys();
    }

    public Set<? extends Object> getStrKeys() {
        return this.stringMap.getKeys();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.intMap.toString());
        b.append(this.stringMap.toString());
        if (b.toString().equals("")) {
            return "array()";
        }
        StringBuilder c = new StringBuilder();
        c.append("array(\n");
        c.append(Tools.addIndent(b.toString(), "  "));
        return String.valueOf(c.substring(0, c.length() - 2)) + ")";
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (this.getType().isDerivedFrom(d)) {
            if (this.getType().equals(d)) {
                return this;
            }
            if (d.getAllowedContent() == null) {
                this.intMap.setTypeTest(AlwaysTrueTest.getTest(), false);
                this.stringMap.setTypeTest(AlwaysTrueTest.getTest(), false);
                return this;
            }
            try {
                this.intMap.setTypeTest(new TypeListTest(d.getAllowedContent()), false);
                this.stringMap.setTypeTest(new TypeListTest(d.getAllowedContent()), false);
            }
            catch (InternalScriptError e) {
                throw new ImplicitCastError("Implicit cast error: cannot cast from " + this.getType() + " to " + d + " because at least one entry contains data" + " that doesn't fit into the new restrictions." + "\nHere is an example of a key value pair that doesn't fit:\n" + e.getMessage());
            }
            return this;
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (this.getType().isDerivedFrom(d)) {
            if (this.getType().equals(d)) {
                return this;
            }
            if (d.getAllowedContent() == null) {
                this.intMap.setTypeTest(AlwaysTrueTest.getTest(), false);
                this.stringMap.setTypeTest(AlwaysTrueTest.getTest(), false);
                this.type = d;
                return this;
            }
            this.intMap.setTypeTest(new TypeListTest(d.getAllowedContent()), true);
            this.stringMap.setTypeTest(new TypeListTest(d.getAllowedContent()), true);
            this.type = d;
            return this;
        }
        if (d.getBasicType() == DataType.types.USER) {
            DataObject result = this.explicitCastTo(d.getDefinition().getParentTypes().get(0));
            result.type = d;
            return result;
        }
        throw new InternalScriptError("Explicit cast error: Cannot cast from " + this.type + " to " + d);
    }

    public void setStringMap(VarSpace stringMap) {
        this.stringMap = stringMap;
    }

    public IntVarSpace getIntSpace() {
        return this.intMap;
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof ArrayObject)) {
            throw new InternalScriptError("Cannot compare " + this.getType() + " with " + o.getType());
        }
        switch (operator) {
            case 0: {
                return o.equals(this);
            }
            case 1: {
                return !o.equals(this);
            }
        }
        throw new InternalScriptError("Evaluation Error: Cannot compare arrays with " + operator);
    }
}

