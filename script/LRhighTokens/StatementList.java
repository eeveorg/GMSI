/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Statement;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;

public class StatementList
extends Token {
    LinkedList<Statement> statements = new LinkedList();
    private EvaluationResult skipListener;

    public LinkedList<Statement> getList() {
        return this.statements;
    }

    public StatementList(Statement s) {
        super(s, (String)((Object)s));
        this.skipListener = this.owner.getEvalResult();
        this.statements.add(s);
    }

    public StatementList(StatementList l, Statement s) {
        super(l, l + "," + s);
        this.skipListener = l.skipListener;
        this.statements.addAll(l.statements);
        this.statements.add(s);
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<Statement> iter = this.statements.listIterator();
        while (iter.hasNext()) {
            Token t = iter.next();
            iter.set((Statement)t.unwrap());
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        EvaluationResult skip = this.skipListener;
        for (Statement s : this.statements) {
            s.eval();
            if (skip.skip()) break;
        }
        return null;
    }
}

