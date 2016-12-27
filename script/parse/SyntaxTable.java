/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.ParseError;
import script.parse.Aktion;
import script.parse.Grammar;
import script.parse.Production;
import script.parse.StringSet;

public class SyntaxTable {
    public HashMap<Integer, ActionTable> aktion = new HashMap();
    public HashMap<Integer, HashMap<String, Integer>> sprung = new HashMap();
    private Grammar g;
    public int Anfangszustand = -1;
    private Pattern regex_num = Pattern.compile("[0-9]+");

    public SyntaxTable(Grammar g) {
        this.g = g;
        this.Anfangszustand = 0;
    }

    public Production getProduction(int num) {
        return (Production)this.g.productions.get(num);
    }

    public Aktion getAktion(int zustand, String input) throws ParseError {
        if (!this.aktion.containsKey(zustand)) {
            throw new ParseError("OMFG DLSDJFK Tyax table");
        }
        ActionTable a = this.aktion.get(zustand);
        if (a.refer != -1) {
            return this.getAktion(a.refer, input);
        }
        if (!this.aktion.get(zustand).containsKey(input)) {
            return new Aktion(3);
        }
        return (Aktion)a.get(input);
    }

    public int getSprung(int zustand, String input) throws ParseError {
        if (!this.sprung.containsKey(zustand)) {
            throw new ParseError("OMFG DLSDJFK 2222Tyax table");
        }
        if (!this.sprung.get(zustand).containsKey(input)) {
            throw new ParseError("OMFG DLSDJFK 3333Tyax table");
        }
        return this.sprung.get(zustand).get(input);
    }

    private String getW(int length) {
        String result = "";
        int i = 0;
        while (i < length) {
            result = String.valueOf(result) + " ";
            ++i;
        }
        return result;
    }

    private String getM(int length) {
        String result = "";
        int i = 0;
        while (i < length) {
            result = String.valueOf(result) + "-";
            ++i;
        }
        return result;
    }

    private String getCentered(String s, int length) {
        if (s.length() >= length) {
            return s;
        }
        int diff = length - s.length();
        return String.valueOf(this.getW(diff / 2)) + s + this.getW((diff + 1) / 2);
    }

    public int getMaxZustand() {
        int max = 0;
        Iterator<Integer> iterator = this.aktion.keySet().iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            if (i <= max) continue;
            max = i;
        }
        return max;
    }

    private StringSet getTerms() {
        StringSet terms = new StringSet();
        Iterator<Integer> iterator = this.aktion.keySet().iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            HashMap a = this.aktion.get(i);
            for (String s : a.keySet()) {
                terms.add(s);
            }
        }
        return terms;
    }

    public HashMap<Integer, Integer> lineEquals() {
        int max = this.getMaxZustand();
        StringSet terms = this.getTerms();
        HashMap<String, Integer> equalmap = new HashMap<String, Integer>();
        HashMap<Integer, Integer> equl = new HashMap<Integer, Integer>();
        int i = 0;
        while (i <= max) {
            HashMap curLine = this.aktion.get(i);
            String hashString = "";
            for (String s : terms) {
                if (curLine == null || s == null) {
                    System.out.println();
                }
                hashString = !curLine.containsKey(s) ? String.valueOf(hashString) + "-" : String.valueOf(hashString) + ((Aktion)curLine.get(s)).toString();
            }
            if (equalmap.containsKey(hashString)) {
                equl.put(i, (Integer)equalmap.get(hashString));
            } else {
                equalmap.put(hashString, i);
            }
            ++i;
        }
        return equl;
    }

    public String toString() {
        Object a;
        StringBuilder result = new StringBuilder();
        int numZusta = this.aktion.size();
        int longestTerm = 0;
        int longestNonTerm = 0;
        StringSet terms = new StringSet();
        Iterator<Integer> iterator = this.aktion.keySet().iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            a = this.aktion.get(i);
            for (String s : a.keySet()) {
                terms.add(s);
                if (s.length() <= longestTerm) continue;
                longestTerm = s.length();
            }
        }
        StringSet nonterms = new StringSet();
        a = this.sprung.keySet().iterator();
        while (a.hasNext()) {
            int i = (Integer)a.next();
            Iterator a2 = this.sprung.get(i);
            for (String s : a2.keySet()) {
                nonterms.add(s);
                if (s.length() <= longestNonTerm) continue;
                longestNonTerm = s.length();
            }
        }
        result.append("|" + this.getCentered("Zustand", 9) + "|" + this.getCentered("Aktion", terms.size() * (longestTerm += 2)) + "|" + this.getCentered("Sprung", nonterms.size() * (longestNonTerm += 2)) + "|");
        int lineLength = result.toString().length();
        result = new StringBuilder();
        result.append("|" + this.getM(lineLength - 2) + "|\n" + result);
        result.append("\n|");
        result.append(String.valueOf(this.getW(9)) + "|");
        for (String s : terms) {
            result.append(this.getCentered(s, longestTerm));
        }
        result.append("|");
        for (String s : nonterms) {
            result.append(this.getCentered(s, longestNonTerm));
        }
        result.append("|\n|");
        result.append(String.valueOf(this.getM(lineLength - 2)) + "|\n|");
        int i = 0;
        while (i < numZusta) {
            result.append(String.valueOf(this.getCentered(new StringBuilder().append(i).toString(), 9)) + "|");
            HashMap curRow = this.aktion.get(i);
            for (String s : terms) {
                if (curRow.containsKey(s)) {
                    result.append(this.getCentered(((Aktion)curRow.get(s)).toString(), longestTerm));
                    continue;
                }
                result.append(this.getW(longestTerm));
            }
            result.append("|");
            HashMap<String, Integer> curRow2 = this.sprung.get(i);
            for (String s : nonterms) {
                if (curRow2.containsKey(s)) {
                    result.append(this.getCentered(curRow2.get(s) == 0 ? "" : curRow2.get(s).toString(), longestNonTerm));
                    continue;
                }
                result.append(this.getW(longestNonTerm));
            }
            result.append("|\n|");
            ++i;
        }
        result.append(String.valueOf(this.getM(lineLength - 2)) + "|");
        return result.toString();
    }

    public void toFile() {
        try {
            HashMap<String, Integer> row;
            Object a;
            Iterator<Integer> row2;
            BufferedWriter w = new BufferedWriter(new FileWriter(new File("misc/syntax.slr")));
            HashMap<String, Integer> tokenNumMapping = new HashMap<String, Integer>();
            int num = 0;
            for (Integer i : this.aktion.keySet()) {
                row2 = this.aktion.get(i);
                for (String s : row2.keySet()) {
                    if (tokenNumMapping.containsKey(s)) continue;
                    tokenNumMapping.put(s, num);
                    w.write(String.valueOf(s) + "\n");
                    ++num;
                }
            }
            for (Integer i : this.sprung.keySet()) {
                row2 = this.sprung.get(i);
                for (String s : row2.keySet()) {
                    if (tokenNumMapping.containsKey(s)) continue;
                    tokenNumMapping.put(s, num);
                    w.write(String.valueOf(s) + "\n");
                    ++num;
                }
            }
            HashMap<Integer, Integer> equalmap = this.lineEquals();
            w.write("!\n");
            for (Integer i : this.aktion.keySet()) {
                row = this.aktion.get(i);
                if (equalmap.containsKey(i)) {
                    w.write("-" + i + "=" + equalmap.get(i) + "\n");
                    continue;
                }
                w.write("-" + i + "\n");
                for (String s : row.keySet()) {
                    a = (Aktion)((Object)row.get(s));
                    w.write(tokenNumMapping.get(s) + ">" + a.type + ":" + a.zustand + "\n");
                }
            }
            w.write("!\n");
            for (Integer i : this.sprung.keySet()) {
                row = this.sprung.get(i);
                w.write("-" + i + "\n");
                for (String s : row.keySet()) {
                    a = row.get(s);
                    w.write(tokenNumMapping.get(s) + ">" + a + "\n");
                }
            }
            w.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromFile(File f) throws IOException {
        Matcher m;
        String curLine;
        BufferedReader r = new BufferedReader(new FileReader(f));
        HashMap<Integer, String> tokenNumMapping = new HashMap<Integer, String>();
        int num = 0;
        while ((curLine = r.readLine()) != null) {
            if (curLine.equals("!")) break;
            tokenNumMapping.put(num, curLine);
            ++num;
        }
        int curZustand = -1;
        while ((curLine = r.readLine()) != null) {
            if (curLine.equals("!")) break;
            m = this.regex_num.matcher(curLine);
            if (curLine.startsWith("-")) {
                m.find();
                curZustand = Integer.parseInt(m.group());
                if (curLine.contains("=")) {
                    m.find();
                    int equal = Integer.parseInt(m.group());
                    this.aktion.put(curZustand, new ActionTable(equal));
                    continue;
                }
                this.aktion.put(curZustand, new ActionTable());
                continue;
            }
            m.find();
            String inputToken = (String)tokenNumMapping.get(Integer.parseInt(m.group()));
            m.find();
            int type = Integer.parseInt(m.group());
            m.find();
            int zustand = Integer.parseInt(m.group());
            Aktion a = new Aktion(type, zustand);
            this.aktion.get(curZustand).put(inputToken, a);
        }
        while ((curLine = r.readLine()) != null) {
            m = this.regex_num.matcher(curLine);
            if (curLine.startsWith("-")) {
                m.find();
                curZustand = Integer.parseInt(m.group());
                this.sprung.put(curZustand, new HashMap());
                continue;
            }
            m.find();
            String inputToken = (String)tokenNumMapping.get(Integer.parseInt(m.group()));
            m.find();
            int zustand = Integer.parseInt(m.group());
            this.sprung.get(curZustand).put(inputToken, zustand);
        }
        r.close();
    }

    public void printZust(int i) {
        ActionTable a = this.aktion.get(i);
        for (String s : a.keySet()) {
            try {
                Aktion aa = this.getAktion(i, s);
                System.out.println(String.valueOf(s) + " -> " + aa);
                continue;
            }
            catch (ParseError e) {
                e.printStackTrace();
            }
        }
    }

    public static class ActionTable
    extends HashMap<String, Aktion> {
        private static final long serialVersionUID = 1;
        int refer = -1;

        public ActionTable(int i) {
            this.refer = i;
        }

        public ActionTable() {
        }

        public int getRefer() {
            return this.refer;
        }
    }

}

