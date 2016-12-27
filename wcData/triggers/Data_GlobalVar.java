/*
 * Decompiled with CFR 0_119.
 */
package wcData.triggers;

import java.io.IOException;
import program.Program;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;

public class Data_GlobalVar {
    private String name;
    private String type;
    private int unknown;
    private boolean isArray;
    private int arraySize;
    private boolean isInited;
    private String initValue;

    public String getName() {
        return this.name;
    }

    public String toString() {
        String result = "";
        result = String.valueOf(result) + "->Var: " + this.name;
        result = String.valueOf(result) + "\n   Type: " + this.type;
        result = String.valueOf(result) + "\n   unknown: " + this.unknown;
        result = String.valueOf(result) + "\n   isArray?: " + this.isArray;
        result = String.valueOf(result) + "\n   arraySize: " + this.arraySize;
        result = String.valueOf(result) + "\n   is Inited?: " + this.isInited;
        result = String.valueOf(result) + "\n   initialValue?: " + this.initValue;
        return result;
    }

    public void fromFile(BlizzardDataInputStream r) throws IOException {
        this.name = r.readString();
        this.type = r.readString();
        this.unknown = r.readInt();
        this.isArray = r.readInt() == 1;
        this.arraySize = r.readInt();
        this.isInited = r.readInt() == 1;
        this.initValue = r.readString();
    }

    public void toFile(BlizzardDataOutputStream b) throws IOException {
        b.writeString(this.name);
        b.writeString(this.type);
        b.writeInt(this.unknown);
        b.writeBool(this.isArray);
        b.writeInt(this.arraySize);
        b.writeBool(this.isInited);
        b.writeString(this.initValue);
    }

    public void fromObject(DataObject o) throws InternalScriptError {
        VarSpace v = ((StructObject)o).getVarSpace();
        this.name = ((StringObject)v.get("name")).getValue();
        this.type = ((StringObject)v.get("typ")).getValue();
        this.unknown = ((IntObject)v.get("unknown")).getValue();
        this.isArray = ((BoolObject)v.get("isArray")).getValue();
        this.arraySize = ((IntObject)v.get("arraySize")).getValue();
        this.isInited = ((BoolObject)v.get("isInited")).getValue();
        this.initValue = ((StringObject)v.get("initValue")).getValue();
    }

    public DataObject toObject() throws EvaluationError, InternalScriptError {
        StructObject s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("GlobalVariable")).getNewInstance();
        VarSpace v = s.getVarSpace();
        v.put("name", new StringObject(this.name));
        v.put("typ", new StringObject(this.type));
        v.put("unknown", new IntObject(this.unknown));
        v.put("isArray", BoolObject.getBool(this.isArray));
        v.put("arraySize", new IntObject(this.arraySize));
        v.put("isInited", BoolObject.getBool(this.isInited));
        v.put("initValue", new StringObject(this.initValue));
        return s;
    }
}

