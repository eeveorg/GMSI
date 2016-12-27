/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.LinkedList;
import script.parse.LR_ElementSet;

public class LR_ElementSetSet {
    public LinkedList<LR_ElementSet> elements = new LinkedList();

    public LR_ElementSetSet() {
    }

    public void clear() {
        this.elements.clear();
    }

    public LR_ElementSetSet(LR_ElementSet e) {
        this.add(e);
    }

    public int size() {
        return this.elements.size();
    }

    public boolean add(LR_ElementSet toAdd) {
        for (LR_ElementSet e : this.elements) {
            if (!e.equals(toAdd)) continue;
            return false;
        }
        this.elements.add(toAdd);
        return true;
    }

    public boolean addAll(LR_ElementSetSet toAdd) {
        boolean change = false;
        for (LR_ElementSet e : toAdd.elements) {
            if (!this.add(e)) continue;
            change = true;
        }
        return change;
    }

    public String toString() {
        String result = "{";
        for (LR_ElementSet t : this.elements) {
            result = String.valueOf(result) + t;
            if (this.elements.getLast().equals(t)) continue;
            result = String.valueOf(result) + ",\n\n";
        }
        return String.valueOf(result) + "}";
    }
}

