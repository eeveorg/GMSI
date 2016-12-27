/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.SourceObject;

public class Line {
    public int num;
    public String content;
    public SourceObject source;

    public Line(String content, int num, SourceObject source) {
        this.content = content;
        this.num = num;
        this.source = source;
        num += 5;
    }
}

