/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import program.misc.IniSubscriber;
import program.ui.IniTextfield;

public class IniFileChooseField
extends IniTextfield
implements IniSubscriber,
ActionListener {
    private static final long serialVersionUID = 1;
    private JButton browse = new JButton("...");
    private FileFilter filter;

    public IniFileChooseField(String text, String section, String key, String defaultValue, FileFilter filter) {
        super(text, section, key, defaultValue);
        this.filter = filter;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 0;
        c.insets = new Insets(1, 2, 2, 2);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.add((Component)this.browse, c);
        this.browse.addActionListener(this);
    }

    @Override
    public Object inform() {
        return this.textField.getText();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        JFileChooser openChoose = new JFileChooser(new File(this.textField.getText()));
        openChoose.setFileSelectionMode(1);
        openChoose.setMultiSelectionEnabled(false);
        openChoose.setDialogTitle("Choose a " + this.text);
        if (openChoose.showDialog(null, "Okay") == 0) {
            this.textField.setText(openChoose.getSelectedFile().getPath());
        }
    }
}

