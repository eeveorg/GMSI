/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.io.File;

public class SourceObject {
    protected String sourceFile;

    protected SourceObject() {
    }

    public SourceObject(String s) {
        this.sourceFile = s;
    }

    public String getSourceFolder() {
        return new File(this.sourceFile).getParent();
    }

    public File getSourceFile() {
        return new File(this.sourceFile);
    }

    public String toString() {
        return this.sourceFile;
    }
}

