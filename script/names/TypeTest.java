/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import script.InternalScriptError;
import script.dataObjects.DataObject;

public interface TypeTest {
    public boolean test(DataObject var1) throws InternalScriptError;

    public boolean isAncestorOf(TypeTest var1);
}

