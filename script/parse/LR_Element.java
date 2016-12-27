/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.util.ArrayList;
import script.Token;
import script.parse.Production;

public class LR_Element {
    public Production production;
    public int index;

    public boolean equals(Object o) {
        if (!(o instanceof LR_Element)) {
            return false;
        }
        LR_Element e = (LR_Element)o;
        if (e.production.equals(this.production) && e.index == this.index) {
            return true;
        }
        return false;
    }

    public LR_Element(Production reduction, int index) {
        this.index = index;
        this.production = reduction;
    }

    public Production.ProductionEntry getRight() {
        return this.production.get(this.index);
    }

    public boolean hasRight() {
        if (this.production.size() <= this.index) {
            return false;
        }
        return true;
    }

    public String toString() {
        String result = String.valueOf(this.production.lValue.getSimpleName()) + " -> ";
        int curIndex = 0;
        for (Production.ProductionEntry e : this.production.rValue) {
            if (this.index == curIndex) {
                result = String.valueOf(result) + "*";
            }
            result = String.valueOf(result) + e.wantedClass.getSimpleName();
            if (!e.equals(this.production.rValue.get(this.production.rValue.size() - 1))) {
                result = String.valueOf(result) + ",";
            }
            ++curIndex;
        }
        return result;
    }
}

