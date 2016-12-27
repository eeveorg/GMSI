/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.HashMap;
import java.util.LinkedList;
import script.ParseError;

public class LocalNameMapper {
    private static LinkedList<LocalNameMapper> mappers = new LinkedList();
    private int num = 0;
    private LinkedList<HashMap<String, Integer>> stack = new LinkedList();

    public static LocalNameMapper pushMapper() {
        LocalNameMapper result = new LocalNameMapper();
        mappers.addFirst(result);
        return result;
    }

    public static int popMapper() {
        LocalNameMapper l = mappers.poll();
        return l.num;
    }

    public static LocalNameMapper getMapper() {
        return mappers.peek();
    }

    public LocalNameMapper() {
        this.stack.addFirst(new HashMap());
    }

    public int registerLocal(String name) throws ParseError {
        for (HashMap<String, Integer> space : this.stack) {
            if (!space.containsKey(name)) continue;
            throw new ParseError("Duplicate local variable " + name);
        }
        this.stack.peek().put(name, this.num);
        return this.num++;
    }

    public void pushSpace() {
        this.stack.addFirst(new HashMap());
    }

    public void popSpace() {
        this.stack.poll();
    }

    public int resolveLocal(String name) {
        for (HashMap<String, Integer> space : this.stack) {
            if (!space.containsKey(name)) continue;
            return space.get(name);
        }
        return -1;
    }
}

