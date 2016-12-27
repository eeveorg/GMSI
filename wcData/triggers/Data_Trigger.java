/*
 * Decompiled with CFR 0_119.
 */
package wcData.triggers;

import java.io.IOException;
import java.util.LinkedList;
import program.Program;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.triggers.GuiParams;

public class Data_Trigger {
    public String name;
    public String description;
    public boolean isComment;
    public boolean isEnabled;
    public boolean isCustomText;
    public boolean isNotInitiallyOn;
    public boolean isRanOnMapInit;
    public int categoryIndex;
    public EAC_Handle eacHandle = null;
    public String code = "";
    public static VarSpace currentGuiStringSpace = null;
    public static int currentGuiStringIndex = 0;

    private Param_Struct parseParam(BlizzardDataInputStream r) throws IOException {
        Param_Struct result = new Param_Struct();
        result.type = r.readInt();
        result.value = r.readString();
        try {
            currentGuiStringSpace.put(currentGuiStringIndex++, new StringObject(result.value));
        }
        catch (InternalScriptError e) {
            throw new Error(e);
        }
        result.beginFunction = r.readInt();
        if (result.beginFunction == 1) {
            result.strangeType = r.readInt();
            result.funcName = r.readString();
            int numParams = GuiParams.getParams().num(result.funcName);
            result.secondBeginFunction = r.readInt();
            int k = 0;
            while (k < numParams) {
                result.params.add(this.parseParam(r));
                ++k;
            }
            result.endFunction = r.readInt();
        }
        result.arrayStart = r.readInt();
        if (result.arrayStart == 1) {
            result.arrayParam = this.parseParam(r);
        }
        return result;
    }

    private ECA_Struct parseECA(BlizzardDataInputStream r) throws IOException {
        ECA_Struct result = new ECA_Struct();
        result.type = r.readInt();
        result.name = r.readString();
        result.enabled = r.readInt() == 1;
        int numParams = GuiParams.getParams().num(result.name);
        int k = 0;
        while (k < numParams) {
            result.params.add(this.parseParam(r));
            ++k;
        }
        result.numEACs = r.readInt();
        if (result.numEACs != 0) {
            result.unknownInt = r.readInt();
        }
        k = 0;
        while (k < result.numEACs) {
            result.ecas.add(this.parseECA(r));
            if (k < result.numEACs - 1) {
                result.ecas.getLast().spacing = r.readInt();
            }
            ++k;
        }
        return result;
    }

    public String toString() {
        String result = "";
        result = String.valueOf(result) + "->Trigger: " + this.name;
        result = String.valueOf(result) + "\n   Description: " + this.description;
        result = String.valueOf(result) + "\n   Comment?: " + this.isComment;
        result = String.valueOf(result) + "\n   Enabled?: " + this.isEnabled;
        result = String.valueOf(result) + "\n   CustomText?: " + this.isCustomText;
        result = String.valueOf(result) + "\n   Initially on?: " + !this.isNotInitiallyOn;
        result = String.valueOf(result) + "\n   Run on map init?: " + this.isRanOnMapInit;
        result = String.valueOf(result) + "\n   Category: " + this.categoryIndex;
        result = String.valueOf(result) + "\n   Functions: " + this.eacHandle.numEACs;
        return result;
    }

    public void fromFiles(BlizzardDataInputStream wtg, BlizzardDataInputStream wct) throws IOException {
        int size;
        this.name = wtg.readString();
        this.description = wtg.readString();
        this.isComment = wtg.readInt() == 1;
        this.isEnabled = wtg.readInt() == 1;
        this.isCustomText = wtg.readInt() == 1;
        this.isNotInitiallyOn = wtg.readInt() == 1;
        this.isRanOnMapInit = wtg.readInt() == 1;
        this.categoryIndex = wtg.readInt();
        int numEACs = wtg.readInt();
        if (numEACs > 0) {
            this.eacHandle = new EAC_Handle();
            this.eacHandle.numEACs = numEACs;
            int j = 0;
            while (j < this.eacHandle.numEACs) {
                this.eacHandle.ecas.add(this.parseECA(wtg));
                ++j;
            }
        }
        if ((size = wct.readInt()) > 0) {
            this.code = wct.readString();
        }
    }

    public void toFiles(BlizzardDataOutputStream wtg, BlizzardDataOutputStream wct) throws IOException {
        wtg.writeString(this.name);
        wtg.writeString(this.description);
        wtg.writeBool(this.isComment);
        wtg.writeBool(this.isEnabled);
        wtg.writeBool(this.isCustomText);
        wtg.writeBool(this.isNotInitiallyOn);
        wtg.writeBool(this.isRanOnMapInit);
        wtg.writeInt(this.categoryIndex);
        if (this.eacHandle != null) {
            wtg.writeInt(this.eacHandle.numEACs);
            for (ECA_Struct e : this.eacHandle.ecas) {
                e.toFile(wtg);
            }
        } else {
            wtg.writeInt(0);
        }
        wct.writeInt(this.code.length() > 0 ? this.code.length() + 1 : 0);
        if (this.code.length() > 0) {
            wct.writeString(this.code);
        }
    }

    public void fromObject(DataObject o) throws InternalScriptError {
        VarSpace v = ((StructObject)o).getVarSpace();
        this.name = ((StringObject)v.get("name")).getValue();
        this.description = ((StringObject)v.get("comment")).getValue();
        this.isComment = ((BoolObject)v.get("isComment")).getValue();
        this.isEnabled = ((BoolObject)v.get("isEnabled")).getValue();
        this.isCustomText = ((BoolObject)v.get("isCustomText")).getValue();
        this.isNotInitiallyOn = ((BoolObject)v.get("isNotInitiallyOn")).getValue();
        this.isRanOnMapInit = ((BoolObject)v.get("isRanOnMapInit")).getValue();
        this.categoryIndex = ((IntObject)v.get("category")).getValue();
        this.code = ((StringObject)v.get("code")).getValue();
        DataObject gui = v.get("guiData");
        if (gui != null && !(gui instanceof NullObject)) {
            this.eacHandle = (EAC_Handle)((ObjObject)gui).getValue();
        }
    }

    public DataObject toObject() throws EvaluationError, InternalScriptError {
        StructObject s = (StructObject)new DataType(Program.getScript().getNameResolver().getUserDef("Trigger")).getNewInstance();
        VarSpace v = s.getVarSpace();
        v.put("name", new StringObject(this.name));
        v.put("comment", new StringObject(this.description));
        v.put("isComment", BoolObject.getBool(this.isComment));
        v.put("isEnabled", BoolObject.getBool(this.isEnabled));
        v.put("isCustomText", BoolObject.getBool(this.isCustomText));
        v.put("isNotInitiallyOn", BoolObject.getBool(this.isNotInitiallyOn));
        v.put("isRanOnMapInit", BoolObject.getBool(this.isRanOnMapInit));
        v.put("category", new IntObject(this.categoryIndex));
        v.put("code", new StringObject(this.code));
        if (this.eacHandle != null) {
            v.put("guiData", new ObjObject(this.eacHandle));
        }
        return s;
    }

    public static class EAC_Handle {
        public int numEACs = 0;
        public LinkedList<ECA_Struct> ecas = new LinkedList();

        public String toString() {
            return "<EAC Handle (" + this.ecas.size() + " eacs)>";
        }
    }

    public static class ECA_Struct {
        public int type;
        public String name;
        public boolean enabled;
        public LinkedList<Param_Struct> params = new LinkedList();
        public int unknownInt;
        public int numEACs;
        public int spacing = -1;
        public LinkedList<ECA_Struct> ecas = new LinkedList();

        private void toFile(BlizzardDataOutputStream b) throws IOException {
            b.writeInt(this.type);
            b.writeString(this.name);
            b.writeBool(this.enabled);
            for (Param_Struct p : this.params) {
                p.toFile(b);
            }
            b.writeInt(this.numEACs);
            if (this.numEACs != 0) {
                b.writeInt(this.unknownInt);
            }
            for (ECA_Struct s : this.ecas) {
                s.toFile(b);
                if (this.ecas.getLast() == s) continue;
                b.writeInt(s.spacing);
            }
        }
    }

    public static class Param_Struct {
        public int type;
        public String value;
        public LinkedList<Param_Struct> params = new LinkedList();
        public int beginFunction;
        public int secondBeginFunction;
        public int endFunction;
        public int strangeType = -1;
        public String funcName;
        public int arrayStart;
        public Param_Struct arrayParam = null;

        private void toFile(BlizzardDataOutputStream b) throws IOException {
            b.writeInt(this.type);
            b.writeString(this.value);
            b.writeInt(this.beginFunction);
            if (this.beginFunction == 1) {
                b.writeInt(this.strangeType);
                b.writeString(this.funcName);
                b.writeInt(this.secondBeginFunction);
                for (Param_Struct p : this.params) {
                    p.toFile(b);
                }
                b.writeInt(this.endFunction);
            }
            b.writeInt(this.arrayStart);
            if (this.arrayStart == 1) {
                this.arrayParam.toFile(b);
            }
        }
    }

}

