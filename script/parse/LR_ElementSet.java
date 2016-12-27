/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.LinkedList;
import script.parse.LR_Element;

public class LR_ElementSet {
    public LinkedList<LR_Element> elements = new LinkedList();

    public LR_ElementSet() {
    }

    public boolean equals(Object o) {
        if (!(o instanceof LR_ElementSet)) {
            return false;
        }
        LR_ElementSet e = (LR_ElementSet)o;
        for (LR_Element e1 : e.elements) {
            boolean matches = false;
            for (LR_Element e2 : this.elements) {
                if (!e1.equals(e2)) continue;
                matches = true;
            }
            if (matches) continue;
            return false;
        }
        return true;
    }

    public void clear() {
        this.elements.clear();
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public LR_ElementSet(LR_Element e) {
        this.add(e);
    }

    public boolean add(LR_Element toAdd) {
        for (LR_Element e : this.elements) {
            if (!e.equals(toAdd)) continue;
            return false;
        }
        this.elements.add(toAdd);
        return true;
    }

    public boolean addAll(LR_ElementSet toAdd) {
        boolean change = false;
        for (LR_Element e : toAdd.elements) {
            if (!this.add(e)) continue;
            change = true;
        }
        return change;
    }

    public String toString() {
        String result = "{";
        for (LR_Element t : this.elements) {
            result = String.valueOf(result) + t;
            if (this.elements.getLast().equals(t)) continue;
            result = String.valueOf(result) + ",\n";
        }
        return String.valueOf(result) + "}";
    }
}

