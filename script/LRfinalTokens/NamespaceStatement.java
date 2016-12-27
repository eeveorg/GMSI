/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.LRfinalTokens.ScriptBracketStatement;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Statement;
import script.LRterminals.WordNamespace;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.NameResolver;

public class NamespaceStatement
extends Statement {
    public ScriptBracketStatement childCode;
    public Expression namespace;

    public NamespaceStatement(WordNamespace w, Expression e, ScriptBracketStatement funcCode) throws ParseError {
        super(w, w + e + funcCode);
        this.childCode = funcCode;
        this.namespace = e;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.childCode.unwrap();
        this.namespace = (Expression)this.namespace.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject o = this.namespace.eval();
        if (!(o instanceof ContainerObject)) {
            throw new EvaluationError(this.namespace, "The expression after namespace must evaluate to a struct or array, but it evaluates to " + o.getType());
        }
        this.owner.getNameResolver().pushNamespace((ContainerObject)o);
        this.childCode.eval();
        this.owner.getNameResolver().popNamespace();
        return voidResult;
    }
}

