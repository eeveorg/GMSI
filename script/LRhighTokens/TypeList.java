/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import script.EvaluationError;
import script.LRhighTokens.TypeName;
import script.LRterminals.Comma;
import script.ParseError;
import script.Token;
import script.dataObjects.DataObject;

public class TypeList
extends Token {
    private LinkedList<TypeName> types = new LinkedList();

    public LinkedList<TypeName> getList() {
        return this.types;
    }

    public TypeList(TypeName s) {
        super(s, (String)((Object)s));
        this.types.add(s);
    }

    public TypeList(TypeList l, Comma k, TypeName s) {
        super(l, l + "," + s);
        this.types.addAll(l.types);
        this.types.add(s);
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<TypeName> iter = this.types.listIterator();
        while (iter.hasNext()) {
            Token t = iter.next();
            iter.set((TypeName)t.unwrap());
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        throw new EvaluationError(this, "Cannot evaluate a Type Name List");
    }
}

