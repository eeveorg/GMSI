/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.InternalScriptError;

public class ScriptStoppedException
extends InternalScriptError {
    private static final long serialVersionUID = 1;

    public ScriptStoppedException() {
        super("stopped");
    }
}

