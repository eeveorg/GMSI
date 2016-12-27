/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.Code;
import script.EvaluationError;
import script.InternalScriptError;
import script.ParseError;
import script.Script;
import script.ScriptAbortionError;
import script.SourceObject;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;

public class Token {
    protected String code;
    protected int line;
    protected Script owner;
    protected static VoidObject voidResult = VoidObject.getVoidObject();
    public SourceObject source;
    public String scriptReplacement;

    public Script getOwner() {
        return this.owner;
    }

    public Token(Token getLineFrom, String code) {
        this.code = code;
        this.line = getLineFrom.getLine();
        this.source = getLineFrom.getSourceObject();
        this.owner = getLineFrom.owner;
    }

    public Token(Token old) {
        this.code = old.code;
        this.line = old.line;
        this.source = old.getSourceObject();
        this.owner = old.owner;
    }

    public Token(Script own, String s) {
        this.code = s;
        this.owner = own;
        this.line = -1;
        this.source = null;
    }

    public Token(Script owner, String code, int line, SourceObject sourceObject) {
        this.code = code;
        this.line = line;
        this.owner = owner;
        this.source = sourceObject;
    }

    protected Token(String code) {
        this.code = code;
    }

    public Code genCode() {
        return null;
    }

    public int getLine() {
        return this.line;
    }

    public SourceObject getSourceObject() {
        return this.source;
    }

    public DataObject eval() throws EvaluationError {
        try {
            this.owner.checkEvalCount();
            if (this.owner.wantAbort) {
                throw new ScriptAbortionError(this, "\n--- Script aborted by user! ---");
            }
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return null;
    }

    public String toString() {
        return this.code;
    }

    public String getCode() {
        return this.code;
    }

    public Token unwrap() throws ParseError {
        return this;
    }
}

