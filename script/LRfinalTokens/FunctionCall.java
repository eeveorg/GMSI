/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.FunctionDefinition;
import script.LRhighTokens.Expression;
import script.LRhighTokens.ParameterList;
import script.LRterminals.CloseBracket;
import script.LRterminals.Identifyer;
import script.LRterminals.OpenBracket;
import script.LRterminals.SimpleTypeName;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.names.NameResolver;

public class FunctionCall
extends Expression {
    Identifyer functionName;
    LinkedList<Expression> parameters;

    protected FunctionCall(Token t, String code) {
        super(t, code);
    }

    public FunctionCall(Identifyer name, OpenBracket o, ParameterList p, CloseBracket c) {
        super(name, name + o + p + c);
        this.functionName = name;
        this.parameters = p.getList();
    }

    public FunctionCall(SimpleTypeName name, OpenBracket o, ParameterList p, CloseBracket c) {
        super(name, name + o + p + c);
        this.functionName = new Identifyer(name);
        this.parameters = p.getList();
    }

    public FunctionCall(Identifyer name, OpenBracket o, CloseBracket c) {
        super(name, name + o + c);
        this.functionName = name;
        this.parameters = new LinkedList();
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<Expression> iter = this.parameters.listIterator();
        while (iter.hasNext()) {
            Expression s = iter.next();
            iter.set((Expression)s.unwrap());
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        FunctionDefinition f;
        DataObject result;
        super.eval();
        LinkedList<DataObject> paramResults = new LinkedList<DataObject>();
        for (Expression e : this.parameters) {
            paramResults.add(e.eval());
        }
        try {
            f = this.owner.getNameResolver().resolveFunction(this.functionName.toString(), paramResults);
        }
        catch (InternalScriptError e1) {
            throw new EvaluationError(this, e1.getMessage());
        }
        f.putParamsOnStack(paramResults);
        try {
            result = f.call();
        }
        catch (EvaluationError e1) {
            e1.addCall(this);
            throw e1;
        }
        this.owner.getNameResolver().popBig();
        return result;
    }

    public String getFunctionName() {
        return this.functionName.toString();
    }
}

