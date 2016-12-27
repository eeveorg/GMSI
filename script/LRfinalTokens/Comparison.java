/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRterminals.CloseSharpBracket;
import script.LRterminals.Comparator;
import script.LRterminals.OpenSharpBracket;
import script.ParseError;
import script.Token;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;

public class Comparison
extends Expression {
    private Expression operand1;
    private Expression operand2;
    private Comparator operator;
    private int compar = -1;
    public static final int COMP_EQ = 0;
    public static final int COMP_NE = 1;
    public static final int COMP_LTE = 2;
    public static final int COMP_LT = 3;
    public static final int COMP_GTE = 4;
    public static final int COMP_GT = 5;

    private int compToInt(String s) {
        if (s.equals("<")) {
            return 3;
        }
        if (s.equals(">")) {
            return 5;
        }
        if (s.equals("<=")) {
            return 2;
        }
        if (s.equals(">=")) {
            return 4;
        }
        if (s.equals("==")) {
            return 0;
        }
        if (s.equals("!=")) {
            return 1;
        }
        throw new RuntimeException("Unknown comparator " + s);
    }

    public Comparison(Expression e1, Comparator Operator, Expression e2) {
        super(e1, e1 + Operator + e2);
        this.operand1 = e1;
        this.operand2 = e2;
        this.operator = Operator;
        this.compar = this.compToInt(this.operator.getCode());
    }

    public Comparison(Expression e1, OpenSharpBracket Operator, Expression e2) {
        super(e1, e1 + Operator + e2);
        this.operand1 = e1;
        this.operand2 = e2;
        this.operator = new Comparator(Operator);
        this.compar = 3;
    }

    public Comparison(Expression e1, CloseSharpBracket Operator, Expression e2) {
        super(e1, e1 + Operator + e2);
        this.operand1 = e1;
        this.operand2 = e2;
        this.operator = new Comparator(Operator);
        this.compar = 5;
    }

    @Override
    public Token unwrap() throws ParseError {
        this.operand1 = (Expression)this.operand1.unwrap();
        this.operand2 = (Expression)this.operand2.unwrap();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        DataObject e1 = this.operand1.eval();
        DataObject e2 = this.operand2.eval();
        if (e1 instanceof NullObject || e2 instanceof NullObject) {
            boolean not;
            switch (this.compar) {
                case 0: {
                    not = true;
                    break;
                }
                case 1: {
                    not = false;
                    break;
                }
                default: {
                    throw new EvaluationError(this, "Evaluation Error: Cannot compare null with " + this.operator + " :" + this.code);
                }
            }
            if (e1 instanceof NullObject && e2 instanceof NullObject) {
                return BoolObject.getBool(not);
            }
            return BoolObject.getBool(!not);
        }
        try {
            return BoolObject.getBool(e1.compare(e2, this.compar));
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

