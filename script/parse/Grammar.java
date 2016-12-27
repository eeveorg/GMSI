/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import script.LRfinalTokens.ArrayName;
import script.LRfinalTokens.AssignDefStatement;
import script.LRfinalTokens.AssignExpression;
import script.LRfinalTokens.BinOpExpression;
import script.LRfinalTokens.BoolBinOpExpression;
import script.LRfinalTokens.BracketExpression;
import script.LRfinalTokens.BreakStatement;
import script.LRfinalTokens.CallExpression;
import script.LRfinalTokens.Comparison;
import script.LRfinalTokens.ConditionalExpression;
import script.LRfinalTokens.ConstructorExpression;
import script.LRfinalTokens.ContinueStatement;
import script.LRfinalTokens.DefStatement;
import script.LRfinalTokens.DereferName;
import script.LRfinalTokens.ForEachStatement;
import script.LRfinalTokens.ForStatement;
import script.LRfinalTokens.FunctionCall;
import script.LRfinalTokens.FunctionDefinition;
import script.LRfinalTokens.IfElseStatement;
import script.LRfinalTokens.IfStatement;
import script.LRfinalTokens.IncDecExpression;
import script.LRfinalTokens.InstanceofExpression;
import script.LRfinalTokens.ModifiedAssignDefStatement;
import script.LRfinalTokens.ModifiedDefStatement;
import script.LRfinalTokens.NamespaceStatement;
import script.LRfinalTokens.NativeFunctionDefinition;
import script.LRfinalTokens.PointBinOpExpression;
import script.LRfinalTokens.ReferExpression;
import script.LRfinalTokens.ReturnStatement;
import script.LRfinalTokens.ScriptBracketStatement;
import script.LRfinalTokens.SemicolonStatement;
import script.LRfinalTokens.StrToTypeExpression;
import script.LRfinalTokens.StructDefinition;
import script.LRfinalTokens.StructName;
import script.LRfinalTokens.TrapExpression;
import script.LRfinalTokens.TypeExpression;
import script.LRfinalTokens.TypeToStrExpression;
import script.LRfinalTokens.TypedefStatement;
import script.LRfinalTokens.UnOpExpression;
import script.LRfinalTokens.UnsetExpression;
import script.LRfinalTokens.WhileStatement;
import script.LRhighTokens.AcceptingStatementList;
import script.LRhighTokens.ComplexTypeName;
import script.LRhighTokens.Expression;
import script.LRhighTokens.GenericTypeName;
import script.LRhighTokens.Name;
import script.LRhighTokens.ParameterList;
import script.LRhighTokens.Signature;
import script.LRhighTokens.SimpleTypeList;
import script.LRhighTokens.Statement;
import script.LRhighTokens.StatementList;
import script.LRhighTokens.TypeCast;
import script.LRhighTokens.TypeGenerator;
import script.LRhighTokens.TypeList;
import script.LRhighTokens.TypeName;
import script.LRhighTokens.VariableDef;
import script.LRterminals.Arrow;
import script.LRterminals.Assignment;
import script.LRterminals.BBinOp;
import script.LRterminals.BinOp;
import script.LRterminals.BoolConstant;
import script.LRterminals.CloseArrayBracket;
import script.LRterminals.CloseBracket;
import script.LRterminals.CloseScriptBracket;
import script.LRterminals.CloseSharpBracket;
import script.LRterminals.Colon;
import script.LRterminals.Comma;
import script.LRterminals.Comparator;
import script.LRterminals.DereferOperator;
import script.LRterminals.ExternalIdentifyer;
import script.LRterminals.FloatConstant;
import script.LRterminals.Identifyer;
import script.LRterminals.IncDecOp;
import script.LRterminals.IntConstant;
import script.LRterminals.OpenArrayBracket;
import script.LRterminals.OpenBracket;
import script.LRterminals.OpenScriptBracket;
import script.LRterminals.OpenSharpBracket;
import script.LRterminals.Point;
import script.LRterminals.PointBinOp;
import script.LRterminals.Questionmark;
import script.LRterminals.ReferOperator;
import script.LRterminals.START;
import script.LRterminals.Semicolon;
import script.LRterminals.SimpleTypeName;
import script.LRterminals.StringConstant;
import script.LRterminals.TypeModifier;
import script.LRterminals.UnOp;
import script.LRterminals.WordBreak;
import script.LRterminals.WordCall;
import script.LRterminals.WordContinue;
import script.LRterminals.WordElse;
import script.LRterminals.WordExtends;
import script.LRterminals.WordExtern;
import script.LRterminals.WordFor;
import script.LRterminals.WordIf;
import script.LRterminals.WordInstanceof;
import script.LRterminals.WordNamespace;
import script.LRterminals.WordNative;
import script.LRterminals.WordNull;
import script.LRterminals.WordReturn;
import script.LRterminals.WordStrtotype;
import script.LRterminals.WordThis;
import script.LRterminals.WordTrap;
import script.LRterminals.WordType;
import script.LRterminals.WordTypedef;
import script.LRterminals.WordTypetostr;
import script.LRterminals.WordUnset;
import script.LRterminals.WordWhile;
import script.Token;
import script.parse.Accept;
import script.parse.ParseGenerator;
import script.parse.Production;

public class Grammar {
    public ProductionList productions;
    private HashSet<String> nonTerminals;
    private HashMap<String, Integer> tokenPriorities;
    Production startProduction;

    public boolean isTerminal(String s) {
        return !this.nonTerminals.contains(s);
    }

    public boolean isTerminal(Token t) {
        return this.isTerminal(t.getClass().getSimpleName());
    }

    public boolean isTerminal(Class<? extends Token> c) {
        return this.isTerminal(c.getSimpleName());
    }

    public void checkTerminal(Class<? extends Token> c) throws ParseGenerator.ParserGenerationError {
        if (this.isTerminal(c)) {
            if (!c.getPackage().getName().equals("script.LRterminals")) {
                throw new ParseGenerator.ParserGenerationError("The terminal Token " + c.getSimpleName() + " is not in the terminals Folder!");
            }
        } else if (c.getPackage().getName().equals("script.LRterminals")) {
            throw new ParseGenerator.ParserGenerationError("The non-terminal Token " + c.getSimpleName() + " is in the terminals Folder!");
        }
    }

    public void checkTerminals() throws ParseGenerator.ParserGenerationError {
        for (Production p : this.productions) {
            this.checkTerminal(p.lValue);
            for (Production.ProductionEntry c : p.rValue) {
                this.checkTerminal(c.wantedClass);
            }
        }
    }

    private void generateTerminalList() {
        for (Production p : this.productions) {
            this.nonTerminals.add(p.lValue.getSimpleName());
        }
    }

    private void addTokenPriority(Class<? extends Token> c, int priority) {
        this.tokenPriorities.put(c.getSimpleName(), priority);
    }

    public int getTokenPriority(Class<? extends Token> c) {
        if (!this.tokenPriorities.containsKey(c.getSimpleName())) {
            return -1;
        }
        return this.tokenPriorities.get(c.getSimpleName());
    }

    public Grammar() throws ParseGenerator.ParserGenerationError {
        this.productions = new ProductionList();
        this.nonTerminals = new HashSet();
        this.tokenPriorities = new HashMap();
        this.addTokenPriority(BinOp.class, 50);
        this.addTokenPriority(PointBinOp.class, 100);
        this.addTokenPriority(WordElse.class, 100);
        this.addTokenPriority(OpenArrayBracket.class, 150);
        this.addTokenPriority(Point.class, 130);
        this.addTokenPriority(IncDecOp.class, 115);
        this.addTokenPriority(Assignment.class, 25);
        this.addTokenPriority(Questionmark.class, 27);
        this.addTokenPriority(Comparator.class, 29);
        this.addTokenPriority(CloseSharpBracket.class, 29);
        this.addTokenPriority(OpenSharpBracket.class, 29);
        this.addTokenPriority(BBinOp.class, 28);
        this.addTokenPriority(Colon.class, 35);
        this.addTokenPriority(OpenBracket.class, 100);
        this.addTokenPriority(CloseBracket.class, 21);
        this.addTokenPriority(WordInstanceof.class, 21);
        this.addTokenPriority(OpenSharpBracket.class, 51);
        Production r = new Production(Accept.class);
        this.productions.add(r);
        r.addRValue(AcceptingStatementList.class);
        this.startProduction = r;
        this.productions.add(new Production(Statement.class, SemicolonStatement.class));
        this.productions.add(new Production(Statement.class, Semicolon.class));
        this.productions.add(new Production(Statement.class, ScriptBracketStatement.class));
        this.productions.add(new Production(Statement.class, FunctionDefinition.class));
        this.productions.add(new Production(Statement.class, NativeFunctionDefinition.class));
        this.productions.add(new Production(Statement.class, IfStatement.class, 50));
        this.productions.add(new Production(Statement.class, IfElseStatement.class));
        this.productions.add(new Production(Statement.class, ForEachStatement.class));
        this.productions.add(new Production(Statement.class, WhileStatement.class));
        this.productions.add(new Production(Statement.class, BreakStatement.class));
        this.productions.add(new Production(Statement.class, ReturnStatement.class));
        this.productions.add(new Production(Statement.class, ContinueStatement.class));
        this.productions.add(new Production(Statement.class, DefStatement.class));
        this.productions.add(new Production(Statement.class, ModifiedDefStatement.class));
        this.productions.add(new Production(Statement.class, ModifiedAssignDefStatement.class));
        this.productions.add(new Production(Statement.class, AssignDefStatement.class));
        this.productions.add(new Production(Statement.class, StructDefinition.class));
        this.productions.add(new Production(Statement.class, TypedefStatement.class));
        this.productions.add(new Production(Statement.class, ForStatement.class));
        this.productions.add(new Production(Statement.class, NamespaceStatement.class));
        this.productions.add(new Production(Expression.class, FunctionCall.class));
        this.productions.add(new Production(Expression.class, BinOpExpression.class));
        this.productions.add(new Production(Expression.class, PointBinOpExpression.class));
        this.productions.add(new Production(Expression.class, IntConstant.class));
        this.productions.add(new Production(Expression.class, FloatConstant.class));
        this.productions.add(new Production(Expression.class, StringConstant.class));
        this.productions.add(new Production(Expression.class, BoolConstant.class));
        this.productions.add(new Production(Expression.class, Name.class, 20));
        this.productions.add(new Production(Expression.class, BracketExpression.class));
        this.productions.add(new Production(Expression.class, IncDecExpression.class));
        this.productions.add(new Production(Expression.class, UnOpExpression.class));
        this.productions.add(new Production(Expression.class, TypeToStrExpression.class));
        this.productions.add(new Production(Expression.class, CallExpression.class));
        this.productions.add(new Production(Expression.class, WordNull.class));
        this.productions.add(new Production(Expression.class, TrapExpression.class));
        this.productions.add(new Production(Expression.class, Comparison.class));
        this.productions.add(new Production(Expression.class, ConditionalExpression.class));
        this.productions.add(new Production(Expression.class, ConstructorExpression.class));
        this.productions.add(new Production(Expression.class, AssignExpression.class));
        this.productions.add(new Production(Expression.class, BoolBinOpExpression.class));
        this.productions.add(new Production(Expression.class, ReferExpression.class));
        this.productions.add(new Production(Expression.class, InstanceofExpression.class));
        this.productions.add(new Production(Expression.class, WordThis.class));
        this.productions.add(new Production(Expression.class, UnsetExpression.class));
        this.productions.add(new Production(Name.class, Identifyer.class, 50));
        this.productions.add(new Production(Name.class, ExternalIdentifyer.class));
        this.productions.add(new Production(Name.class, ArrayName.class));
        this.productions.add(new Production(Name.class, StructName.class));
        this.productions.add(new Production(Name.class, DereferName.class));
        this.productions.add(new Production(Name.class, OpenBracket.class, Name.class, CloseBracket.class));
        this.productions.add(new Production(TypeName.class, SimpleTypeName.class));
        this.productions.add(new Production(TypeName.class, GenericTypeName.class));
        this.productions.add(new Production(TypeName.class, ComplexTypeName.class));
        this.productions.add(new Production(DereferName.class, DereferOperator.class, Expression.class, 170));
        this.productions.add(new Production(DereferName.class, DereferOperator.class, Name.class, 170));
        this.productions.add(new Production(ReferExpression.class, ReferOperator.class, Name.class, 170));
        this.productions.add(new Production(ComplexTypeName.class, TypeName.class, DereferOperator.class));
        this.productions.add(new Production(GenericTypeName.class, TypeName.class, OpenSharpBracket.class, TypeList.class, CloseSharpBracket.class));
        this.productions.add(new Production(NamespaceStatement.class, WordNamespace.class, Expression.class, ScriptBracketStatement.class));
        this.productions.add(new Production(UnsetExpression.class, WordUnset.class, OpenBracket.class, Name.class, CloseBracket.class));
        this.productions.add(new Production(BracketExpression.class, OpenBracket.class, Expression.class, CloseBracket.class));
        this.productions.add(new Production(SemicolonStatement.class, Expression.class, Semicolon.class));
        this.productions.add(new Production(ScriptBracketStatement.class, OpenScriptBracket.class, StatementList.class, CloseScriptBracket.class));
        this.productions.add(new Production(ScriptBracketStatement.class, OpenScriptBracket.class, CloseScriptBracket.class));
        this.productions.add(new Production(AcceptingStatementList.class, START.class, StatementList.class));
        this.productions.add(new Production(AcceptingStatementList.class, START.class, TypeName.class));
        this.productions.add(new Production(StatementList.class, Statement.class));
        this.productions.add(new Production(StatementList.class, StatementList.class, Statement.class));
        this.productions.add(new Production(ParameterList.class, Expression.class));
        this.productions.add(new Production(ParameterList.class, ParameterList.class, Comma.class, Expression.class));
        this.productions.add(new Production(Signature.class, VariableDef.class));
        this.productions.add(new Production(Signature.class, Signature.class, Comma.class, VariableDef.class));
        this.productions.add(new Production(FunctionDefinition.class, TypeName.class, Identifyer.class, OpenBracket.class, CloseBracket.class, ScriptBracketStatement.class));
        this.productions.add(new Production(FunctionDefinition.class, TypeName.class, Identifyer.class, OpenBracket.class, Signature.class, CloseBracket.class, ScriptBracketStatement.class));
        this.productions.add(new Production(FunctionDefinition.class, TypeName.class, SimpleTypeName.class, OpenBracket.class, Signature.class, CloseBracket.class, ScriptBracketStatement.class));
        this.productions.add(new Production(NativeFunctionDefinition.class, WordNative.class, TypeName.class, Identifyer.class, OpenBracket.class, CloseBracket.class, Semicolon.class));
        this.productions.add(new Production(NativeFunctionDefinition.class, WordNative.class, TypeName.class, Identifyer.class, OpenBracket.class, Signature.class, CloseBracket.class, Semicolon.class));
        this.productions.add(new Production(NativeFunctionDefinition.class, WordNative.class, TypeName.class, SimpleTypeName.class, OpenBracket.class, Signature.class, CloseBracket.class, Semicolon.class));
        this.productions.add(new Production(FunctionCall.class, Identifyer.class, OpenBracket.class, CloseBracket.class));
        this.productions.add(new Production(FunctionCall.class, Identifyer.class, OpenBracket.class, ParameterList.class, CloseBracket.class));
        this.productions.add(new Production(FunctionCall.class, SimpleTypeName.class, OpenBracket.class, ParameterList.class, CloseBracket.class));
        this.productions.add(new Production(CallExpression.class, Expression.class, Point.class, WordCall.class, OpenBracket.class, ParameterList.class, CloseBracket.class));
        this.productions.add(new Production(CallExpression.class, Expression.class, Point.class, WordCall.class, OpenBracket.class, CloseBracket.class));
        this.productions.add(new Production(SimpleTypeList.class, SimpleTypeName.class));
        this.productions.add(new Production(SimpleTypeList.class, SimpleTypeList.class, Comma.class, SimpleTypeName.class));
        this.productions.add(new Production(TypeList.class, TypeName.class));
        this.productions.add(new Production(TypeList.class, TypeList.class, Comma.class, TypeName.class));
        this.productions.add(new Production(StructDefinition.class, WordTypedef.class, SimpleTypeName.class, SimpleTypeName.class, ScriptBracketStatement.class));
        this.productions.add(new Production(StructDefinition.class, WordTypedef.class, SimpleTypeName.class, SimpleTypeName.class, WordExtends.class, SimpleTypeList.class, ScriptBracketStatement.class));
        this.productions.add(new Production(TypedefStatement.class, WordTypedef.class, SimpleTypeName.class, WordExtends.class, TypeName.class, Semicolon.class));
        this.productions.add(new Production(TypedefStatement.class, WordTypedef.class, SimpleTypeList.class, WordExtern.class, Semicolon.class));
        this.productions.add(new Production(TypeGenerator.class, TypeExpression.class));
        this.productions.add(new Production(TypeGenerator.class, StrToTypeExpression.class));
        this.productions.add(new Production(IfStatement.class, WordIf.class, OpenBracket.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(IfElseStatement.class, IfStatement.class, WordElse.class, Statement.class));
        this.productions.add(new Production(ForEachStatement.class, WordFor.class, OpenBracket.class, Name.class, Colon.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(ForEachStatement.class, WordFor.class, OpenBracket.class, VariableDef.class, Colon.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(ForEachStatement.class, WordFor.class, OpenBracket.class, VariableDef.class, Arrow.class, VariableDef.class, Colon.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(WhileStatement.class, WordWhile.class, OpenBracket.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(ForStatement.class, WordFor.class, OpenBracket.class, Statement.class, Expression.class, Semicolon.class, Expression.class, CloseBracket.class, Statement.class));
        this.productions.add(new Production(BinOpExpression.class, Expression.class, BinOp.class, Expression.class, 50));
        this.productions.add(new Production(BoolBinOpExpression.class, Expression.class, BBinOp.class, Expression.class, 28));
        this.productions.add(new Production(InstanceofExpression.class, Name.class, WordInstanceof.class, TypeName.class, 50));
        this.productions.add(new Production(InstanceofExpression.class, Name.class, WordInstanceof.class, TypeGenerator.class, 50));
        this.productions.add(new Production(PointBinOpExpression.class, Expression.class, PointBinOp.class, Expression.class, 100));
        this.productions.add(new Production(ArrayName.class, Expression.class, OpenArrayBracket.class, Expression.class, CloseArrayBracket.class));
        this.productions.add(new Production(ArrayName.class, Expression.class, OpenArrayBracket.class, CloseArrayBracket.class));
        this.productions.add(new Production(StructName.class, Expression.class, Point.class, Identifyer.class, 130));
        this.productions.add(new Production(IncDecExpression.class, Name.class, IncDecOp.class));
        this.productions.add(new Production(UnOpExpression.class, UnOp.class, Expression.class, 110));
        this.productions.add(new Production(TypeCast.class, OpenBracket.class, TypeName.class, CloseBracket.class));
        this.productions.add(new Production(TypeCast.class, OpenBracket.class, TypeGenerator.class, CloseBracket.class));
        this.productions.add(new Production(UnOpExpression.class, TypeCast.class, Expression.class, 110));
        this.productions.add(new Production(BreakStatement.class, WordBreak.class, Semicolon.class));
        this.productions.add(new Production(ReturnStatement.class, WordReturn.class, Semicolon.class));
        this.productions.add(new Production(ReturnStatement.class, WordReturn.class, Expression.class, Semicolon.class));
        this.productions.add(new Production(ContinueStatement.class, WordContinue.class, Semicolon.class));
        this.productions.add(new Production(TrapExpression.class, WordTrap.class, OpenBracket.class, StringConstant.class, CloseBracket.class));
        this.productions.add(new Production(TypeExpression.class, Expression.class, Point.class, WordType.class));
        this.productions.add(new Production(StrToTypeExpression.class, Expression.class, Point.class, WordStrtotype.class));
        this.productions.add(new Production(TypeToStrExpression.class, TypeName.class, Point.class, WordTypetostr.class));
        this.productions.add(new Production(TypeToStrExpression.class, TypeGenerator.class, Point.class, WordTypetostr.class));
        this.productions.add(new Production(TypeToStrExpression.class, Expression.class, Point.class, WordTypetostr.class));
        this.productions.add(new Production(Comparison.class, Expression.class, Comparator.class, Expression.class, 29));
        this.productions.add(new Production(Comparison.class, Expression.class, OpenSharpBracket.class, Expression.class, 29));
        this.productions.add(new Production(Comparison.class, Expression.class, CloseSharpBracket.class, Expression.class, 29));
        this.productions.add(new Production(ConditionalExpression.class, Expression.class, Questionmark.class, Expression.class, Colon.class, Expression.class, 27));
        this.productions.add(new Production(ConstructorExpression.class, SimpleTypeName.class, OpenBracket.class, CloseBracket.class));
        this.productions.add(new Production(ConstructorExpression.class, TypeGenerator.class, OpenBracket.class, CloseBracket.class));
        this.productions.add(new Production(AssignExpression.class, Name.class, Assignment.class, Expression.class, 25));
        this.productions.add(new Production(VariableDef.class, TypeName.class, Identifyer.class));
        this.productions.add(new Production(DefStatement.class, VariableDef.class, Semicolon.class));
        this.productions.add(new Production(ModifiedDefStatement.class, TypeModifier.class, TypeName.class, Identifyer.class, Semicolon.class));
        this.productions.add(new Production(ModifiedAssignDefStatement.class, TypeModifier.class, TypeName.class, Identifyer.class, Assignment.class, Expression.class, Semicolon.class));
        this.productions.add(new Production(AssignDefStatement.class, VariableDef.class, Assignment.class, Expression.class, Semicolon.class));
        this.generateTerminalList();
        this.checkTerminals();
    }

    public class ProductionList
    extends ArrayList<Production> {
        private static final long serialVersionUID = 1;
        int curCount;

        public ProductionList() {
            super(50);
            this.curCount = 0;
        }

        @Override
        public boolean add(Production p) {
            p.num = this.curCount++;
            return super.add(p);
        }
    }

}

