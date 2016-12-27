/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import program.misc.Log;
import program.misc.Misc;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.StructObject;
import script.names.NameResolver;
import script.names.VarHandle;
import script.names.VarSpace;
import script.systemCalls.Trap;
import wcData.objects.ObjectTableEntry;
import wcData.objects.WC3DefaultValues;

public class Trap_CreateObject
extends Trap {
    public Trap_CreateObject(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        DataObject o;
        ArrayObject output;
        StructObject map = this.getStructParam(0, true);
        String baseId = this.getStrParam(1, true);
        String newId = this.getStrParam(2, true);
        boolean overwrite = this.getBoolParam(3);
        ArrayObject objSpace = (ArrayObject)map.getVarSpace().get("objects");
        WC3DefaultValues defVals = WC3DefaultValues.getDefaultValues();
        if (!defVals.hasObject(baseId)) {
            throw new InternalScriptError("Cannot create object because the baseId '" + baseId + "' is no valid base object id");
        }
        if (defVals.hasObject(newId)) {
            throw new InternalScriptError("Cannot create object because the desired id '" + newId + "' is the id of a base object which cannot be overwritten!");
        }
        if (!overwrite && !(o = objSpace.getEntry(newId).getValue()).isNull()) {
            throw new InternalScriptError("Cannot create object because the desired id '" + newId + "' is already in use and overwriting was disabled!");
        }
        if (!Misc.isIdValid(newId)) {
            throw new InternalScriptError("Cannot create object because the desired id '" + newId + "' is not a valid WC3 object id!");
        }
        if (!Misc.isIdCouraged(newId) && Log.doLog(3, 2)) {
            Log.println("Object creation warning: The specified id '" + newId + "' is not an id that the editor would give to an object! However it is valid, so the editor (probably) won't crash when you use it.");
        }
        ObjectTableEntry father = (ObjectTableEntry)defVals.getObject(baseId).getVarSpace();
        ObjectTableEntry result = new ObjectTableEntry(father.getCategory(), father.getId(), newId, 16);
        DataType type = new DataType(this.owner.getNameResolver().getUserDef(Misc.getTypeFromCategory(result.getCategory())));
        try {
            output = (ArrayObject)type.getNewInstance();
        }
        catch (EvaluationError e1) {
            throw new Error("Error:", e1);
        }
        catch (InternalScriptError e1) {
            throw new Error("Error:", e1);
        }
        output.setStringMap(result);
        objSpace.getVarSpace().put(newId, output);
        return output;
    }
}

