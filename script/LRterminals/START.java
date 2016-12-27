/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.Script;
import script.SourceObject;
import script.Token;

public class START
extends Token {
    public START(Token t) {
        super(t.getOwner(), "<START>", t.getLine(), t.getSourceObject());
    }
}

