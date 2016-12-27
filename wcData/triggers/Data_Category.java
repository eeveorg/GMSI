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

public class Data_Category {
    private int index;
    private String name;
    private boolean isComment;

    public void fromFile(BlizzardDataInputStream b) throws IOException {
        this.index = b.readInt();
        this.name = b.readString();
        this.isComment = b.readInt() == 1;
    }

    public void toFile(BlizzardDataOutputStream b) throws IOException {
        b.writeInt(this.index);
        b.writeString(this.name);
        b.writeBool(this.isComment);
    }

    public void fromObject(DataObject o) throws InternalScriptError {
        VarSpace v = ((StructObject)o).getVarSpace();
        this.index = ((IntObject)v.get("index")).getValue();
        this.name = ((StringObject)v.get("name")).getValue();
        this.isComment = ((BoolObject)v.get("isComment")).getValue();
    }

    public DataObject toObject() throws EvaluationError, InternalScriptError {
        StructObject s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("TriggerCategory")).getNewInstance();
        VarSpace v = s.getVarSpace();
        v.put("index", new IntObject(this.index));
        v.put("name", new StringObject(this.name));
        v.put("isComment", BoolObject.getBool(this.isComment));
        return s;
    }
}

