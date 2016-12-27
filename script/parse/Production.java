/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import script.ParseError;
import script.Script;
import script.Token;
import script.parse.SyntaxTable;
import script.parse.ZustandsToken;

public class Production {
    public int num;
    Class<? extends Token> lValue;
    ArrayList<ProductionEntry> rValue = new ArrayList();
    private int priority;

    public Production(Class<? extends Token> lValue, int priority) {
        this.lValue = lValue;
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.addRValue(rValue5);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.addRValue(rValue5);
        this.addRValue(rValue6);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.addRValue(rValue5);
        this.addRValue(rValue6);
        this.addRValue(rValue7);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7, Class<? extends Token> rValue8, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.addRValue(rValue5);
        this.addRValue(rValue6);
        this.addRValue(rValue7);
        this.addRValue(rValue8);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7, Class<? extends Token> rValue8, Class<? extends Token> rValue9, int priority) {
        this.lValue = lValue;
        this.addRValue(rValue1);
        this.addRValue(rValue2);
        this.addRValue(rValue3);
        this.addRValue(rValue4);
        this.addRValue(rValue5);
        this.addRValue(rValue6);
        this.addRValue(rValue7);
        this.addRValue(rValue8);
        this.addRValue(rValue9);
        this.priority = priority;
    }

    public Production(Class<? extends Token> lValue) {
        this(lValue, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1) {
        this(lValue, rValue1, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2) {
        this(lValue, rValue1, rValue2, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3) {
        this(lValue, rValue1, rValue2, rValue3, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, rValue5, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, rValue5, rValue6, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, rValue5, rValue6, rValue7, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7, Class<? extends Token> rValue8) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, rValue5, rValue6, rValue7, rValue8, -1);
    }

    public Production(Class<? extends Token> lValue, Class<? extends Token> rValue1, Class<? extends Token> rValue2, Class<? extends Token> rValue3, Class<? extends Token> rValue4, Class<? extends Token> rValue5, Class<? extends Token> rValue6, Class<? extends Token> rValue7, Class<? extends Token> rValue8, Class<? extends Token> rValue9) {
        this(lValue, rValue1, rValue2, rValue3, rValue4, rValue5, rValue6, rValue7, rValue8, rValue9, -1);
    }

    public ProductionEntry get(int index) {
        return this.rValue.get(index);
    }

    public int getPriority() {
        return this.priority;
    }

    public int size() {
        return this.rValue.size();
    }

    public void addRValue(Class<? extends Token> toAdd, boolean list) {
        this.rValue.add(new ProductionEntry(toAdd, list));
    }

    public void addRValue(Class<? extends Token> toAdd) {
        this.addRValue(toAdd, false);
    }

    public int reduce(LinkedList<Token> stack, SyntaxTable synTable) throws SecurityException, NoSuchMethodException, ParseError, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Token t;
        ListIterator<ProductionEntry> iter = this.rValue.listIterator(this.rValue.size());
        int num = 0;
        ArrayList<Class<? extends Token>> temp = new ArrayList<Class<? extends Token>>();
        for (ProductionEntry e : this.rValue) {
            temp.add(e.wantedClass);
        }
        Class[] params = temp.toArray(new Class[0]);
        Object[] arguments = new Object[params.length];
        int index = params.length - 1;
        while (iter.hasPrevious()) {
            stack.removeLast();
            ProductionEntry e = iter.previous();
            Class<? extends Token> c = e.wantedClass;
            Token top = stack.getLast();
            if (!c.equals(top.getClass())) {
                throw new ParseError("WTF TYPER" + c.getSimpleName() + ":" + top.getClass().getSimpleName());
            }
            stack.removeLast();
            arguments[index] = top;
            --index;
            ++num;
        }
        ZustandsToken sprungZustand = (ZustandsToken)stack.getLast();
        int neuerZustand = synTable.getSprung(sprungZustand.zustand, this.lValue.getSimpleName());
        Constructor<? extends Token> cc = this.lValue.getConstructor(params);
        try {
            t = cc.newInstance(arguments);
        }
        catch (InvocationTargetException e) {
            throw new ParseError(e.getCause());
        }
        stack.addLast(t);
        stack.addLast(new ZustandsToken(null, neuerZustand));
        return num;
    }

    public String toString() {
        String result = String.valueOf(this.lValue.getSimpleName()) + " -> ";
        int curIndex = 0;
        for (ProductionEntry e : this.rValue) {
            result = String.valueOf(result) + e.wantedClass.getSimpleName();
            if (!e.equals(this.rValue.get(this.rValue.size() - 1))) {
                result = String.valueOf(result) + ",";
            }
            ++curIndex;
        }
        return result;
    }

    public class ProductionEntry {
        public Class<? extends Token> wantedClass;
        public boolean wantsList;

        public ProductionEntry(Class<? extends Token> wantedClass, boolean wantsList) {
            this.wantedClass = wantedClass;
            this.wantsList = wantsList;
        }
    }

}

