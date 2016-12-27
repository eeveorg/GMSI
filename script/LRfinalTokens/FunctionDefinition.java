/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.EvaluationResult;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRfinalTokens.ScriptBracketStatement;
import script.LRhighTokens.Signature;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeName;
import script.LRhighTokens.VariableDef;
import script.LRterminals.CloseBracket;
import script.LRterminals.Identifyer;
import script.LRterminals.OpenBracket;
import script.LRterminals.SimpleTypeName;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;
import script.names.NameResolver;

public class FunctionDefinition
extends Statement {
    public Identifyer functionName;
    public TypeName returnType;
    public LinkedList<VariableDef> parameters;
    public ScriptBracketStatement functionCode;
    private int numLocals = -1;
    private EvaluationResult returnListener;

    public FunctionDefinition(TypeName r, Identifyer f, OpenBracket o, Signature s, CloseBracket c, ScriptBracketStatement funcCode) throws ParseError {
        super(r, r + " " + f + o + s + c + funcCode);
        this.functionName = f;
        this.returnType = r;
        this.parameters = s.getList();
        this.functionCode = funcCode;
        this.returnListener = this.owner.getEvalResult();
        this.owner.getNameResolver().addFunction(this);
    }

    public FunctionDefinition(TypeName r, SimpleTypeName f, OpenBracket o, Signature s, CloseBracket c, ScriptBracketStatement funcCode) throws ParseError {
        super(r, r + " " + f + o + s + c + funcCode);
        this.functionName = new Identifyer(f);
        this.returnType = r;
        this.parameters = s.getList();
        this.functionCode = funcCode;
        this.returnListener = this.owner.getEvalResult();
        this.owner.getNameResolver().addFunction(this);
    }

    public FunctionDefinition(TypeName r, Identifyer f, OpenBracket o, CloseBracket c, ScriptBracketStatement funcCode) throws ParseError {
        super(r, r + " " + f + o + c + funcCode);
        this.functionName = f;
        this.returnType = r;
        this.parameters = new LinkedList();
        this.functionCode = funcCode;
        this.returnListener = this.owner.getEvalResult();
        this.owner.getNameResolver().addFunction(this);
    }

    protected FunctionDefinition(TypeName r, Identifyer f, Signature s) throws ParseError {
        super(r, "native " + r + " " + f + "(" + (s == null ? "" : s) + ");");
        this.functionName = f;
        this.returnType = r;
        this.parameters = s == null ? new LinkedList() : s.getList();
        this.functionCode = null;
        this.owner.getNameResolver().addFunction(this);
    }

    @Override
    public Token unwrap() throws ParseError {
        LocalNameMapper mapper = LocalNameMapper.pushMapper();
        for (VariableDef d : this.parameters) {
            d.unwrap();
            d.name.localIndex = mapper.registerLocal(d.name.getCode());
        }
        this.returnType = (TypeName)this.returnType.unwrap();
        if (this.functionCode != null) {
            this.functionCode.unwrap();
        }
        this.numLocals = LocalNameMapper.popMapper();
        return this;
    }

    public void putParamsOnStack(LinkedList<DataObject> inputParams) throws EvaluationError {
        NameResolver nr = this.owner.getNameResolver();
        nr.pushLocalNew(this.numLocals);
        ListIterator<DataObject> i1 = inputParams.listIterator();
        ListIterator<VariableDef> i2 = this.parameters.listIterator();
        while (i1.hasNext()) {
            DataObject r = i1.next();
            VariableDef v = i2.next();
            try {
                nr.addLocal(v.name.localIndex, r.implicitCastTo(v.getType()), v.getType());
                continue;
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, String.valueOf(e.getMessage()) + " (" + v.name.getCode() + ")");
            }
        }
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return voidResult;
    }

    public DataObject call() throws EvaluationError {
        DataObject r = this.functionCode.eval();
        r = !this.returnListener.wantReturn() || this.returnListener.value == null ? voidResult : this.returnListener.value;
        this.returnListener.clearFlags();
        try {
            r.implicitCastTo(this.returnType.getType());
        }
        catch (ImplicitCastError e) {
            throw new EvaluationError(this, "Type Safety Error: Function " + this.functionName + " returned <" + r.getType() + "> but should return <" + this.returnType + ">");
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return r;
    }

    public String getSignature() throws ParseError {
        StringBuilder output = new StringBuilder();
        output.append("(");
        for (VariableDef r : this.parameters) {
            if (r.type.getCode().equalsIgnoreCase("void")) {
                throw new ParseError("void is no valid parameter type");
            }
            output.append(r.type.toString());
            if (this.parameters.getLast().equals(r)) continue;
            output.append(",");
        }
        output.append(")");
        return output.toString();
    }
}

