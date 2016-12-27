/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.io.IOException;
import script.InternalScriptError;
import script.ParseError;
import script.Script;
import script.SyntaxError;
import script.patterns.SimpleMatchHandler;

public class ScriptMatchHandler
extends SimpleMatchHandler {
    private Script owner;

    public ScriptMatchHandler(String pattern, Script owner) {
        super(pattern);
        this.owner = owner;
    }

    @Override
    public String handleMatch(String[] groups) throws InternalScriptError {
        String s = super.handleMatch(groups);
        this.owner.resetScriptReplacement();
        try {
            this.owner.execCode(s, "Macro code:\"" + s + "\"");
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        catch (SyntaxError e) {
            throw new InternalScriptError(e.getMessage());
        }
        catch (ParseError e) {
            throw new InternalScriptError(e.getMessage());
        }
        return this.owner.getScriptReplacement();
    }
}

