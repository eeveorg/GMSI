/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.InternalScriptError;
import script.LRhighTokens.TypeName;
import script.LRterminals.DereferOperator;
import script.ParseError;
import script.Token;
import script.dataObjects.DataType;

public class ComplexTypeName
extends TypeName {
    private TypeName r;

    public ComplexTypeName(TypeName r, DereferOperator t) {
        super(r, r + t);
        this.r = r;
    }

    @Override
    public DataType getType() throws InternalScriptError {
        return DataType.getReferenceType(this.r.getType());
    }

    @Override
    public Token unwrap() throws ParseError {
        this.r = (TypeName)this.r.unwrap();
        return this;
    }
}

