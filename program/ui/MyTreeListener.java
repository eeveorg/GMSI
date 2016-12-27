/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.tree.TreePath;
import program.JobExecFile;
import program.Program;
import program.ui.FileTree;
import program.ui.MonitorPanel;
import program.ui.ScriptInfoParser;

public class MyTreeListener
extends MouseAdapter
implements TreeSelectionListener,
ActionListener {
    private JTree tree;
    private JTextPane output;
    private ScriptInfoParser parser = new ScriptInfoParser();
    private ScriptInfoParser.InfoParseResult nothingSelected;
    private ScriptInfoParser.InfoParseResult mapSelected;
    private JButton exec;
    private JButton view;
    private ScriptInfoParser.InfoParseResult currentFile = null;

    public MyTreeListener(JTree t, JTextPane out, JButton exec, JButton view) {
        this.tree = t;
        this.exec = exec;
        this.view = view;
        this.output = out;
        DefaultStyledDocument notSelectDoc = new DefaultStyledDocument();
        try {
            notSelectDoc.insertString(0, "Script info:\n\n", ScriptInfoParser.capt);
            notSelectDoc.insertString(notSelectDoc.getLength(), "- select a script file -", null);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
        DefaultStyledDocument mapSelectDoc = new DefaultStyledDocument();
        try {
            mapSelectDoc.insertString(0, "This is a wc3 map:\n\n", ScriptInfoParser.capt);
            mapSelectDoc.insertString(mapSelectDoc.getLength(), "If you execute this file, the program will search for a trigger called \"script.ini\" (which has to be a comment) and will execute its content as GSL code. If the map contains no trigger with that name or that trigger is not a comment you will receive an error.", null);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
        this.nothingSelected = new ScriptInfoParser.InfoParseResult(notSelectDoc, false, false, null);
        this.mapSelected = new ScriptInfoParser.InfoParseResult(mapSelectDoc, true, false, null);
    }

    private File assemblePath(TreePath p) {
        Object[] path = p.getPath();
        StringBuilder b = new StringBuilder();
        int i = 1;
        int max = path.length;
        while (i < max) {
            b.append(path[i].toString());
            if (i < max - 1) {
                b.append("/");
            }
            ++i;
        }
        return new File(b.toString());
    }

    private void updateStatus(ScriptInfoParser.InfoParseResult r) {
        this.currentFile = r;
        this.output.setDocument(r.result);
        this.exec.setEnabled(r.allowExecution);
        this.view.setEnabled(r.allowViewSource);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String file = ((FileTree.MyTreeNode)e.getPath().getLastPathComponent()).getUserObject().toString();
        if (file.endsWith(".gsl")) {
            this.updateStatus(this.parser.parseFile(this.assemblePath(e.getPath())));
        } else if (file.endsWith(".w3x")) {
            this.mapSelected.file = this.assemblePath(e.getPath());
            this.updateStatus(this.mapSelected);
        } else {
            this.updateStatus(this.nothingSelected);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = this.tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = this.tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1 && e.getClickCount() != 1 && e.getClickCount() == 2) {
            Object[] path = selPath.getPath();
            StringBuilder p = new StringBuilder();
            int i = 1;
            int max = path.length;
            while (i < max) {
                p.append(path[i].toString());
                if (i < max - 1) {
                    p.append("/");
                }
                ++i;
            }
            if (this.currentFile.allowExecution) {
                if (this.currentFile == this.mapSelected) {
                    this.execCurrentMap();
                } else {
                    this.execCurrentFile();
                }
            }
        }
    }

    private void execCurrentMap() {
        Program.getMainFrame().setSelectedTab(1);
        Program.getMainFrame().getMonitor().setSelectedTab(0);
        Program.setJob(new JobExecFile(this.currentFile.file, true));
    }

    private void execCurrentFile() {
        Program.getMainFrame().setSelectedTab(1);
        Program.getMainFrame().getMonitor().setSelectedTab(0);
        Program.setJob(new JobExecFile(this.currentFile.file, false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.view) {
            String editor = "\"" + Program.getIni().getPropertyString("editor", "editorFile", "") + "\"";
            if (editor.equals("")) {
                JOptionPane.showInternalMessageDialog(null, "You haven't specified an editor for viewing script files in the .ini! ", "No editor selected", 0);
                return;
            }
            try {
                Runtime.getRuntime().exec(String.valueOf(editor) + " \"" + this.currentFile.file.getAbsolutePath() + "\"", null);
            }
            catch (IOException e1) {
                JOptionPane.showInternalMessageDialog(null, "An error occured, while trying to start the specified editor. Check if you have specified the right editorFile in your .ini! ", "Editor not found", 0);
            }
        } else if (e.getSource() == this.exec) {
            if (this.currentFile == this.mapSelected) {
                this.execCurrentMap();
            } else {
                this.execCurrentFile();
            }
        } else {
            this.parser.clearCache();
        }
    }
}

