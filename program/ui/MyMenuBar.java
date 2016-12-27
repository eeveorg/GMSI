/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import program.Job;
import program.JobExecFile;
import program.JobListener;
import program.Program;
import program.misc.UpdateFiles;
import program.ui.MonitorPanel;
import program.ui.Settings;

public class MyMenuBar
extends JMenuBar
implements ActionListener,
JobListener {
    private static final long serialVersionUID = 1;
    private JMenu MFile = new JMenu("File");
    private JMenuItem MISync = new JMenuItem("Synchronize Data with WC3");
    private JMenuItem MIExit = new JMenuItem("Exit");
    private JMenu MHelp = new JMenu("Help");
    private JMenuItem MIDocumentation = new JMenuItem("Documentation");
    private JMenuItem MIAbout = new JMenuItem("About GMSI");
    private JMenu MScript = new JMenu("Script");
    private JMenuItem MIExecute = new JMenuItem("Execute Script...");
    private JMenuItem MIRestart = new JMenuItem("Restart last script");
    private JMenu MSettings = new JMenu("Settings");
    private JMenuItem MIFolders = new JMenuItem("Folders...");

    public MyMenuBar(boolean monitorOnly) {
        this.add(this.MFile);
        this.MFile.add(this.MISync);
        this.MISync.addActionListener(this);
        this.MFile.add(new JSeparator());
        this.MFile.add(this.MIExit);
        this.MIExit.addActionListener(this);
        if (!monitorOnly) {
            this.add(this.MScript);
            this.MScript.add(this.MIExecute);
            this.MIExecute.addActionListener(this);
            Program.addJobListener(this);
            this.MScript.add(this.MIRestart);
            this.MIRestart.addActionListener(this);
            this.MIRestart.setEnabled(false);
        }
        this.add(this.MSettings);
        this.MSettings.add(this.MIFolders);
        this.MIFolders.addActionListener(this);
        this.add(this.MHelp);
        this.MHelp.add(this.MIDocumentation);
        this.MIDocumentation.addActionListener(this);
        this.MHelp.add(this.MIAbout);
        this.MIAbout.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent act) {
        if (act.getSource() == this.MIExit) {
            System.exit(0);
        } else if (act.getSource() == this.MIAbout) {
            JOptionPane.showMessageDialog(Program.getMainFrame(), "GMSI beta v2.1.20 using GSL v" + Program.getScript().getVersion() + "\nby gex, December 2008\nmail: gekko_tgh@gmx.de\n\n" + "Visit http://www.eeve.org for more information.\nCheck out my maps there ;)\n\n" + "Mpq handling with ShadowFlare's SFMpq.dll.\nThanks to Dr.Willy & shadow1500" + " for some code snippets.\nThanks again to Dr.Willy for showing me where to find\n" + "information about the Wc3 file format.\nThanks for this information, I was unable to find out who wrote it.\n" + "Thanks to everyone who advised me.", "Manual", 1);
        } else if (act.getSource() == this.MIDocumentation) {
            try {
                int o;
                Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "\"" + new File("").getAbsolutePath() + "\\doc\\index.html\""});
                while ((o = p.getInputStream().read()) != -1) {
                    System.out.print((char)o);
                }
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(Program.getMainFrame(), "Unable to start browser", "Unable to start your browser, starting JAVA browser.\nHowever this looks ugly :(\n Maybe rather open doc/index.html with your favorite browser!", 2);
                JEditorPane pane = new JEditorPane();
                JScrollPane scroll = new JScrollPane(pane);
                pane.setEditable(false);
                try {
                    pane.setPage("File:///" + new File("").getAbsolutePath() + "\\doc\\index.html");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                MyListenerFrame f = new MyListenerFrame("Documentation", pane);
                f.add(scroll);
                f.setSize(800, 600);
                f.setVisible(true);
            }
        } else if (act.getSource() == this.MISync) {
            Program.getMainFrame().setSelectedTab(1);
            Program.getMainFrame().getMonitor().setSelectedTab(1);
            Program.setJob(new UpdateFiles());
        } else if (act.getSource() == this.MIRestart) {
            Program.getMainFrame().setSelectedTab(1);
            Program.getMainFrame().getMonitor().setSelectedTab(0);
            Program.setJob(Program.getLastJob());
        } else if (act.getSource() == this.MIExecute) {
            CustomFilter filter = new CustomFilter(".w3x,.gsl");
            JFileChooser openChoose = new JFileChooser(".");
            openChoose.setFileSelectionMode(0);
            openChoose.setMultiSelectionEnabled(false);
            openChoose.setDialogTitle("Choose a file to execute");
            openChoose.setFileFilter(filter);
            if (openChoose.showDialog(null, "Okay") == 0) {
                Program.getMainFrame().setSelectedTab(1);
                Program.getMainFrame().getMonitor().setSelectedTab(0);
                Program.setJob(new JobExecFile(openChoose.getSelectedFile()));
            }
        } else if (act.getSource() == this.MIFolders) {
            Settings.getFolders().setVisible(true);
        }
    }

    @Override
    public void jobFinished(Job j) {
        this.MIExecute.setEnabled(true);
        if (Program.getLastJob() != null) {
            this.MIRestart.setEnabled(true);
        }
        this.MISync.setEnabled(true);
    }

    @Override
    public void jobStarted(Job j) {
        this.MIExecute.setEnabled(false);
        this.MIRestart.setEnabled(false);
        this.MISync.setEnabled(false);
    }

    private class CustomFilter
    extends FileFilter {
        private String ext;
        private String[] exts;

        public CustomFilter(String ext) {
            this.ext = ext;
            if (ext == null) {
                this.exts = null;
                ext = "All";
                return;
            }
            this.exts = ext.split(",");
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            if (this.exts == null) {
                return true;
            }
            String fileName = f.getName().toLowerCase();
            String[] arrstring = this.exts;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String ex = arrstring[n2];
                if (fileName.endsWith(ex)) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return String.valueOf(this.ext) + " files";
        }
    }

    private class MyListenerFrame
    extends JFrame
    implements HyperlinkListener {
        private JEditorPane htmlPane;

        public MyListenerFrame(String s, JEditorPane htmlPane) {
            super(s);
            this.htmlPane = htmlPane;
            htmlPane.addHyperlinkListener(this);
            this.setIconImage(new ImageIcon("misc/icon.gif").getImage());
        }

        @Override
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    this.htmlPane.setPage(event.getURL());
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
    }

}

