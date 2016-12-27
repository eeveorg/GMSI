/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.TypeGenerator;
import script.LRhighTokens.TypeName;
import script.LRterminals.Point;
import script.LRterminals.WordTypetostr;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.StringObject;

public class TypeToStrExpression
extends Expression {
    TypeName typeName = null;
    Expression expr = null;

    public TypeToStrExpression(TypeName t, Point p, WordTypetostr w) {
        super(t, t + p + w);
        this.typeName = t;
    }

    public TypeToStrExpression(TypeGenerator t, Point p, WordTypetostr w) {
        super(t, t + p + w);
        this.typeName = t;
    }

    public TypeToStrExpression(Expression t, Point p, WordTypetostr w) {
        super(t, t + p + w);
        this.expr = t;
    }

    @Override
    public Token unwrap() throws ParseError {
        if (this.typeName != null) {
            this.typeName = (TypeName)this.typeName.unwrap();
        }
        if (this.expr != null) {
            this.expr = (Expression)this.expr.unwrap();
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        try {
            if (this.typeName != null) {
                return new StringObject(this.typeName.getType().toString());
            }
            return new StringObject(this.expr.eval().getContentDataType().toString());
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

