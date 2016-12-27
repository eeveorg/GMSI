/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.filechooser.FileFilter;
import program.Program;
import program.misc.IniSubscriber;
import program.ui.FileFilters;
import program.ui.IniFileChooseField;

public class Settings {
    private static Folders folderCache = null;

    private static Rectangle center(Frame f, int width, int height) {
        int x = f.getX() + f.getWidth() / 2 - width / 2;
        int y = f.getY() + f.getHeight() / 2 - height / 2;
        return new Rectangle(x, y, width, height);
    }

    public static JDialog getFolders() {
        if (folderCache == null) {
            folderCache = new Folders();
        }
        folderCache.setBounds(Settings.center(Program.getMainFrame(), 300, 120));
        return folderCache;
    }

    public void actionPerformed(ActionEvent arg0) {
    }

    private static class Folders
    extends SettingWindow {
        private static final long serialVersionUID = 1;

        public Folders() {
            super(1);
            GridBagConstraints c = new GridBagConstraints();
            c.fill = 0;
            c.insets = new Insets(4, 4, 4, 4);
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.weightx = 1.0;
            c.weighty = 1.0;
            this.add(new IniFileChooseField("Warcraft 3 Folder", "folders", "wc3folder", "", new FileFilters.OnlyFoldersFilter()), c);
        }
    }

    private static class SettingWindow
    extends JDialog
    implements ActionListener,
    WindowListener {
        private static final long serialVersionUID = 1;
        private LinkedList<IniSubscriber> containedSubscribers = new LinkedList();

        public SettingWindow(int y) {
            super((Frame)Program.getMainFrame(), true);
            GridBagLayout gridbag = new GridBagLayout();
            this.setLayout(gridbag);
            this.setResizable(false);
            this.addWindowListener(this);
            this.addOkayButton(y);
        }

        protected void addOkayButton(int y) {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = 0;
            c.insets = new Insets(4, 4, 4, 4);
            c.gridx = 0;
            c.gridy = y;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.weightx = 1.0;
            c.weighty = 1.0;
            JButton add = new JButton("OK");
            this.add(add, c);
            add.addActionListener(this);
            this.addWindowListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.setVisible(false);
            this.windowClosing(null);
        }

        public void add(Component i, GridBagConstraints c) {
            if (i instanceof IniSubscriber) {
                this.containedSubscribers.add((IniSubscriber)((Object)i));
            }
            super.add(i, c);
        }

        @Override
        public void windowClosed(WindowEvent arg0) {
        }

        @Override
        public void windowActivated(WindowEvent arg0) {
        }

        @Override
        public void windowClosing(WindowEvent arg0) {
            for (IniSubscriber i : this.containedSubscribers) {
                Program.getIni().update(i);
            }
        }

        @Override
        public void windowDeactivated(WindowEvent arg0) {
        }

        @Override
        public void windowDeiconified(WindowEvent arg0) {
        }

        @Override
        public void windowIconified(WindowEvent arg0) {
        }

        @Override
        public void windowOpened(WindowEvent arg0) {
        }
    }

}

