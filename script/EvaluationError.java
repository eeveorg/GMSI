/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.Iterator;
import java.util.LinkedList;
import script.InternalScriptError;
import script.LRfinalTokens.FunctionCall;
import script.SourceObject;
import script.Token;

public class EvaluationError
extends InternalScriptError {
    private static final long serialVersionUID = 1;
    private Token firedBy;
    private LinkedList<FunctionCall> calls = new LinkedList();

    public void addCall(FunctionCall t) {
        this.calls.add(t);
    }

    public EvaluationError(Token t, String s) {
        super(s);
        this.firedBy = t;
    }

    public Token getFiredBy() {
        return this.firedBy;
    }

    public String getFullMessage() {
        String result = "Evaluation Error at line " + this.firedBy.line + " in file " + this.firedBy.source + ":\nCause: " + this.getMessage() + "\nAt code: " + this.firedBy.getCode();
        if (!this.calls.isEmpty()) {
            result = String.valueOf(result) + "\nCall stack:";
            for (Token t : this.calls) {
                result = String.valueOf(result) + "\ncalled by line: " + t.getLine() + ", file: " + t.getSourceObject() + ", code: " + t.getCode();
            }
        }
        return result;
    }

    public String getCallStack() {
        String result = "";
        if (!this.calls.isEmpty()) {
            result = String.valueOf(result) + "\nCall stack:";
            Iterator<FunctionCall> iterator = this.calls.iterator();
            while (iterator.hasNext()) {
                FunctionCall t;
                result = String.valueOf(result) + "\n" + ((t = iterator.next()).getFunctionName().equals("include") ? "included" : "called") + " by line: " + t.getLine() + ", file: " + t.getSourceObject() + ", code: " + t.getCode();
            }
        }
        return result;
    }

    public int getLine() {
        return this.firedBy.line;
    }

    public String getSourceFile() {
        return this.firedBy.getSourceObject().toString();
    }

    public String getCode() {
        return this.firedBy.getCode();
    }
}

