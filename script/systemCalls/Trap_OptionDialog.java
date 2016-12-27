/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.util.Set;
import javax.swing.JOptionPane;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.names.IntVarSpace;
import script.systemCalls.Trap;

public class Trap_OptionDialog
extends Trap {
    public Trap_OptionDialog(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String title = this.getStrParam(0, true);
        String text = this.getStrParam(1, true);
        ArrayObject options = this.getArrayParam(2, true);
        String defValue = this.getStrParam(3, "");
        Object[] opt = new Object[options.getIntKeys().size()];
        int num = 0;
        for (Object key : options.getIntKeys()) {
            opt[num++] = options.getIntSpace().get(key).toString();
        }
        Object result = JOptionPane.showInputDialog(null, text, title, 3, null, opt, defValue);
        if (result == null) {
            return nullResult;
        }
        return new StringObject((String)result);
    }
}

