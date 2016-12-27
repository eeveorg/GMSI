/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.LRfinalTokens.StrToTypeExpression;
import script.LRfinalTokens.TypeExpression;
import script.LRhighTokens.TypeName;
import script.ParseError;
import script.Token;

public class TypeGenerator
extends TypeName {
    private TypeGenerator child = null;

    public TypeGenerator(Token t, String code) {
        super(t, code);
    }

    public TypeGenerator(Token t) {
        super(t);
    }

    public TypeGenerator(TypeExpression n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    public TypeGenerator(StrToTypeExpression n) {
        super(n, (String)((Object)n));
        this.child = n;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.child = (TypeGenerator)this.child.unwrap();
        if (this.child != null) {
            return this.child;
        }
        return this;
    }
}

