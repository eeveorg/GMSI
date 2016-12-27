/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.InternalScriptError;
import script.LRhighTokens.TypeName;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataType;
import script.names.NameResolver;

public class SimpleTypeName
extends TypeName {
    public SimpleTypeName(Token old) {
        super(old);
    }

    @Override
    public DataType getType() throws InternalScriptError {
        return this.owner.getNameResolver().stringToTypeName(this.code);
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }
}

