/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.TypeGenerator;
import script.LRhighTokens.TypeName;
import script.LRterminals.CloseBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.SimpleTypeName;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class ConstructorExpression
extends Expression {
    public TypeName typeName;

    public ConstructorExpression(SimpleTypeName w, OpenBracket o, CloseBracket c) {
        super(w, w + "()");
        this.typeName = w;
    }

    public ConstructorExpression(TypeGenerator w, OpenBracket o, CloseBracket c) {
        super(w, w + "()");
        this.typeName = w;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.typeName = (TypeName)this.typeName.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject o;
        super.eval();
        try {
            o = this.typeName.getType().getNewInstance();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return o;
    }
}

