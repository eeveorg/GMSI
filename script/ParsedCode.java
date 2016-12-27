/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.LinkedList;
import script.EvaluationError;
import script.ParseError;
import script.ScannedCode;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.names.LocalNameMapper;
import script.names.NameResolver;
import script.parse.Parser;

public class ParsedCode {
    protected Token codeTree;
    private int numLocalVars = -1;

    public ParsedCode(Parser p, ScannedCode input) throws ParseError {
        Token result;
        try {
            result = p.parse(input.tokens);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ParseError(e);
        }
        LocalNameMapper.pushMapper();
        this.codeTree = result.unwrap();
        this.numLocalVars = LocalNameMapper.popMapper();
    }

    public void eval() throws EvaluationError {
        try {
            this.codeTree.owner.getNameResolver().pushLocalNew(this.numLocalVars);
            this.codeTree.eval();
            this.codeTree.owner.getNameResolver().popBig();
        }
        finally {
            this.codeTree.owner.finished();
        }
    }

    public Token getParsedToken() {
        return this.codeTree;
    }
}

