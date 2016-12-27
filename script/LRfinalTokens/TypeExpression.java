/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.TypeGenerator;
import script.LRterminals.Point;
import script.LRterminals.WordType;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class TypeExpression
extends TypeGenerator {
    Expression expr;

    public TypeExpression(Expression e, Point p, WordType t) throws ParseError {
        super(e, e + p + t);
        this.expr = e;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.expr = (Expression)this.expr.unwrap();
        return this;
    }

    @Override
    public DataType getType() throws InternalScriptError {
        return this.expr.eval().getContentDataType();
    }
}

