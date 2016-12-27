/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.TypeExpression;
import script.LRhighTokens.ComplexTypeName;
import script.LRhighTokens.GenericTypeName;
import script.LRterminals.SimpleTypeName;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class TypeName
extends Token {
    private TypeName child = null;

    public TypeName(Token t, String code) {
        super(t, code);
    }

    public TypeName(Token t) {
        super(t);
    }

    public TypeName(ComplexTypeName n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    public TypeName(GenericTypeName n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    public TypeName(SimpleTypeName n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    public TypeName(TypeExpression n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.child = (TypeName)this.child.unwrap();
        if (this.child != null) {
            return this.child;
        }
        return this;
    }

    public DataType getType() throws InternalScriptError {
        throw new Error("Cannot retrieve type of type name");
    }

    @Override
    public DataObject eval() throws EvaluationError {
        throw new EvaluationError(this, "Cannot evaluate type name!");
    }
}

