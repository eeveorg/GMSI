/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRhighTokens.TypeGenerator;
import script.LRhighTokens.TypeName;
import script.LRterminals.WordInstanceof;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.VarHandle;

public class InstanceofExpression
extends Expression {
    private Name operand1;
    private TypeName operand2;

    public InstanceofExpression(Name n, WordInstanceof w, TypeName t) {
        super(n, n + " instanceof " + t);
        this.operand1 = n;
        this.operand2 = t;
    }

    public InstanceofExpression(Name n, WordInstanceof w, TypeGenerator t) {
        super(n, n + " instanceof " + t);
        this.operand1 = n;
        this.operand2 = t;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand1 = (Name)this.operand1.unwrap();
        this.operand2 = (TypeName)this.operand2.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        try {
            DataType d1 = this.operand1.resolve().getValue().getContentDataType();
            DataType d2 = this.operand2.getType();
            return BoolObject.getBool(d1.isDerivedFrom(d2));
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

