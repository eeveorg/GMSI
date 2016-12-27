/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.LRfinalTokens.AssignDefStatement;
import script.LRfinalTokens.BreakStatement;
import script.LRfinalTokens.ContinueStatement;
import script.LRfinalTokens.DefStatement;
import script.LRfinalTokens.ForEachStatement;
import script.LRfinalTokens.ForStatement;
import script.LRfinalTokens.FunctionDefinition;
import script.LRfinalTokens.IfElseStatement;
import script.LRfinalTokens.IfStatement;
import script.LRfinalTokens.ModifiedAssignDefStatement;
import script.LRfinalTokens.ModifiedDefStatement;
import script.LRfinalTokens.NamespaceStatement;
import script.LRfinalTokens.NativeFunctionDefinition;
import script.LRfinalTokens.ReturnStatement;
import script.LRfinalTokens.ScriptBracketStatement;
import script.LRfinalTokens.SemicolonStatement;
import script.LRfinalTokens.StructDefinition;
import script.LRfinalTokens.TypedefStatement;
import script.LRfinalTokens.WhileStatement;
import script.LRterminals.Semicolon;
import script.ParseError;
import script.Script;
import script.Token;

public class Statement
extends Token {
    private Statement child;

    public Statement getChild() {
        return this.child;
    }

    public Statement(Token t, String code) {
        super(t, code);
    }

    public Statement(Token t) {
        super(t);
    }

    public Statement(Script s, String code) {
        super(s, code);
    }

    protected Statement(String s) {
        super(s);
    }

    public Statement(SemicolonStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(Semicolon s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ScriptBracketStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(FunctionDefinition s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(NativeFunctionDefinition s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(IfStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(IfElseStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ForEachStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(WhileStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(BreakStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ReturnStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ContinueStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(DefStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ModifiedDefStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(StructDefinition s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(TypedefStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ModifiedAssignDefStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(AssignDefStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(ForStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    public Statement(NamespaceStatement s) {
        super(s, (String)((Object)s));
        this.child = s;
    }

    @Override
    public Token unwrap() throws ParseError {
        return this.child.unwrap();
    }
}

