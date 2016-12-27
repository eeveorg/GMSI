/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import javax.swing.JOptionPane;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.systemCalls.Trap;

public class Trap_ConfirmDialog
extends Trap {
    public Trap_ConfirmDialog(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String title = this.getStrParam(0, true);
        String text = this.getStrParam(1, true);
        int buttons = this.getIntParam(2);
        if (buttons == 0) {
            buttons = -1;
        } else if (buttons == 1) {
            buttons = 2;
        } else if (buttons == 2) {
            buttons = 0;
        } else {
            throw new InternalScriptError("Trap error: The parameter \"buttons\" for \"confirmDialog\" must be between 0 and 2!");
        }
        int result = JOptionPane.showConfirmDialog(null, text, title, buttons);
        if (result == 0 || result == 0) {
            return BoolObject.TRUE;
        }
        return BoolObject.FALSE;
    }
}

