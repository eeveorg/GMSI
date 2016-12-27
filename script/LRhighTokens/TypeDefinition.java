/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.List;
import script.InternalScriptError;
import script.SourceObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public interface TypeDefinition {
    public DataObject getNewInstance() throws InternalScriptError;

    public DataObject getDefaultInstance() throws InternalScriptError;

    public String getName();

    public boolean isDerivedFrom(DataType var1) throws InternalScriptError;

    public List<DataType> getParentTypes() throws InternalScriptError;

    public int getLine();

    public SourceObject getSource();
}

