/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import program.Program;
import program.misc.IniSubscriber;

public class IniTextfield
extends JPanel
implements IniSubscriber {
    private static final long serialVersionUID = 1;
    protected String text;
    protected JTextField textField = new JTextField(25);

    public IniTextfield(String text, String section, String key, String defaultValue) {
        this.setLayout(new GridBagLayout());
        this.text = text;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 0;
        c.insets = new Insets(1, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.add((Component)this.textField, c);
        JLabel capt = new JLabel(text);
        capt.setLabelFor(this.textField);
        capt.setHorizontalTextPosition(4);
        c.gridy = 0;
        c.insets = new Insets(2, 6, 2, 1);
        c.fill = 2;
        this.add((Component)capt, c);
        this.textField.setText(Program.getIni().subscribePropertyString(this, section, key, defaultValue));
    }

    @Override
    public Object inform() {
        return this.textField.getText();
    }
}

