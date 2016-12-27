/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.SourceObject;

public class SyntaxError
extends Exception {
    private static final long serialVersionUID = 1;

    public SyntaxError(String message, int line, SourceObject o) {
        super("Syntax error in line " + line + " in file " + o + ": " + message);
    }
}

