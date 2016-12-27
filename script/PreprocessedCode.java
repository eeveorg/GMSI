/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.util.LinkedList;
import script.Line;
import script.SourceObject;

public class PreprocessedCode {
    protected LinkedList<Line> lines = new LinkedList();
    protected SourceObject source;

    private Line processLine(String content, int lineNum) {
        return new Line(content, lineNum, this.source);
    }

    public PreprocessedCode(String input, SourceObject source) {
        this.source = source;
        String[] lineArray = input.split("\n");
        int i = 0;
        while (i < lineArray.length) {
            Line l = this.processLine(lineArray[i], i + 1);
            if (l != null) {
                this.lines.add(l);
            }
            ++i;
        }
    }
}

