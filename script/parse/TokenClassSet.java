/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.LinkedList;
import java.util.ListIterator;
import script.Token;
import script.parse.EmptyWord;

public class TokenClassSet {
    public LinkedList<Class<? extends Token>> tokens = new LinkedList();

    public boolean add(Class<? extends Token> toAdd) {
        for (Class<? extends Token> s : this.tokens) {
            if (!s.equals(toAdd)) continue;
            return false;
        }
        return this.tokens.add(toAdd);
    }

    public boolean addAll(TokenClassSet t) {
        boolean change = false;
        for (Class<? extends Token> tt : t.tokens) {
            if (!this.add(tt)) continue;
            change = true;
        }
        return change;
    }

    public String toString() {
        String result = "{";
        for (Class<? extends Token> t : this.tokens) {
            result = String.valueOf(result) + t.getSimpleName();
            if (this.tokens.getLast().equals(t)) continue;
            result = String.valueOf(result) + ",";
        }
        return String.valueOf(result) + "}";
    }

    public boolean containsEmptyWord() {
        for (Class<? extends Token> t : this.tokens) {
            if (!t.equals(EmptyWord.class)) continue;
            return true;
        }
        return false;
    }

    public boolean removeEmptyWord() {
        ListIterator<Class<? extends Token>> iter = this.tokens.listIterator();
        while (iter.hasNext()) {
            Class<? extends Token> t = iter.next();
            if (!t.equals(EmptyWord.class)) continue;
            iter.remove();
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.tokens.isEmpty();
    }
}

