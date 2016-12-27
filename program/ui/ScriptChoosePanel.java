/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import program.ui.FileTree;
import program.ui.MyTreeListener;

public class ScriptChoosePanel
extends JPanel {
    private static final long serialVersionUID = 1;
    JSplitPane split1;
    FileTree tree;
    JPanel right;
    JTextPane text;
    JButton execute;
    JButton viewSource;

    public ScriptChoosePanel() {
        GridBagLayout gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(4, 4, 4, 4);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 10;
        c.gridwidth = 10;
        c.weightx = 0.2;
        c.weighty = 0.2;
        this.tree = new FileTree(new File("."));
        this.right = new JPanel();
        this.split1 = new JSplitPane(1, this.tree, this.right);
        this.add((Component)this.split1, c);
        this.split1.setDividerLocation(250);
        this.right.setLayout(new GridBagLayout());
        this.text = new JTextPane();
        this.right.add((Component)this.text, c);
        this.text.setEditable(false);
        this.text.setOpaque(false);
        Font f = new Font("Verdana", 0, 12);
        c.fill = 0;
        c.gridx = 0;
        c.gridy = -1;
        c.gridheight = 4;
        c.gridwidth = 3;
        c.weightx = 0.0;
        c.weighty = 0.0;
        this.right.add(new JSeparator());
        this.execute = new JButton("Execute Script");
        this.right.add((Component)this.execute, c);
        this.execute.setEnabled(false);
        c.gridx = 3;
        this.viewSource = new JButton("View Source");
        this.right.add((Component)this.viewSource, c);
        MyTreeListener listen = new MyTreeListener(this.tree.getTree(), this.text, this.execute, this.viewSource);
        this.viewSource.setEnabled(false);
        this.viewSource.addActionListener(listen);
        this.execute.addActionListener(listen);
        this.text.setFont(f);
        this.tree.addListener(listen);
        this.tree.getRefreshButton().addActionListener(listen);
    }
}

