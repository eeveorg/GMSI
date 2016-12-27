/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.names.TypeTest;

public interface VarSpace {
    public boolean erase(Object var1);

    public void put(Object var1, DataObject var2) throws InternalScriptError;

    public boolean hasName(Object var1);

    public DataObject get(Object var1) throws InternalScriptError;

    public String toString();

    public Set<? extends Object> getKeys();

    public void setTypeTest(TypeTest var1, boolean var2) throws InternalScriptError;

    public TypeTest getTypeTest();
}

