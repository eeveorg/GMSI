/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.FunctionCall;
import script.LRfinalTokens.FunctionDefinition;
import script.LRhighTokens.Expression;
import script.LRhighTokens.ParameterList;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.Point;
import script.LRterminals.WordCall;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.StringObject;
import script.names.NameResolver;

public class CallExpression
extends FunctionCall {
    Expression functionName;
    String name = "-";

    public CallExpression(Expression name, Point p, WordCall ca, OpenBracket o, ParameterList params, CloseBracket c) {
        super(name, name + p + ca + o + params + c);
        this.functionName = name;
        this.parameters = params.getList();
    }

    public CallExpression(Expression name, Point p, WordCall ca, OpenBracket o, CloseBracket c) {
        super(name, name + p + ca + o + c);
        this.functionName = name;
        this.parameters = new LinkedList();
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<Expression> iter = this.parameters.listIterator();
        while (iter.hasNext()) {
            Expression s = (Expression)iter.next();
            iter.set((Expression)s.unwrap());
        }
        this.functionName = (Expression)this.functionName.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject result;
        FunctionDefinition f;
        LinkedList<DataObject> paramResults = new LinkedList<DataObject>();
        DataObject o = this.functionName.eval();
        if (o.isNull()) {
            throw new EvaluationError(this, "the left side of the .call operator is null!");
        }
        if (!(o instanceof StringObject)) {
            throw new EvaluationError(this, "the left side of the .call operator must have a value of type string, but its value is of type " + o.getType().toString());
        }
        this.name = o;
        if (this.name.trim().equals("")) {
            throw new EvaluationError(this, "the left side of the .call operator may not be an empty string or whitespaces!");
        }
        for (Expression e : this.parameters) {
            paramResults.add(e.eval());
        }
        try {
            f = this.owner.getNameResolver().resolveFunction(this.name, paramResults);
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

    @Override
    public String getFunctionName() {
        return "\"" + this.name + "\".call";
    }
}

