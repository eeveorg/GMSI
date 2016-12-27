/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRterminals.DereferOperator;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.ReferenceObject;
import script.names.VarHandle;

public class DereferName
extends Name {
    Expression operand;

    public DereferName(DereferOperator Operator, Expression Op1) {
        super(Operator, Operator + Op1);
        this.operand = Op1;
    }

    public DereferName(DereferOperator Operator, Name Op1) {
        super(Operator, Operator + Op1);
        this.operand = Op1;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand = (Expression)this.operand.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        DataObject res;
        super.eval();
        DataObject r = this.operand.eval();
        if (!(this.operand.eval() instanceof ReferenceObject)) {
            throw new EvaluationError(this, "Trying to use the dereferencing operator * onto a non pointer value");
        }
        try {
            res = ((ReferenceObject)r).derefer();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        return res;
    }

    public DataObject evalL() throws EvaluationError {
        DataObject r = this.operand.eval();
        if (!(this.operand.eval() instanceof ReferenceObject)) {
            throw new EvaluationError(this, "Trying to use the dereferencing operator * onto a non pointer value");
        }
        return r;
    }

    @Override
    public VarHandle resolve() throws EvaluationError {
        DataObject point = this.operand.eval();
        if (point instanceof NullObject) {
            throw new EvaluationError(this, "Trying to dereferenciate a null pointer!");
        }
        if (!(point instanceof ReferenceObject)) {
            throw new EvaluationError(this, "Error in derefer name: The variable <" + this.operand.getCode() + "> handed to the star operator is no valid pointer, cannot dereferenciate it.");
        }
        return ((ReferenceObject)point).getReference();
    }
}

