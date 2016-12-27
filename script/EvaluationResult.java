/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.dataObjects.DataObject;

public class EvaluationResult {
    public DataObject value;
    private boolean wantReturn = false;
    private boolean wantBreak = false;
    private boolean wantContinue = false;
    private boolean skip = false;

    EvaluationResult() {
    }

    public boolean wantReturn() {
        return this.wantReturn;
    }

    public boolean wantBreak() {
        return this.wantBreak;
    }

    public boolean wantContinue() {
        return this.wantContinue;
    }

    public boolean skip() {
        return this.skip;
    }

    public void clearFlags() {
        this.skip = false;
        this.wantReturn = false;
        this.wantBreak = false;
        this.wantContinue = false;
    }

    public void setReturn(DataObject o) {
        this.value = o;
        this.wantReturn = true;
        this.skip = true;
    }

    public void setReturn() {
        this.value = null;
        this.wantReturn = true;
        this.skip = true;
    }

    public void setBreak() {
        this.wantBreak = true;
        this.skip = true;
    }

    public void setContinue() {
        this.wantContinue = true;
        this.skip = true;
    }
}

