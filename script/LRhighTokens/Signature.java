/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.Collection;
import java.util.LinkedList;
import script.LRhighTokens.VariableDef;
import script.LRterminals.Comma;
import script.Token;

public class Signature
extends Token {
    private LinkedList<VariableDef> defs = new LinkedList();

    public Signature(VariableDef d) {
        super(d, (String)((Object)d));
        this.defs.add(d);
    }

    public Signature(Signature s, Comma c, VariableDef d) {
        super(s, s + "," + d);
        this.defs.addAll(s.defs);
        this.defs.add(d);
    }

    public LinkedList<VariableDef> getList() {
        return this.defs;
    }
}

