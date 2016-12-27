/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.EvaluationError;
import script.LRfinalTokens.ArrayName;
import script.LRfinalTokens.DereferName;
import script.LRfinalTokens.StructName;
import script.LRhighTokens.Expression;
import script.LRterminals.CloseBracket;
import script.LRterminals.ExternalIdentifyer;
import script.LRterminals.Identifyer;
import script.LRterminals.OpenBracket;
import script.ParseError;
import script.Script;
import script.SourceObject;
import script.Token;
import script.names.LocalNameMapper;
import script.names.VarHandle;

public class Name
extends Expression {
    private Name child;
    private boolean childIsIdent = false;

    @Override
    public Name getChild() {
        return this.child;
    }

    public Name(Token t, String code) {
        super(t, code);
    }

    public Name(Token t) {
        super(t);
    }

    public Name(Script s, String code) {
        super(s, code);
    }

    public Name(Identifyer i) {
        super(i, (String)((Object)i));
        this.childIsIdent = true;
        this.child = i;
    }

    public Name(ArrayName i) {
        super(i, (String)((Object)i));
        this.child = i;
    }

    public Name(StructName i) {
        super(i, (String)((Object)i));
        this.child = i;
    }

    public Name(OpenBracket i, Name j, CloseBracket k) {
        super(i, "(" + j + ")");
        this.child = j;
    }

    public Name(DereferName i) {
        super(i, (String)((Object)i));
        this.child = i;
    }

    public Name(ExternalIdentifyer i) {
        super(i, (String)((Object)i));
        this.child = i;
    }

    @Override
    public Token unwrap() throws ParseError {
        if (this.childIsIdent) {
            Identifyer i = (Identifyer)this.child;
            i.localIndex = LocalNameMapper.getMapper().resolveLocal(i.code);
        }
        return this.child.unwrap();
    }

    public VarHandle resolve() throws EvaluationError {
        throw new EvaluationError(this, "cannot resolve names, code:" + this.code + " line: " + this.line + " file " + this.getSourceObject());
    }
}

