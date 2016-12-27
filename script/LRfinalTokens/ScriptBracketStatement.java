/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.EvaluationResult;
import script.LRhighTokens.Statement;
import script.LRhighTokens.StatementList;
import script.LRterminals.CloseScriptBracket;
import script.LRterminals.OpenScriptBracket;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;

public class ScriptBracketStatement
extends Statement {
    LinkedList<Statement> statements = new LinkedList();
    private boolean isEmpty;
    private EvaluationResult skip;

    public LinkedList<Statement> getList() {
        return this.statements;
    }

    public ScriptBracketStatement(OpenScriptBracket o, StatementList l, CloseScriptBracket b) {
        super(o, o + l + b);
        this.statements.addAll(l.getList());
        this.isEmpty = false;
        this.skip = this.owner.getEvalResult();
    }

    public ScriptBracketStatement(OpenScriptBracket o, CloseScriptBracket b) {
        super(o, o + b);
        this.isEmpty = true;
    }

    @Override
    public Token unwrap() throws ParseError {
        LocalNameMapper.getMapper().pushSpace();
        ListIterator<Statement> iter = this.statements.listIterator();
        while (iter.hasNext()) {
            Statement s = iter.next();
            iter.set((Statement)s.unwrap());
        }
        LocalNameMapper.getMapper().popSpace();
        return this;
    }

    public String getSubtokenString() {
        String result = "|";
        for (Statement t : this.statements) {
            result = String.valueOf(result) + t.toString() + "|";
        }
        return result;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        if (this.isEmpty) {
            return voidResult;
        }
        for (Statement t : this.statements) {
            t.eval();
            if (this.skip.skip()) break;
        }
        return voidResult;
    }
}

