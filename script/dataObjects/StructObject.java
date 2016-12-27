/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import script.EvaluationError;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRfinalTokens.AssignDefStatement;
import script.LRfinalTokens.DefStatement;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeName;
import script.LRterminals.Identifyer;
import script.Tools;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.StructVarSpace;
import script.names.VarHandle;
import script.names.VarSpace;

public class StructObject
extends ContainerObject {
    private StructVarSpace map;
    private StructDefinition def;

    @Override
    public boolean isStruct() {
        return true;
    }

    @Override
    public VarSpace getVarSpace() {
        return this.map;
    }

    @Override
    public DataType getContentDataType() {
        return this.contentType;
    }

    @Override
    public VarHandle getEntry(String name) throws InternalScriptError {
        if (!this.map.hasName(name)) {
            throw new InternalScriptError("The name \"." + name + "\" is not a member of the struct-type " + this.def.getName());
        }
        return new VarHandle(this.map, name);
    }

    public Set<? extends Object> getKeys() {
        return this.map.getKeys();
    }

    public StructObject(StructDefinition def) throws InternalScriptError, EvaluationError {
        this.contentType = this.type = new DataType(def);
        this.map = new StructVarSpace(def.getTypeMap());
        this.def = def;
        for (Statement s : def.children) {
            DefStatement d;
            DataObject o;
            if (s instanceof DefStatement) {
                d = (DefStatement)s;
                o = d.getType().getDefaultInstance();
                this.map.assign(d.name.getCode(), o);
                continue;
            }
            d = (AssignDefStatement)s;
            o = d.rValue.eval().implicitCastTo(d.type.getType());
            this.map.assign(d.name.getCode(), o);
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.map.toString());
        StringBuilder c = new StringBuilder();
        c.append("struct " + this.def.getName() + "(\n");
        c.append(Tools.addIndent(b.toString(), "  "));
        return String.valueOf(c.substring(0, c.length() - 2)) + ")";
    }

    private StructObject(StructObject a, DataType d, DataType dNew) {
        this.type = dNew;
        this.contentType = d;
        this.map = a.map;
        this.def = a.def;
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (this.getContentDataType().isDerivedFrom(d)) {
            return new StructObject(this, this.getContentDataType(), d);
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    @Override
    public DataObject explicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (this.getType().isDerivedFrom(d)) {
            return new StructObject(this, this.getContentDataType(), d);
        }
        if (this.getContentDataType().isDerivedFrom(d)) {
            return new StructObject(this, this.getContentDataType(), d);
        }
        throw new InternalScriptError("Explicit cast error: Cannot cast explicitly from " + this.type + " to " + d);
    }

    public boolean equals(Object o) {
        if (!(o instanceof StructObject)) {
            return false;
        }
        StructObject s = (StructObject)o;
        return s.map.equals(this.map);
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof StructObject)) {
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
        throw new InternalScriptError("Evaluation Error: Cannot compare Structs with " + operator);
    }
}

