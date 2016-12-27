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
import script.LRterminals.WordStrtotype;
import script.ParseError;
import script.ParsedCode;
import script.Script;
import script.SourceObject;
import script.SyntaxError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;

public class StrToTypeExpression
extends TypeGenerator {
    Expression expr;

    public StrToTypeExpression(Expression e, Point p, WordStrtotype t) throws ParseError {
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
        Token c;
        String s = this.expr.eval().toString();
        try {
            c = this.owner.parseCode(s, new SourceObject("strtotype")).getParsedToken();
        }
        catch (SyntaxError e) {
            throw new EvaluationError(this, "strtotype error: The string \"" + s + "\" is no valid type name:\n" + e.getMessage());
        }
        catch (ParseError e) {
            throw new EvaluationError(this, "strtotype error: The string \"" + s + "\" is no valid type name:\n" + e.getMessage());
        }
        if (!(c instanceof TypeName)) {
            throw new EvaluationError(this, "strtotype error: The string \"" + s + "\" is no valid type name!");
        }
        TypeName t = (TypeName)c;
        return t.getType();
    }
}

