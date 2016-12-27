/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.Line;
import script.PreprocessedCode;
import script.Script;
import script.SourceObject;
import script.SyntaxError;
import script.Token;
import script.UnknownToken;
import script.WhitespaceToken;

public class TokenizedCode {
    private static Pattern regex_whitespace = Pattern.compile("(\\s)+");
    private static Pattern regex_number = Pattern.compile("[0-9]+");
    private Script owner;
    protected LinkedList<Token> tokens = new LinkedList();
    private boolean escaped = false;
    private boolean lastWasSlash = false;
    private boolean inStarComment = false;
    private boolean inString = false;
    private boolean possibleStarEnd = false;
    private StringBuilder curString;
    private int curStringStart;

    private void tokenizeLine(Line l) throws SyntaxError {
        StringTokenizer st = new StringTokenizer(l.content, "+-/*()?!<>=:;.&|{}[]\"\\%,$ \n\r\t#", true);
        String s = "";
        this.escaped = false;
        this.lastWasSlash = false;
        while (st.hasMoreTokens()) {
            String t;
            s = st.nextToken();
            if (this.inStarComment) {
                this.curString.append(s);
                if (this.possibleStarEnd && s.equals("/")) {
                    this.possibleStarEnd = false;
                    this.inStarComment = false;
                    this.tokens.add(new WhitespaceToken(this.owner, this.curString.toString(), l.num, l.source));
                    continue;
                }
                if (s.equals("*")) {
                    this.possibleStarEnd = true;
                    continue;
                }
                this.possibleStarEnd = false;
                continue;
            }
            if (this.inString) {
                if (!this.escaped) {
                    if (s.equals("\"")) {
                        this.tokens.add(new UnknownToken(this.owner, "\"" + this.curString.toString() + "\"", this.curStringStart, l.source));
                        this.inString = false;
                        continue;
                    }
                    if (s.equals("\\")) {
                        this.escaped = true;
                        continue;
                    }
                    this.curString.append(s);
                    this.escaped = false;
                    continue;
                }
                if (s.equals("\\") || s.equals("\"")) {
                    this.curString.append(s);
                } else if (s.startsWith("n") || s.startsWith("r")) {
                    this.curString.append("\r\n");
                    this.curString.append(s.substring(1));
                } else if (s.startsWith("0")) {
                    this.curString.append("\u0000");
                    this.curString.append(s.substring(1));
                } else if (s.startsWith("t")) {
                    this.curString.append("\t");
                    this.curString.append(s.substring(1));
                } else {
                    throw new SyntaxError("Unrecognized escape sequence in string: \"\\" + s + "\"", l.num, l.source);
                }
                this.escaped = false;
                continue;
            }
            if (!this.inString && s.equals("*") && this.lastWasSlash) {
                this.tokens.removeLast();
                this.curString = new StringBuilder();
                this.curString.append("/*");
                this.inStarComment = true;
                this.lastWasSlash = false;
                continue;
            }
            if (!this.inString && s.equals("/") && this.lastWasSlash) {
                this.tokens.removeLast();
                this.curString = new StringBuilder();
                this.curString.append("//");
                while (st.hasMoreTokens()) {
                    this.curString.append(st.nextToken());
                }
                this.tokens.add(new WhitespaceToken(this.owner, this.curString.toString(), l.num, l.source));
                this.lastWasSlash = false;
                break;
            }
            if (s.equals("\"")) {
                this.curString = new StringBuilder();
                this.curStringStart = l.num;
                this.inString = true;
                continue;
            }
            this.lastWasSlash = s.equals("/");
            if (regex_whitespace.matcher(s).matches()) {
                this.tokens.add(new WhitespaceToken(this.owner, s, l.num, l.source));
                continue;
            }
            if (s.equals("=")) {
                t = this.tokens.getLast().code;
                if (t.equals("<") || t.equals(">") || t.equals("=") || t.equals("!") || t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/") || t.equals("%")) {
                    this.tokens.removeLast();
                    s = String.valueOf(t) + "=";
                }
            } else if (s.equals("&") || s.equals("|") || s.equals("+") || s.equals("-")) {
                t = this.tokens.getLast().code;
                if (s.equals(t)) {
                    this.tokens.removeLast();
                    s = String.valueOf(s) + s;
                }
            } else if (s.equals(">") && (t = this.tokens.getLast().code).equals("=")) {
                this.tokens.removeLast();
                s = "=>";
            }
            this.tokens.add(new UnknownToken(this.owner, s, l.num, l.source));
        }
        this.tokens.add(new WhitespaceToken(this.owner, "\n", l.num, l.source));
    }

    private void recognizeFloats() {
        ListIterator<Token> it = this.tokens.listIterator();
        while (it.hasNext()) {
            Token t2;
            Token t = it.next();
            if (t instanceof WhitespaceToken || !t.getCode().equals(".")) continue;
            if (it.hasNext() && !((t2 = it.next()) instanceof WhitespaceToken) && regex_number.matcher(t2.code).matches()) {
                it.remove();
                t = it.previous();
                t.code = String.valueOf(t.code) + t2.code;
                it.next();
            }
            t = it.previous();
            if (!it.hasPrevious()) continue;
            t2 = it.previous();
            if (!(t2 instanceof WhitespaceToken) && regex_number.matcher(t2.code).matches()) {
                it.remove();
                t = it.next();
                t.code = String.valueOf(t2.code) + t.code;
                continue;
            }
            t = it.next();
        }
    }

    public TokenizedCode(Script owner, PreprocessedCode input) throws SyntaxError {
        this.owner = owner;
        for (Line l : input.lines) {
            this.tokenizeLine(l);
        }
        this.recognizeFloats();
    }
}

