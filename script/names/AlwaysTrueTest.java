/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import script.dataObjects.DataObject;
import script.names.TypeTest;

public class AlwaysTrueTest
implements TypeTest {
    private static AlwaysTrueTest t = new AlwaysTrueTest();

    @Override
    public boolean test(DataObject o) {
        return true;
    }

    private AlwaysTrueTest() {
    }

    public static AlwaysTrueTest getTest() {
        return t;
    }

    @Override
    public boolean isAncestorOf(TypeTest t) {
        if (t instanceof AlwaysTrueTest) {
            return true;
        }
        return false;
    }
}

