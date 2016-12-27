/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.Collection;
import java.util.LinkedList;
import script.LRhighTokens.Expression;
import script.LRterminals.Comma;
import script.Token;

public class ParameterList
extends Token {
    private LinkedList<Expression> params = new LinkedList();

    public ParameterList(Expression e) {
        super(e, (String)((Object)e));
        this.params.add(e);
    }

    public ParameterList(ParameterList p, Comma c, Expression e) {
        super(p, p + "," + e);
        this.params.addAll(p.params);
        this.params.add(e);
    }

    public LinkedList<Expression> getList() {
        return this.params;
    }
}

