/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import java.util.LinkedList;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.DummyTypeDefinition;
import script.LRhighTokens.TypeDefinition;
import script.Tools;
import script.dataObjects.ArrayObject;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;

public class DataType {
    private types type;
    private DataType referencedType = null;
    private LinkedList<DataType> allowedContent = null;
    private TypeDefinition definition = null;
    public static DataType FLOAT = new DataType(types.FLOAT);
    public static DataType INT = new DataType(types.INT);
    public static DataType BOOL = new DataType(types.BOOL);
    public static DataType ARRAY = new DataType(types.ARRAY);
    public static DataType STRING = new DataType(types.STRING);
    private static /* synthetic */ int[] $SWITCH_TABLE$script$dataObjects$DataType$types;

    public LinkedList<DataType> getAllowedContent() {
        return this.allowedContent;
    }

    public DataType(LinkedList<DataType> cont) {
        this.allowedContent = cont;
        this.type = types.ARRAY;
    }

    public DataType(types t) {
        if (t == types.USER) {
            throw new Error("WTF---");
        }
        this.type = t;
    }

    public DataType(TypeDefinition t) throws InternalScriptError {
        if (t instanceof DummyTypeDefinition) {
            throw new InternalScriptError("The type " + t.getName() + " was declared extern, but no external definition was found!");
        }
        if (t == null) {
            throw new Error("Unable to create datatype from NULL TypdefStatement");
        }
        this.type = types.USER;
        this.definition = t;
    }

    public DataType(StructDefinition t) {
        if (t == null) {
            throw new Error("Unable to create datatype from NULL TypdefStatement");
        }
        this.type = types.USER;
        this.definition = t;
    }

    public static DataType getReferenceType(DataType t) {
        DataType result = new DataType(types.PTR);
        result.referencedType = t;
        return result;
    }

    public boolean isValidAsParameter() {
        if (this.type == types.VAR || this.type == types.VOID) {
            return false;
        }
        return true;
    }

    public String toString() {
        switch (DataType.$SWITCH_TABLE$script$dataObjects$DataType$types()[this.type.ordinal()]) {
            case 1: {
                return "bool";
            }
            case 2: {
                return "int";
            }
            case 3: {
                return "string";
            }
            case 4: {
                return "var";
            }
            case 5: {
                return "array" + (this.allowedContent != null ? Tools.listToString(this.allowedContent, "<", ">") : "");
            }
            case 6: {
                return "float";
            }
            case 7: {
                return "void";
            }
            case 9: {
                return "object";
            }
            case 8: {
                return this.referencedType + "*";
            }
            case 11: {
                return "none";
            }
            case 10: {
                return this.definition.getName();
            }
        }
        return "WTF11";
    }

    public DataObject getNewInstance() throws InternalScriptError, EvaluationError {
        switch (DataType.$SWITCH_TABLE$script$dataObjects$DataType$types()[this.type.ordinal()]) {
            case 1: {
                return BoolObject.FALSE;
            }
            case 2: {
                return new IntObject(0);
            }
            case 3: {
                return new StringObject("");
            }
            case 4: {
                throw new InternalScriptError("Cannot create VAR objects!");
            }
            case 5: {
                return new ArrayObject();
            }
            case 6: {
                return new FloatObject(0.0f);
            }
            case 7: {
                throw new InternalScriptError("cannot instanciate variables of type void");
            }
            case 8: {
                throw new InternalScriptError("cannot instanciate pointer variables with a constructor");
            }
            case 10: {
                return this.definition.getNewInstance();
            }
        }
        throw new InternalScriptError("cannot instanciate variables of type " + this);
    }

    public DataObject getDefaultInstance() throws InternalScriptError {
        switch (DataType.$SWITCH_TABLE$script$dataObjects$DataType$types()[this.type.ordinal()]) {
            case 1: {
                return BoolObject.FALSE;
            }
            case 2: {
                return new IntObject(0);
            }
            case 3: {
                return new StringObject("");
            }
            case 4: {
                return new NullObject(new DataType(types.VAR));
            }
            case 5: {
                return new NullObject(new DataType(types.ARRAY));
            }
            case 6: {
                return new FloatObject(0.0f);
            }
            case 7: {
                throw new InternalScriptError("cannot instanciate variables of type void");
            }
            case 8: {
                return new NullObject(this.referencedType);
            }
            case 10: {
                return this.definition.getDefaultInstance();
            }
        }
        throw new InternalScriptError("cannot instanciate variables of type " + this);
    }

    public boolean equals(Object o) {
        if (!(o instanceof DataType)) {
            return false;
        }
        DataType t = (DataType)o;
        if (t.type != this.type) {
            return false;
        }
        if (t.type == types.PTR) {
            return this.referencedType.equals(t.referencedType);
        }
        if (t.type == types.USER) {
            if (t.definition == this.definition) {
                return true;
            }
            return false;
        }
        if (t.type == types.ARRAY) {
            if (t.allowedContent == null) {
                if (this.allowedContent == null) {
                    return true;
                }
                return false;
            }
            if (this.allowedContent == null) {
                return false;
            }
            return t.allowedContent.equals(this.allowedContent);
        }
        return true;
    }

    public DataObject getNewInstance(DataObject initValue) throws InternalScriptError {
        return initValue.implicitCastTo(this);
    }

    public types getBasicType() {
        return this.type;
    }

    public DataType getReferencedType() {
        return this.referencedType;
    }

    public boolean isVoidPointer() {
        if (this.type == types.PTR) {
            return this.referencedType.containsVoid();
        }
        return false;
    }

    private boolean containsVoid() {
        if (this.type == types.PTR) {
            return this.referencedType.containsVoid();
        }
        if (this.type == types.VOID) {
            return true;
        }
        return false;
    }

    public boolean isDerivedFrom(DataType t) throws InternalScriptError {
        if (t.equals(this)) {
            return true;
        }
        if (this.getBasicType() == types.USER) {
            return this.definition.isDerivedFrom(t);
        }
        if (this.getBasicType() == types.ARRAY && t.getBasicType() == types.ARRAY) {
            return true;
        }
        return false;
    }

    public TypeDefinition getDefinition() {
        return this.definition;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$script$dataObjects$DataType$types() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$script$dataObjects$DataType$types;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[types.values().length];
        try {
            arrn[types.ARRAY.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.BOOL.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.FLOAT.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.INT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.NONE.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.OBJECT.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.PTR.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.STRING.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.USER.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.VAR.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[types.VOID.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$script$dataObjects$DataType$types = arrn;
        return $SWITCH_TABLE$script$dataObjects$DataType$types;
    }

    public static enum types {
        BOOL,
        INT,
        STRING,
        VAR,
        ARRAY,
        FLOAT,
        VOID,
        PTR,
        OBJECT,
        USER,
        NONE;
        

        private types(String string2, int n2) {
        }
    }

}

