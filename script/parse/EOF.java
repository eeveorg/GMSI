/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import script.Script;
import script.SourceObject;
import script.Token;

public class EOF
extends Token {
    public EOF(Token t) {
        super(t.getOwner(), "<EOF>", t.getLine(), t.getSourceObject());
    }
}

