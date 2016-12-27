/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.LRhighTokens.DummyTypeDefinition;
import script.LRhighTokens.TypeDefinition;
import script.LRhighTokens.TypeName;
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
import script.LRterminals.Semicolon;
import script.LRterminals.SimpleTypeName;
import script.LRterminals.StringConstant;
import script.LRterminals.TypeModifier;
import script.LRterminals.UnOp;
import script.LRterminals.WordBreak;
import script.LRterminals.WordCall;
import script.LRterminals.WordCatch;
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
import script.LRterminals.WordTry;
import script.LRterminals.WordType;
import script.LRterminals.WordTypedef;
import script.LRterminals.WordTypetostr;
import script.LRterminals.WordUnset;
import script.LRterminals.WordWhile;
import script.ParseError;
import script.Script;
import script.SourceObject;
import script.SyntaxError;
import script.Token;
import script.TokenizedCode;
import script.WhitespaceToken;
import script.names.NameResolver;

public class ScannedCode {
    private static Pattern regex_externalName = Pattern.compile("\\@[a-zA-Z_]\\w*");
    private static Pattern regex_name = Pattern.compile("[a-zA-Z_]\\w*");
    private static Pattern regex_string = Pattern.compile("\".*\"", 32);
    private static Pattern regex_int = Pattern.compile("(\\+|\\-)?[0-9]+");
    private static Pattern regex_float = Pattern.compile("(\\+|\\-)?([0-9]*\\.[0-9]+)|([0-9]+\\.[0-9]*)");
    private static Pattern regex_incdecop = Pattern.compile("(\\+\\+)|(\\-\\-)");
    private static Pattern regex_binop = Pattern.compile("\\+|\\-");
    private static Pattern regex_pointbinop = Pattern.compile("\\*|\\/|\\%");
    private static Pattern regex_comp = Pattern.compile("((\\>|\\<|\\=|\\!)\\=)");
    private static Pattern regex_openSharpBracket = Pattern.compile("\\<");
    private static Pattern regex_closeSharpBracket = Pattern.compile("\\>");
    private static Pattern regex_unop = Pattern.compile("\\$|\\!");
    private static Pattern regex_openarray = Pattern.compile("\\[");
    private static Pattern regex_closearray = Pattern.compile("\\]");
    private static Pattern regex_bbinop = Pattern.compile("(\\&\\&)|(\\|\\|)");
    private static Pattern regex_assign = Pattern.compile("((\\+)|(\\-)|(\\*)|(\\/)|(\\%))?(\\=)");
    private static Pattern regex_semi = Pattern.compile("\\;");
    private static Pattern regex_comma = Pattern.compile("\\,");
    private static Pattern regex_point = Pattern.compile("\\.");
    private static Pattern regex_colon = Pattern.compile("\\:");
    private static Pattern regex_arrow = Pattern.compile("=>");
    private static Pattern regex_derefer = Pattern.compile("\\&");
    private static Pattern regex_question = Pattern.compile("\\?");
    private static Pattern regex_bracket = Pattern.compile("\\(|\\)");
    private static Pattern regex_scriptbracket = Pattern.compile("\\{|\\}");
    private static Pattern regex_bool = Pattern.compile("(true)|(false)");
    private static Pattern regex_if = Pattern.compile("if");
    private static Pattern regex_else = Pattern.compile("else");
    private static Pattern regex_while = Pattern.compile("while");
    private static Pattern regex_unset = Pattern.compile("unset");
    private static Pattern regex_null = Pattern.compile("null");
    private static Pattern regex_break = Pattern.compile("break");
    private static Pattern regex_trap = Pattern.compile("trap");
    private static Pattern regex_for = Pattern.compile("for");
    private static Pattern regex_this = Pattern.compile("this");
    private static Pattern regex_native = Pattern.compile("native");
    private static Pattern regex_extern = Pattern.compile("extern");
    private static Pattern regex_continue = Pattern.compile("continue");
    private static Pattern regex_namespace = Pattern.compile("namespace");
    private static Pattern regex_return = Pattern.compile("return");
    private static Pattern regex_type = Pattern.compile("((v|V)oid)|((v|V)ar)|((s|S)tring)|((i|I)nt)|((f|F)loat)|((b|B)ool)|((a|A)rray)|((o|O)bject)|((s|S)truct)|((c|C)ontainer)");
    private static Pattern regex_typemod = Pattern.compile("global");
    private static Pattern regex_typedef = Pattern.compile("typedef");
    private static Pattern regex_typ = Pattern.compile("type");
    private static Pattern regex_strtotype = Pattern.compile("strtotype");
    private static Pattern regex_typetostr = Pattern.compile("typetostr");
    private static Pattern regex_call = Pattern.compile("call");
    private static Pattern regex_extends = Pattern.compile("extends");
    private static Pattern regex_instanceof = Pattern.compile("instanceof");
    private static Pattern regex_try = Pattern.compile("try");
    private static Pattern regex_catch = Pattern.compile("catch");
    private HashSet<String> userDefTypes;
    protected LinkedList<Token> tokens;

    public ScannedCode(Script owner, TokenizedCode input) throws SyntaxError, ParseError {
        ListIterator<Token> it = input.tokens.listIterator();
        this.tokens = input.tokens;
        this.userDefTypes = owner.getNameResolver().getUserDefTypes();
        boolean nextTokenIsTypeName = false;
        boolean ifNextTokenIsTypeNameThenFollowedByTypeName = false;
        while (it.hasNext()) {
            Token pred;
            Matcher m;
            Token t = it.next();
            if (t instanceof WhitespaceToken) {
                it.remove();
                continue;
            }
            if (nextTokenIsTypeName) {
                m = regex_name.matcher(t.getCode());
                if (!m.matches()) {
                    throw new SyntaxError("The word \"struct\" or \"typedef\" must be followed by a valid identifier, but it is followed by \"" + t.getCode() + "\"", t.getLine(), t.getSourceObject());
                }
                this.userDefTypes.add(t.getCode());
                owner.getNameResolver().addTypeDefinition(t.getCode(), new DummyTypeDefinition(t.getCode()));
                nextTokenIsTypeName = false;
                ifNextTokenIsTypeNameThenFollowedByTypeName = true;
            } else if (ifNextTokenIsTypeNameThenFollowedByTypeName) {
                m = regex_comma.matcher(t.getCode());
                if (m.matches()) {
                    nextTokenIsTypeName = true;
                }
                ifNextTokenIsTypeNameThenFollowedByTypeName = false;
            }
            m = regex_string.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new StringConstant(t));
                continue;
            }
            m = regex_type.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new SimpleTypeName(t));
                continue;
            }
            m = regex_typetostr.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordTypetostr(t));
                continue;
            }
            m = regex_typ.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordType(t));
                continue;
            }
            m = regex_call.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordCall(t));
                continue;
            }
            m = regex_if.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordIf(t));
                continue;
            }
            m = regex_else.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordElse(t));
                continue;
            }
            m = regex_extends.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordExtends(t));
                continue;
            }
            m = regex_strtotype.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordStrtotype(t));
                continue;
            }
            m = regex_while.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordWhile(t));
                continue;
            }
            m = regex_for.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordFor(t));
                continue;
            }
            m = regex_trap.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordTrap(t));
                continue;
            }
            m = regex_extern.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordExtern(t));
                continue;
            }
            m = regex_break.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordBreak(t));
                continue;
            }
            m = regex_return.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordReturn(t));
                continue;
            }
            m = regex_try.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordTry(t));
                continue;
            }
            m = regex_catch.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordCatch(t));
                continue;
            }
            m = regex_instanceof.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordInstanceof(t));
                continue;
            }
            m = regex_typedef.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordTypedef(t));
                nextTokenIsTypeName = true;
                continue;
            }
            m = regex_continue.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordContinue(t));
                continue;
            }
            m = regex_null.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordNull(t));
                continue;
            }
            m = regex_typemod.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new TypeModifier(t));
                continue;
            }
            m = regex_bool.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new BoolConstant(t));
                continue;
            }
            m = regex_this.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordThis(t));
                continue;
            }
            m = regex_native.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordNative(t));
                continue;
            }
            m = regex_unset.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordUnset(t));
                continue;
            }
            m = regex_namespace.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new WordNamespace(t));
                continue;
            }
            if (this.userDefTypes.contains(t.getCode())) {
                it.remove();
                it.add(new SimpleTypeName(t));
                continue;
            }
            m = regex_externalName.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new ExternalIdentifyer(t));
                continue;
            }
            m = regex_name.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Identifyer(t));
                continue;
            }
            m = regex_int.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new IntConstant(t));
                continue;
            }
            m = regex_float.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new FloatConstant(t));
                continue;
            }
            m = regex_incdecop.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new IncDecOp(t));
                continue;
            }
            m = regex_unop.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new UnOp(t));
                continue;
            }
            m = regex_binop.matcher(t.getCode());
            if (m.matches()) {
                it.previous();
                pred = it.previous();
                it.next();
                it.next();
                if (pred instanceof Assignment || pred instanceof BBinOp || pred instanceof BinOp || pred instanceof CloseScriptBracket || pred instanceof Colon || pred instanceof Comma || pred instanceof Comparator || pred instanceof OpenArrayBracket || pred instanceof OpenBracket || pred instanceof OpenScriptBracket || pred instanceof Point || pred instanceof PointBinOp || pred instanceof Questionmark || pred instanceof Semicolon || pred instanceof UnOp || pred instanceof WordElse || pred instanceof WordReturn) {
                    it.remove();
                    it.add(new UnOp(t));
                    continue;
                }
                it.remove();
                it.add(new BinOp(t));
                continue;
            }
            m = regex_pointbinop.matcher(t.getCode());
            if (m.matches()) {
                if (t.getCode().equals("*")) {
                    it.previous();
                    pred = it.previous();
                    it.next();
                    it.next();
                    if (pred instanceof Assignment || pred instanceof BBinOp || pred instanceof BinOp || pred instanceof CloseScriptBracket || pred instanceof Colon || pred instanceof Comma || pred instanceof Comparator || pred instanceof OpenArrayBracket || pred instanceof OpenBracket || pred instanceof OpenScriptBracket || pred instanceof Point || pred instanceof PointBinOp || pred instanceof DereferOperator || pred instanceof CloseSharpBracket || pred instanceof Questionmark || pred instanceof Semicolon || pred instanceof UnOp || pred instanceof WordElse || pred instanceof TypeName || pred instanceof WordReturn) {
                        it.remove();
                        it.add(new DereferOperator(t));
                        continue;
                    }
                    it.remove();
                    it.add(new PointBinOp(t));
                    continue;
                }
                it.remove();
                it.add(new PointBinOp(t));
                continue;
            }
            m = regex_comp.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Comparator(t));
                continue;
            }
            m = regex_bbinop.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new BBinOp(t));
                continue;
            }
            m = regex_assign.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Assignment(t));
                continue;
            }
            m = regex_semi.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Semicolon(t));
                continue;
            }
            m = regex_colon.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Colon(t));
                continue;
            }
            m = regex_arrow.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Arrow(t));
                continue;
            }
            m = regex_derefer.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new ReferOperator(t));
                continue;
            }
            m = regex_question.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Questionmark(t));
                continue;
            }
            m = regex_comma.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Comma(t));
                continue;
            }
            m = regex_point.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new Point(t));
                continue;
            }
            m = regex_openarray.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new OpenArrayBracket(t));
                continue;
            }
            m = regex_closearray.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new CloseArrayBracket(t));
                continue;
            }
            m = regex_bracket.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                if (t.getCode().equals("(")) {
                    it.add(new OpenBracket(t));
                    continue;
                }
                it.add(new CloseBracket(t));
                continue;
            }
            m = regex_openSharpBracket.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new OpenSharpBracket(t));
                continue;
            }
            m = regex_closeSharpBracket.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                it.add(new CloseSharpBracket(t));
                continue;
            }
            m = regex_scriptbracket.matcher(t.getCode());
            if (m.matches()) {
                it.remove();
                if (t.getCode().equals("{")) {
                    it.add(new OpenScriptBracket(t));
                    continue;
                }
                it.add(new CloseScriptBracket(t));
                continue;
            }
            throw new SyntaxError("\"" + t.getCode() + "\" is an unknown token!", t.line, t.source);
        }
    }
}

