/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import script.LRfinalTokens.AssignExpression;
import script.LRfinalTokens.BinOpExpression;
import script.LRfinalTokens.BoolBinOpExpression;
import script.LRfinalTokens.BracketExpression;
import script.LRfinalTokens.CallExpression;
import script.LRfinalTokens.Comparison;
import script.LRfinalTokens.ConditionalExpression;
import script.LRfinalTokens.ConstructorExpression;
import script.LRfinalTokens.FunctionCall;
import script.LRfinalTokens.IncDecExpression;
import script.LRfinalTokens.InstanceofExpression;
import script.LRfinalTokens.PointBinOpExpression;
import script.LRfinalTokens.ReferExpression;
import script.LRfinalTokens.TrapExpression;
import script.LRfinalTokens.TypeToStrExpression;
import script.LRfinalTokens.UnOpExpression;
import script.LRfinalTokens.UnsetExpression;
import script.LRhighTokens.Name;
import script.LRterminals.BoolConstant;
import script.LRterminals.FloatConstant;
import script.LRterminals.IntConstant;
import script.LRterminals.StringConstant;
import script.LRterminals.WordNull;
import script.LRterminals.WordThis;
import script.ParseError;
import script.Script;
import script.Token;

public class Expression
extends Token {
    private Expression child;

    public Expression(Token t, String code) {
        super(t, code);
    }

    public Expression(Token t) {
        super(t);
    }

    public Expression(Script s, String code) {
        super(s, code);
    }

    public Expression getChild() {
        return this.child;
    }

    public Expression(FunctionCall child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(BinOpExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(PointBinOpExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(BracketExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(IntConstant child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(FloatConstant child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(StringConstant child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(BoolConstant child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(Name child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(IncDecExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(UnOpExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(WordNull child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(TrapExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(Comparison child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(ConditionalExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(ConstructorExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(AssignExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(BoolBinOpExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(ReferExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(InstanceofExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(WordThis child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(UnsetExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(TypeToStrExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    public Expression(CallExpression child) {
        super(child, child.getCode());
        this.child = child;
    }

    @Override
    public Token unwrap() throws ParseError {
        return this.child.unwrap();
    }
}

