/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.LRterminals.Comma;
import script.LRterminals.SimpleTypeName;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class SimpleTypeList
extends Token {
    private LinkedList<SimpleTypeName> types = new LinkedList();

    public LinkedList<SimpleTypeName> getList() {
        return this.types;
    }

    public SimpleTypeList(SimpleTypeName s) {
        super(s, (String)((Object)s));
        this.types.add(s);
    }

    public SimpleTypeList(SimpleTypeList l, Comma k, SimpleTypeName s) {
        super(l, l + "," + s);
        this.types.addAll(l.types);
        this.types.add(s);
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<SimpleTypeName> iter = this.types.listIterator();
        while (iter.hasNext()) {
            Token t = iter.next();
            iter.set((SimpleTypeName)t.unwrap());
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        throw new EvaluationError(this, "Cannot evaluate a Simple Type Name");
    }
}

