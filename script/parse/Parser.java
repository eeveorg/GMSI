/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.ListIterator;
import script.LRterminals.START;
import script.ParseError;
import script.Script;
import script.SourceObject;
import script.SyntaxError;
import script.Token;
import script.parse.Aktion;
import script.parse.EOF;
import script.parse.ErrorMessageTable;
import script.parse.Production;
import script.parse.SyntaxTable;
import script.parse.ZustandsToken;

public class Parser {
    private LinkedList<Token> stack;
    private SyntaxTable syntaxAnalyseTable;
    private ErrorMessageTable errorTable;

    public Parser(SyntaxTable t, ErrorMessageTable e) {
        this.syntaxAnalyseTable = t;
        this.errorTable = e;
    }

    private void printStack(LinkedList<Token> stack) {
        String output = "Stack is ";
        for (Token t : stack) {
            output = String.valueOf(output) + t.toString();
        }
        System.out.println(output);
    }

    public Token parse(LinkedList<Token> input) throws SecurityException, IllegalArgumentException, NoSuchMethodException, ParseError, InstantiationException, IllegalAccessException, InvocationTargetException, SyntaxError {
        input.add(new EOF(input.getLast()));
        input.addFirst(new START(input.getFirst()));
        this.stack = new LinkedList();
        this.stack.add(new ZustandsToken(null, this.syntaxAnalyseTable.Anfangszustand));
        ListIterator<Token> iter = input.listIterator();
        Token curToken = iter.next();
        do {
            int zustand = ((ZustandsToken)this.stack.getLast()).zustand;
            Aktion aktion = this.syntaxAnalyseTable.getAktion(zustand, curToken.getClass().getSimpleName());
            if (aktion.type == 0) {
                this.stack.addLast(curToken);
                this.stack.addLast(new ZustandsToken(null, aktion.zustand));
                curToken = iter.next();
                continue;
            }
            if (aktion.type == 1) {
                this.syntaxAnalyseTable.getProduction(aktion.zustand).reduce(this.stack, this.syntaxAnalyseTable);
                continue;
            }
            if (aktion.type == 2) {
                this.stack.removeLast();
                Token result = this.stack.getLast();
                this.stack.removeLast();
                this.stack.removeLast();
                while (!this.stack.isEmpty()) {
                    System.out.println("---" + this.stack.getLast());
                    this.stack.removeLast();
                }
                return result;
            }
            this.handleError(aktion, curToken, iter, zustand);
        } while (true);
    }

    private void handleError(Aktion a, Token t, ListIterator<Token> inputIter, int zustand) throws SyntaxError {
        Token tt;
        int line = t.getLine();
        String errorLineBefore = "";
        String errorLineAfter = "";
        ListIterator<Token> iter = this.stack.listIterator(this.stack.size());
        while (iter.hasPrevious()) {
            iter.previous();
            if (!iter.hasPrevious() || (tt = iter.previous()).getLine() != line) break;
            errorLineBefore = tt + " " + errorLineBefore;
        }
        while (inputIter.hasNext()) {
            tt = inputIter.next();
            if (tt.getLine() != line) break;
            errorLineAfter = String.valueOf(errorLineAfter) + " " + tt;
        }
        throw new SyntaxError(String.valueOf(errorLineBefore) + "->" + t + "<-" + errorLineAfter + " (" + t.getClass().getSimpleName() + ")\n" + this.errorTable.getErrorMessage(zustand), t.getLine(), t.source);
    }
}

