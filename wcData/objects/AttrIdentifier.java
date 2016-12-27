/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

public class AttrIdentifier
implements Cloneable {
    private String name;
    private int level = 0;
    private int datapointer = 0;

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getDatapointer() {
        return this.datapointer;
    }

    public boolean isDatafield() {
        if (this.datapointer != 0) {
            return true;
        }
        return false;
    }

    public AttrIdentifier(String name) {
        this.name = name;
    }

    public AttrIdentifier(String name, int level, int datapointer) {
        this.name = name;
        this.level = level;
        this.datapointer = datapointer;
    }

    public String toString() {
        String result = this.name;
        if (this.datapointer != 0 || this.level != 0) {
            result = String.valueOf(result) + "|" + this.datapointer + "|" + this.level;
        }
        return result;
    }

    public static AttrIdentifier stringToAttrIdent(String s) {
        if (s.length() == 4) {
            return new AttrIdentifier(s);
        }
        String[] c = s.split("\\|");
        if (c.length != 1 && c.length != 3) {
            return null;
        }
        if (c.length == 1) {
            return new AttrIdentifier(s);
        }
        return new AttrIdentifier(c[0], Integer.parseInt(c[2]), Integer.parseInt(c[1]));
    }

    public boolean equals(Object o) {
        if (!(o instanceof AttrIdentifier)) {
            return false;
        }
        AttrIdentifier a = (AttrIdentifier)o;
        if (!a.name.equals(this.name)) {
            return false;
        }
        if (a.level != this.level) {
            return false;
        }
        if (a.datapointer != this.datapointer) {
            return false;
        }
        return true;
    }

    public AttrIdentifier clone() {
        return new AttrIdentifier(this.name, this.level, this.datapointer);
    }

    public void setDatapointer(int datapointer) {
        this.datapointer = datapointer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class InvalidAttrIdentException
    extends Exception {
        private static final long serialVersionUID = 1;

        public InvalidAttrIdentException(String s) {
            super(s);
        }
    }

}

