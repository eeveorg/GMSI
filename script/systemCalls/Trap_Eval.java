/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.IOException;
import script.InternalScriptError;
import script.ParseError;
import script.Script;
import script.SyntaxError;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_Eval
extends Trap {
    public Trap_Eval(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String code = this.getStrParam(0, true);
        String name = this.getStrParam(1, true);
        try {
            this.owner.execCode(code, name);
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
        return voidResult;
    }
}

