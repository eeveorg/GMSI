/*
 * Decompiled with CFR 0_119.
 */
package script;

import script.EvaluationError;
import script.Token;

public class ScriptAbortionError
extends EvaluationError {
    public ScriptAbortionError(Token t, String s) {
        super(t, s);
    }
}

