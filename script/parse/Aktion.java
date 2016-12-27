/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

public class Aktion {
    public int type;
    public int zustand;
    public static final int SCHIEBE = 0;
    public static final int REDUZIERE = 1;
    public static final int AKZEPTIERE = 2;
    public static final int FEHLER = 3;

    public Aktion(int type, int zustand) {
        this.type = type;
        this.zustand = zustand;
    }

    public Aktion(int type) {
        this.type = type;
    }

    public String toString() {
        if (this.type == 0) {
            return "s" + this.zustand;
        }
        if (this.type == 1) {
            return "r" + this.zustand;
        }
        if (this.type == 2) {
            return "acc";
        }
        return "fail";
    }
}

