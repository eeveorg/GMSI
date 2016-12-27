/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.List;
import java.util.ListIterator;

public class Tools {
    public static String addIndent(String base, String indent) {
        base = base.replaceAll("\n", "\n" + indent);
        return String.valueOf(indent) + base;
    }

    public static String listToString(List l, String start, String end, String seperator) {
        StringBuilder b = new StringBuilder();
        b.append(start);
        ListIterator it = l.listIterator();
        while (it.hasNext()) {
            String s = it.next().toString();
            b.append(s);
            if (!it.hasNext()) continue;
            b.append(seperator);
        }
        b.append(end);
        return b.toString();
    }

    public static String listToString(List l, String start, String end) {
        return Tools.listToString(l, start, end, ",");
    }

    public static String listToString(List l, String seperator) {
        return Tools.listToString(l, "", "", seperator);
    }

    public static String listToString(List l) {
        return Tools.listToString(l, "", "", ",");
    }
}

