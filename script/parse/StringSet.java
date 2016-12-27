/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.LinkedList;

public class StringSet
extends LinkedList<String> {
    private static final long serialVersionUID = 1;

    @Override
    public boolean add(String ss) {
        for (String s : this) {
            if (!s.equals(ss)) continue;
            return false;
        }
        return super.add(ss);
    }
}

