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
import program.ui.MonitorPanel;
import program.ui.MyMenuBar;
import program.ui.MyWindowListener;

public class MonitorFrame
extends JFrame {
    private static final long serialVersionUID = 1;
    private MonitorPanel monitor;

    public MonitorFrame() {
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
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.monitor = new MonitorPanel(true);
        this.add((Component)this.monitor, c);
        this.setJMenuBar(new MyMenuBar(true));
    }

    public MonitorPanel getMonitor() {
        return this.monitor;
    }

    public static void main(String[] args) {
        new MonitorFrame().setVisible(true);
    }
}

