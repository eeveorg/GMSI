/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.InternalScriptError;
import script.LRhighTokens.TypeName;
import script.LRterminals.Identifyer;
import script.ParseError;
import script.Token;
import script.dataObjects.DataType;

public class VariableDef
extends Token {
    public Identifyer name;
    public TypeName type;

    public VariableDef(TypeName type, Identifyer name) {
        super(type, type + " " + name);
        this.type = type;
        this.name = name;
    }

    public DataType getType() throws InternalScriptError {
        return this.type.getType();
    }

    @Override
    public Token unwrap() throws ParseError {
        this.type = (TypeName)this.type.unwrap();
        return this;
    }
}

