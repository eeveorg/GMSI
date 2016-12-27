/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.LRhighTokens.TypeGenerator;
import script.LRhighTokens.TypeName;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.ParseError;
import script.Token;

public class TypeCast
extends Token {
    public TypeName typeName;

    public TypeCast(OpenBracket o, TypeName n, CloseBracket c) throws ParseError {
        super(o, "(" + n + ")");
        this.typeName = n;
        if (n.getCode().equals("void")) {
            throw new ParseError("Cannot type cast to " + n.getCode());
        }
    }

    public TypeCast(OpenBracket o, TypeGenerator n, CloseBracket c) throws ParseError {
        super(o, "(" + n + ")");
        this.typeName = n;
    }
}

