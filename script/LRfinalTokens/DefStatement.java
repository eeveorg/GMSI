/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeName;
import script.LRhighTokens.VariableDef;
import script.LRterminals.Identifyer;
import script.LRterminals.Semicolon;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;
import script.names.NameResolver;

public class DefStatement
extends Statement {
    public Identifyer name;
    TypeName type;

    public DefStatement(VariableDef n, Semicolon s) throws ParseError {
        super(n, n + ";");
        this.name = n.name;
        this.type = n.type;
    }

    public DataType getType() throws InternalScriptError {
        return this.type.getType();
    }

    @Override
    public Token unwrap() throws ParseError {
        this.name.localIndex = LocalNameMapper.getMapper().registerLocal(this.name.toString());
        this.type = (TypeName)this.type.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        try {
            this.owner.getNameResolver().addLocal(this.name.localIndex, this.type.getType().getDefaultInstance(), this.type.getType());
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return voidResult;
    }
}

