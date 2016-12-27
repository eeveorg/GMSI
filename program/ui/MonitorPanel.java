/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import program.Job;
import program.JobListener;
import program.Program;
import program.misc.Log;
import program.misc.LogWriter;
import program.ui.AutoscrollPane;
import program.ui.UIOutputStream;
import script.systemCalls.EchoTarget;

public class MonitorPanel
extends JPanel
implements ActionListener,
JobListener {
    private static final long serialVersionUID = 1;
    private JTabbedPane tabs;
    private JButton restart = new JButton("Restart last script");
    private JButton abort = new JButton("Abort script");
    private JButton exit = new JButton("Okay, Exit");
    private AutoscrollPane echoPane;
    private AutoscrollPane statusPane;
    private JButton clearEcho = new JButton("Clear");
    private JButton clearStatus = new JButton("Clear");

    public MonitorPanel(boolean monitorOnly) {
        GridBagLayout gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = monitorOnly ? new Insets(2, 2, 2, 2) : new Insets(20, 8, 4, 8);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 10;
        c.gridwidth = 10;
        c.weightx = 0.2;
        c.weighty = 0.2;
        JPanel echo = new JPanel();
        JPanel status = new JPanel();
        JPanel info = new JPanel();
        info.add(new JLabel("not used yet"));
        this.tabs = new JTabbedPane();
        this.add((Component)this.tabs, c);
        this.tabs.addTab("Script output", echo);
        this.tabs.addTab("Log & Warnings", status);
        echo.setLayout(new GridBagLayout());
        status.setLayout(new GridBagLayout());
        this.echoPane = new AutoscrollPane();
        echo.add((Component)new JScrollPane(this.echoPane), c);
        this.statusPane = new AutoscrollPane();
        status.add((Component)new JScrollPane(this.statusPane), c);
        System.setErr(new PrintStream(new UIOutputStream(this.statusPane, Log.error)));
        c.gridx = 10;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        status.add((Component)this.clearStatus, c);
        echo.add((Component)this.clearEcho, c);
        this.clearStatus.addActionListener(this);
        this.clearEcho.addActionListener(this);
        Log.setLogWriter(this.statusPane);
        Log.setErrorWriter(this.echoPane);
        Program.getScript().setEchoTarget(this.echoPane);
        c.gridx = 0;
        c.gridy = 10;
        c.insets = monitorOnly ? new Insets(2, 4, 4, 4) : new Insets(4, 8, 8, 8);
        this.add((Component)this.restart, c);
        this.restart.addActionListener(this);
        this.restart.setEnabled(false);
        c.gridx = 1;
        c.fill = 0;
        c.anchor = 13;
        this.add((Component)this.abort, c);
        this.abort.setEnabled(false);
        this.abort.addActionListener(this);
        if (monitorOnly) {
            c.gridx = 9;
            this.add((Component)this.exit, c);
            this.exit.addActionListener(this);
        }
        Program.addJobListener(this);
    }

    public void setSelectedTab(int index) {
        this.tabs.setSelectedIndex(index);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.clearStatus) {
            this.statusPane.setText("");
        } else if (e.getSource() == this.clearEcho) {
            this.echoPane.setText("");
        } else if (e.getSource() == this.restart) {
            Program.setJob(Program.getLastJob());
        } else if (e.getSource() == this.abort) {
            Program.getScript().abort();
        } else if (e.getSource() == this.exit) {
            System.exit(0);
        }
    }

    @Override
    public void jobStarted(Job j) {
        this.restart.setEnabled(false);
        this.exit.setEnabled(false);
        this.abort.setEnabled(true);
    }

    @Override
    public void jobFinished(Job j) {
        if (Program.getLastJob() != null) {
            this.restart.setEnabled(true);
        }
        this.exit.setEnabled(true);
        this.abort.setEnabled(false);
    }
}

