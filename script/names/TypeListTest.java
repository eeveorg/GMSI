/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.LinkedList;
import java.util.ListIterator;
import script.InternalScriptError;
import script.Tools;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.AlwaysTrueTest;
import script.names.TypeTest;

public class TypeListTest
implements TypeTest {
    private LinkedList<DataType> testList;

    @Override
    public boolean test(DataObject o) throws InternalScriptError {
        if (o.isNull()) {
            return true;
        }
        for (DataType d : this.testList) {
            if (!o.getType().isDerivedFrom(d)) continue;
            return true;
        }
        return false;
    }

    public TypeListTest(LinkedList<DataType> in) {
        this.testList = in;
    }

    @Override
    public boolean isAncestorOf(TypeTest t) {
        if (t instanceof AlwaysTrueTest) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (!(o instanceof TypeListTest)) {
            return false;
        }
        TypeListTest t = (TypeListTest)o;
        if (this == t) {
            return true;
        }
        if (t.testList.size() != this.testList.size()) {
            return false;
        }
        ListIterator<DataType> i1 = this.testList.listIterator();
        ListIterator<DataType> i2 = t.testList.listIterator();
        while (i1.hasNext()) {
            if (i1.next().equals(i2.next())) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        return Tools.listToString(this.testList, "<", ">");
    }
}

