/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import javax.swing.JCheckBox;
import program.Program;
import program.misc.IniSubscriber;

public class IniCheckbox
extends JCheckBox
implements IniSubscriber {
    private static final long serialVersionUID = 1;

    public IniCheckbox(String text, String section, String key, boolean defaultValue) {
        super(text);
        this.setSelected(Program.getIni().subscribePropertyBool(this, section, key, defaultValue));
    }

    @Override
    public Object inform() {
        return this.isSelected();
    }
}

