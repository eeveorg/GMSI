/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.File;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.ObjObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.dataObjects.VoidObject;
import script.names.NameResolver;
import script.names.VarHandle;
import script.systemCalls.TrapHandler;

public abstract class Trap {
    protected Script owner;
    protected static VoidObject voidResult = VoidObject.getVoidObject();
    protected static NullObject nullResult = new NullObject();

    public Trap(Script s) {
        this.owner = s;
    }

    public Trap(Script s, String callName) {
        this.owner = s;
        s.getTrapHandler().addTrap(this, callName);
    }

    public Trap(Script s, TrapHandler t, String callName) {
        this.owner = s;
        t.addTrap(this, callName);
    }

    public abstract DataObject apply() throws InternalScriptError;

    public void reset() {
    }

    public void finish() {
    }

    private void nullError(int varNum) throws InternalScriptError {
        throw new InternalScriptError("Parameter failure when invoking system call <" + this.getClass().getSimpleName().substring(5, 6).toLowerCase() + this.getClass().getSimpleName().substring(6) + ">:\n" + "The " + varNum + ". parameter is null!");
    }

    protected File strToFile(String s) throws InternalScriptError {
        if (s.startsWith("..")) {
            String parent = this.owner.getCurrentPath().getAbsoluteFile().getParent();
            if (parent == null) {
                throw new InternalScriptError("Trying to get the parent directory (..) of a directory that has no parent.\nErroneous file name:\n" + s);
            }
            return new File(String.valueOf(parent) + s.substring(2));
        }
        if (s.startsWith(".")) {
            return new File(String.valueOf(this.owner.getCurrentPath().getAbsolutePath()) + s.substring(1));
        }
        return new File(s);
    }

    protected ArrayObject getArrayParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return null;
        }
        if (!(result instanceof ArrayObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not an array!");
        }
        return (ArrayObject)result;
    }

    protected StructObject getStructParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return null;
        }
        if (!(result instanceof StructObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not an array!");
        }
        return (StructObject)result;
    }

    protected int getIntParam(int varName) throws InternalScriptError {
        return this.getIntParam(varName, true);
    }

    protected int getIntParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return 0;
        }
        if (!(result instanceof IntObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not an int!");
        }
        return ((IntObject)result).getValue();
    }

    protected String getStrParam(int varName, String defaultValue) throws InternalScriptError {
        String r = this.getStrParam(varName, false);
        if (r == null) {
            return defaultValue;
        }
        return r;
    }

    protected String getStrParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return null;
        }
        if (!(result instanceof StringObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not a string!");
        }
        return ((StringObject)result).getValue();
    }

    protected boolean getBoolParam(int varName) throws InternalScriptError {
        return this.getBoolParam(varName, true);
    }

    protected boolean getBoolParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return false;
        }
        if (!(result instanceof BoolObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not a boolean!");
        }
        return ((BoolObject)result).getValue();
    }

    protected float getFloatParam(int varName) throws InternalScriptError {
        return this.getFloatParam(varName, true);
    }

    protected float getFloatParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return 0.0f;
        }
        if (!(result instanceof FloatObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not a boolean!");
        }
        return ((FloatObject)result).getValue();
    }

    protected Object getObjParam(int varName, boolean notNull) throws InternalScriptError {
        DataObject result;
        try {
            result = this.owner.getNameResolver().resolveLocal(varName).getValue();
        }
        catch (InternalScriptError e) {
            throw new InternalScriptError("Trap error: Couldn't get the trap parameter " + varName);
        }
        if (result instanceof NullObject) {
            if (notNull) {
                this.nullError(varName);
            }
            return null;
        }
        if (!(result instanceof ObjObject)) {
            throw new InternalScriptError("Trap error: Parameter " + varName + " is not a boolean!");
        }
        return ((ObjObject)result).getValue();
    }
}

