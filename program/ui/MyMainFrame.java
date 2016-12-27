/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import program.ui.MonitorPanel;
import program.ui.MyMenuBar;
import program.ui.MyWindowListener;
import program.ui.ScriptChoosePanel;

public class MyMainFrame
extends JFrame {
    private static final long serialVersionUID = 1;
    private JTabbedPane tabs;
    private MonitorPanel monitor;

    public MyMainFrame() {
        this.setTitle("GMSI beta v2.1.20");
        this.setIconImage(new ImageIcon("misc/icon.gif").getImage());
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 800;
        int height = 700;
        this.setBounds(d.width / 2 - width / 2, d.height / 2 - height / 2, width, height);
        this.addWindowListener(new MyWindowListener());
        GridBagLayout gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(4, 4, 4, 4);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 10;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.tabs = new JTabbedPane();
        this.add((Component)this.tabs, c);
        ScriptChoosePanel test2 = new ScriptChoosePanel();
        this.tabs.addTab("Script Browser", test2);
        this.monitor = new MonitorPanel(false);
        this.tabs.addTab("Execution Monitor", this.monitor);
        this.setJMenuBar(new MyMenuBar(false));
    }

    public void setSelectedTab(int index) {
        this.tabs.setSelectedIndex(index);
    }

    public MonitorPanel getMonitor() {
        return this.monitor;
    }
}

