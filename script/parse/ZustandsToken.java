/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import script.Script;
import script.Token;

public class ZustandsToken
extends Token {
    public int zustand;

    public ZustandsToken(Script ss, int zustand) {
        super(ss, "<STATE " + zustand + ">");
        this.zustand = zustand;
    }
}

