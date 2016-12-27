/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import script.ParseError;
import script.Token;
import script.parse.Aktion;
import script.parse.EOF;
import script.parse.ErrorMessageGenerator;
import script.parse.Grammar;
import script.parse.LR_Element;
import script.parse.LR_ElementSet;
import script.parse.LR_ElementSetSet;
import script.parse.Parser;
import script.parse.Production;
import script.parse.SyntaxTable;
import script.parse.TokenClassSet;

public class ParseGenerator {
    private Grammar grammar;
    private HashMap<String, TokenClassSet> followMap = new HashMap();

    private boolean addToFollowMap(String key, TokenClassSet t) {
        if (!this.followMap.containsKey(key)) {
            this.followMap.put(key, new TokenClassSet());
        }
        return this.followMap.get(key).addAll(t);
    }

    public TokenClassSet first(Class<? extends Token> token, LinkedList<Production> excludes) throws InstantiationException, IllegalAccessException {
        TokenClassSet result = new TokenClassSet();
        if (this.grammar.isTerminal(token)) {
            result.add(token);
            return result;
        }
        block0 : for (Production p : this.grammar.productions) {
            if (!p.lValue.equals(token)) continue;
            boolean wantCont = false;
            for (Production excl : excludes) {
                if (!excl.equals(p)) continue;
                wantCont = true;
                break;
            }
            if (wantCont) continue;
            boolean containsEmptyWord = true;
            for (Production.ProductionEntry e : p.rValue) {
                if (!containsEmptyWord || e.wantedClass.equals(token)) continue block0;
                excludes.add(p);
                TokenClassSet curSet = this.first(e.wantedClass, excludes);
                if (!curSet.containsEmptyWord()) {
                    containsEmptyWord = false;
                }
                result.addAll(curSet);
            }
        }
        return result;
    }

    public TokenClassSet first(Class<? extends Token> token) throws InstantiationException, IllegalAccessException {
        return this.first(token, new LinkedList<Production>());
    }

    private void generateFollow() throws InstantiationException, IllegalAccessException {
        TokenClassSet t = new TokenClassSet();
        t.add(EOF.class);
        this.addToFollowMap(this.grammar.startProduction.lValue.getSimpleName(), t);
        boolean changes = true;
        while (changes) {
            ListIterator<Production.ProductionEntry> iter;
            TokenClassSet toAdd;
            Production.ProductionEntry pe2;
            Production.ProductionEntry pe;
            changes = false;
            block1 : for (Production p : this.grammar.productions) {
                iter = p.rValue.listIterator();
                while (iter.hasNext()) {
                    pe = iter.next();
                    if (!iter.hasNext()) continue block1;
                    if (this.grammar.isTerminal(pe.wantedClass)) continue;
                    pe2 = iter.next();
                    iter.previous();
                    toAdd = this.first(pe2.wantedClass);
                    toAdd.removeEmptyWord();
                    if (toAdd.isEmpty() || !this.addToFollowMap(pe.wantedClass.getSimpleName(), toAdd)) continue;
                    changes = true;
                }
            }
            for (Production p : this.grammar.productions) {
                iter = p.rValue.listIterator();
                while (iter.hasNext()) {
                    pe = iter.next();
                    if (this.grammar.isTerminal(pe.wantedClass)) continue;
                    if (iter.hasNext()) {
                        pe2 = iter.next();
                        iter.previous();
                        if (!this.first(pe2.wantedClass).containsEmptyWord() || (toAdd = this.followMap.get(p.lValue.getSimpleName())) == null || toAdd.isEmpty() || !this.addToFollowMap(pe.wantedClass.getSimpleName(), toAdd)) continue;
                        changes = true;
                        continue;
                    }
                    TokenClassSet toAdd2 = this.followMap.get(p.lValue.getSimpleName());
                    if (toAdd2 == null || toAdd2.isEmpty() || !this.addToFollowMap(pe.wantedClass.getSimpleName(), toAdd2)) continue;
                    changes = true;
                }
            }
        }
    }

    public TokenClassSet follow(Class<? extends Token> token) throws InstantiationException, IllegalAccessException, ParseError {
        if (this.grammar.isTerminal(token)) {
            throw new ParseError("FOLLOW WTF " + token.getSimpleName());
        }
        return this.followMap.get(token.getSimpleName());
    }

    public LR_ElementSet Sprung(LR_ElementSet I, Class<? extends Token> X) throws InstantiationException, IllegalAccessException {
        LR_ElementSet swap = new LR_ElementSet();
        for (LR_Element e : I.elements) {
            if (!e.hasRight() || !e.getRight().wantedClass.equals(X)) continue;
            swap.add(new LR_Element(e.production, e.index + 1));
        }
        return this.Huelle(swap);
    }

    public LR_ElementSet Huelle(LR_ElementSet I) throws InstantiationException, IllegalAccessException {
        LR_ElementSet result = new LR_ElementSet();
        result.addAll(I);
        LR_ElementSet swap = new LR_ElementSet();
        boolean changes = true;
        while (changes) {
            changes = false;
            for (LR_Element current : result.elements) {
                if (!current.hasRight() || this.grammar.isTerminal(current.getRight().wantedClass)) continue;
                for (Production p : this.grammar.productions) {
                    if (!p.lValue.equals(current.getRight().wantedClass) || !swap.add(new LR_Element(p, 0))) continue;
                    changes = true;
                }
            }
            if (!changes) continue;
            changes = result.addAll(swap);
        }
        return result;
    }

    public TokenClassSet getTokenListFromGrammar() throws InstantiationException, IllegalAccessException {
        TokenClassSet result = new TokenClassSet();
        for (Production p : this.grammar.productions) {
            result.add(p.lValue);
            for (Production.ProductionEntry c : p.rValue) {
                result.add(c.wantedClass);
            }
        }
        return result;
    }

    public LR_ElementSetSet KanonischeSammlung() throws InstantiationException, IllegalAccessException {
        LR_ElementSetSet C = new LR_ElementSetSet();
        LR_ElementSetSet swap = new LR_ElementSetSet();
        TokenClassSet symbols = this.getTokenListFromGrammar();
        C.add(this.Huelle(new LR_ElementSet(new LR_Element((Production)this.grammar.productions.get(0), 0))));
        boolean changes = true;
        while (changes) {
            changes = false;
            for (LR_ElementSet I : C.elements) {
                for (Class<? extends Token> X : symbols.tokens) {
                    LR_ElementSet sprung = this.Sprung(I, X);
                    if (sprung.isEmpty()) continue;
                    swap.add(sprung);
                    changes = true;
                }
            }
            if (!changes) continue;
            changes = C.addAll(swap);
            swap.clear();
        }
        return C;
    }

    public SyntaxTable generateSyntaxTable(LR_ElementSetSet C) throws InstantiationException, IllegalAccessException, ParseError, ConflictException {
        TokenClassSet sigma = this.getTokenListFromGrammar();
        SyntaxTable table = new SyntaxTable(this.grammar);
        LinkedList<String> conflicts = new LinkedList<String>();
        LR_Element accept = new LR_Element(this.grammar.startProduction, 1);
        int count = 0;
        for (LR_ElementSet s : C.elements) {
            String key;
            boolean hasFound;
            int count2;
            LR_ElementSet sprung;
            System.out.println("*********** MENGE " + count + " ****************");
            for (LR_Element e : s.elements) {
                if (!e.hasRight() || !this.grammar.isTerminal(e.getRight().wantedClass)) continue;
                sprung = this.Sprung(s, e.getRight().wantedClass);
                count2 = 0;
                hasFound = false;
                for (LR_ElementSet s2 : C.elements) {
                    if (s2.equals(sprung)) {
                        hasFound = true;
                        break;
                    }
                    ++count2;
                }
                if (!hasFound) {
                    throw new ParseError("WTF Tabbbb");
                }
                if (!table.aktion.containsKey(count)) {
                    table.aktion.put(count, new SyntaxTable.ActionTable());
                }
                key = e.getRight().wantedClass.getSimpleName();
                if (table.aktion.get(count).containsKey(key)) {
                    if (((Aktion)table.aktion.get((Object)Integer.valueOf((int)count)).get((Object)key)).zustand == count2) continue;
                    conflicts.add("WARNING: Schiebe schiebe konflikt " + count + " " + key + ":" + count2 + ((Aktion)table.aktion.get((Object)Integer.valueOf((int)count)).get((Object)key)).zustand);
                    continue;
                }
                table.aktion.get(count).put(key, new Aktion(0, count2));
            }
            for (LR_Element e : s.elements) {
                if (e.production.lValue.equals(this.grammar.startProduction.lValue) || e.hasRight()) continue;
                TokenClassSet set = this.follow(e.production.lValue);
                for (Class<? extends Token> t : set.tokens) {
                    if (!table.aktion.containsKey(count)) {
                        table.aktion.put(count, new SyntaxTable.ActionTable());
                    }
                    key = t.getSimpleName();
                    if (table.aktion.get(count).containsKey(key)) {
                        String prio;
                        Aktion a = (Aktion)table.aktion.get(count).get(key);
                        if (a.type == 1) {
                            int oldPriority = ((Production)this.grammar.productions.get(a.zustand)).getPriority();
                            int newPriority = e.production.getPriority();
                            if (oldPriority == -1 || newPriority == -1) {
                                conflicts.add("Reduziere Reduziere konflikt:" + this.grammar.productions.get(a.zustand) + " ? " + e.production);
                            }
                            System.out.println("Reduziere Reduziere konflikt:" + this.grammar.productions.get(a.zustand) + " ? " + e.production);
                            prio = "O: " + oldPriority + " N: " + newPriority;
                            if (oldPriority > newPriority) {
                                System.out.println(String.valueOf(prio) + " --> bevorzuge " + this.grammar.productions.get(a.zustand));
                                continue;
                            }
                            System.out.println(String.valueOf(prio) + " --> bevorzuge " + e.production);
                            table.aktion.get(count).put(key, new Aktion(1, e.production.num));
                            continue;
                        }
                        if (a.type == 0) {
                            int tokenPriority = this.grammar.getTokenPriority(t);
                            int productionPriority = e.production.getPriority();
                            if (tokenPriority == -1 || productionPriority == -1) {
                                conflicts.add("Schiebe Reduziere konflikt:" + count + "\n" + key + " ? " + e.production);
                            }
                            System.out.println("Schiebe Reduziere konflikt:" + count + "\n" + key + " ? " + e.production);
                            prio = "T: " + tokenPriority + " P: " + productionPriority;
                            if (tokenPriority > productionPriority) {
                                System.out.println(String.valueOf(prio) + " --> bevorzuge schiebe");
                                continue;
                            }
                            System.out.println(String.valueOf(prio) + " --> bevorzuge reduziere");
                            table.aktion.get(count).put(key, new Aktion(1, e.production.num));
                            continue;
                        }
                        conflicts.add("WTF konflikt" + count + " " + key + "\n" + a + "\n" + new Aktion(1, e.production.num));
                        continue;
                    }
                    table.aktion.get(count).put(key, new Aktion(1, e.production.num));
                }
            }
            for (LR_Element e : s.elements) {
                if (!e.equals(accept)) continue;
                if (!table.aktion.containsKey(count)) {
                    table.aktion.put(count, new SyntaxTable.ActionTable());
                }
                String key2 = EOF.class.getSimpleName();
                if (table.aktion.get(count).containsKey(key2)) {
                    int type = ((Aktion)table.aktion.get((Object)Integer.valueOf((int)count)).get((Object)key2)).type;
                    if (type == 1) {
                        conflicts.add("WARNING: Redu33ziere Reduziere konflikt");
                    } else if (type == 0) {
                        conflicts.add("WARNING: Schi333ebe Reduziere konflikt");
                    } else {
                        conflicts.add("WARNING: WT333F konflikt");
                    }
                }
                table.aktion.get(count).put(key2, new Aktion(2));
            }
            for (Class t : sigma.tokens) {
                if (this.grammar.isTerminal(t)) continue;
                sprung = this.Sprung(s, t);
                count2 = 0;
                hasFound = false;
                for (LR_ElementSet s2 : C.elements) {
                    if (s2.equals(sprung)) {
                        hasFound = true;
                        break;
                    }
                    ++count2;
                }
                if (!hasFound) {
                    throw new ParseError("WTF Tabbbb");
                }
                if (!table.sprung.containsKey(count)) {
                    table.sprung.put(count, new HashMap());
                }
                key = t.getSimpleName();
                if (table.sprung.get(count).containsKey(key)) {
                    conflicts.add("Sprung konflikt");
                }
                table.sprung.get(count).put(key, count2);
            }
            ++count;
        }
        if (!conflicts.isEmpty()) {
            String message = "\n";
            for (String s : conflicts) {
                message = String.valueOf(message) + s + "\n";
            }
            throw new ConflictException(message);
        }
        System.out.println("--------- DONE ----------");
        return table;
    }

    public Parser generateParser(Grammar g) throws ParserGenerationError {
        this.grammar = g;
        System.out.println(g.productions.get(64));
        try {
            this.generateFollow();
            LR_ElementSetSet C = this.KanonischeSammlung();
            SyntaxTable t = this.generateSyntaxTable(C);
            t.lineEquals();
            t.toFile();
            BufferedWriter w = new BufferedWriter(new FileWriter(new File("misc/table.txt")));
            w.write(t.toString());
            w.close();
            ErrorMessageGenerator.buildErrorTable(t);
            return null;
        }
        catch (Exception e) {
            throw new ParserGenerationError(e);
        }
    }

    public static void main(String[] args) {
        ParseGenerator p = new ParseGenerator();
        try {
            p.generateParser(new Grammar());
        }
        catch (ParserGenerationError e) {
            e.printStackTrace();
        }
    }

    public class ConflictException
    extends Exception {
        private static final long serialVersionUID = 1;

        public ConflictException(String s) {
            super(s);
        }
    }

    public static class ParserGenerationError
    extends Exception {
        private static final long serialVersionUID = 1;

        public ParserGenerationError(String s) {
            super(s);
        }

        public ParserGenerationError(Throwable a) {
            super(a);
        }
    }

}

