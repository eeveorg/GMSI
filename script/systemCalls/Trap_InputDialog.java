/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import javax.swing.JOptionPane;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_InputDialog
extends Trap {
    public Trap_InputDialog(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String defValue;
        String title = this.getStrParam(0, true);
        String text = this.getStrParam(1, true);
        Object result = JOptionPane.showInputDialog(null, text, title, 3, null, null, defValue = this.getStrParam(2, ""));
        if (result == null) {
            return nullResult;
        }
        return new StringObject((String)result);
    }
}

