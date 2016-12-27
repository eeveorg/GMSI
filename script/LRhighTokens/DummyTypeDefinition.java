/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.List;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.SourceObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class DummyTypeDefinition
implements TypeDefinition {
    private String name;

    @Override
    public DataObject getDefaultInstance() throws InternalScriptError {
        throw new InternalScriptError("The type " + this.name + " was declared extern, but no external definition was found!");
    }

    @Override
    public String getName() {
        return this.name;
    }

    public DummyTypeDefinition(String n) {
        this.name = n;
    }

    @Override
    public DataObject getNewInstance() throws InternalScriptError {
        throw new InternalScriptError("The type " + this.name + " was declared extern, but no external definition was found!");
    }

    @Override
    public List<DataType> getParentTypes() {
        throw new Error("The type " + this.name + " was declared extern, but no external definition was found!");
    }

    public DataType getType() {
        throw new Error("The type " + this.name + " was declared extern, but no external definition was found!");
    }

    @Override
    public boolean isDerivedFrom(DataType d) {
        throw new Error("The type " + this.name + " was declared extern, but no external definition was found!");
    }

    @Override
    public int getLine() {
        return 0;
    }

    @Override
    public SourceObject getSource() {
        return null;
    }
}

