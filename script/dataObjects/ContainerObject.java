/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.names.VarHandle;
import script.names.VarSpace;

public abstract class ContainerObject
extends DataObject {
    public abstract VarSpace getVarSpace();

    public abstract VarHandle getEntry(String var1) throws InternalScriptError;
}

