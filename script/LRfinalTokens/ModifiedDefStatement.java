/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeName;
import script.LRterminals.Identifyer;
import script.LRterminals.Semicolon;
import script.LRterminals.TypeModifier;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.NameResolver;

public class ModifiedDefStatement
extends Statement {
    public TypeModifier mod;
    public TypeName type;
    public Identifyer name;

    public ModifiedDefStatement(TypeModifier tm, TypeName t, Identifyer i, Semicolon s) throws ParseError {
        super(tm, tm + " " + t + " " + i + s);
        this.mod = tm;
        this.type = t;
        this.name = i;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.type = (TypeName)this.type.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        try {
            this.owner.getNameResolver().addGlobal(this.name.toString(), this.type.getType().getDefaultInstance());
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return voidResult;
    }
}

