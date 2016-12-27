/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.LinkedList;
import java.util.ListIterator;
import script.LRhighTokens.Statement;
import script.LRhighTokens.StatementList;
import script.LRhighTokens.TypeName;
import script.LRterminals.START;
import script.ParseError;
import script.Token;

public class AcceptingStatementList
extends Token {
    private StatementList statements = null;
    private TypeName typeName = null;

    public LinkedList<Statement> getList() {
        return this.statements.getList();
    }

    public AcceptingStatementList(START s, StatementList l) {
        super(l, (String)((Object)l));
        this.statements = l;
    }

    public AcceptingStatementList(START s, TypeName t) {
        super(t, (String)((Object)t));
        this.typeName = t;
    }

    @Override
    public Token unwrap() throws ParseError {
        if (this.typeName != null) {
            return this.typeName.unwrap();
        }
        ListIterator<Statement> iter = this.statements.getList().listIterator();
        while (iter.hasNext()) {
            Token t = iter.next();
            iter.set((Statement)t.unwrap());
        }
        return this.statements;
    }
}

