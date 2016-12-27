/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeName;
import script.LRhighTokens.VariableDef;
import script.LRterminals.Assignment;
import script.LRterminals.Identifyer;
import script.LRterminals.Semicolon;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.LocalNameMapper;
import script.names.NameResolver;

public class AssignDefStatement
extends Statement {
    public Identifyer name;
    public TypeName type;
    public Expression rValue;
    private NameResolver nr;

    public AssignDefStatement(VariableDef n, Assignment a, Expression e, Semicolon s) throws ParseError {
        super(n, n + a + e + s);
        if (!a.getCode().equals("=")) {
            throw new ParseError("Cannot use the assignment " + a.getCode() + " in a definition");
        }
        this.type = n.type;
        this.name = n.name;
        this.rValue = e;
        this.nr = this.owner.getNameResolver();
    }

    @Override
    public Token unwrap() throws ParseError {
        this.name.localIndex = LocalNameMapper.getMapper().registerLocal(this.name.toString());
        this.type = (TypeName)this.type.unwrap();
        this.rValue = (Expression)this.rValue.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject e1 = this.rValue.eval();
        try {
            this.nr.addLocal(this.name.localIndex, e1.implicitCastTo(this.type.getType()), this.type.getType());
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return e1;
    }
}

