/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import program.ui.MyTreeListener;

public class FileTree
extends JPanel
implements ActionListener {
    private JTree myTree;
    private File myDir;
    private JButton refreshButton = new JButton("Refresh");
    private static final long serialVersionUID = 1;

    public void refresh(File dir) {
        this.myTree.setModel(new DefaultTreeModel(this.addNodes(null, dir)));
        TreeModel m = this.myTree.getModel();
        MyTreeNode root = (MyTreeNode)m.getRoot();
        Object[] path = new MyTreeNode[2];
        path[0] = root;
        int i = 0;
        int max = root.getChildCount();
        while (i < max) {
            MyTreeNode cur = (MyTreeNode)root.getChildAt(i);
            if (cur.getUserObject().equals("script")) {
                path[1] = cur;
            }
            ++i;
        }
        this.myTree.expandPath(new TreePath(path));
    }

    public FileTree(File dir) {
        this.myDir = dir;
        this.setLayout(new BorderLayout());
        JTree tree = new JTree(this.addNodes(null, dir));
        tree.setCellRenderer(new MyRenderer());
        DefaultTreeSelectionModel myModel = new DefaultTreeSelectionModel();
        myModel.setSelectionMode(1);
        tree.setSelectionModel(myModel);
        TreeModel m = tree.getModel();
        MyTreeNode root = (MyTreeNode)m.getRoot();
        Object[] path = new MyTreeNode[2];
        path[0] = root;
        int i = 0;
        int max = root.getChildCount();
        while (i < max) {
            MyTreeNode cur = (MyTreeNode)root.getChildAt(i);
            if (cur.getUserObject().equals("script")) {
                path[1] = cur;
            }
            ++i;
        }
        tree.expandPath(new TreePath(path));
        JScrollPane myPane = new JScrollPane();
        myPane.getViewport().add(tree);
        this.add("Center", myPane);
        this.myTree = tree;
        this.add("South", this.refreshButton);
        this.refreshButton.addActionListener(this);
    }

    public void addListener(MyTreeListener l) {
        this.myTree.addTreeSelectionListener(l);
        this.myTree.addMouseListener(l);
    }

    MyTreeNode addNodes(MyTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        MyTreeNode curDir = new MyTreeNode(dir.getName(), true);
        if (curTop != null) {
            curTop.add(curDir);
        }
        Vector<String> ol = new Vector<String>();
        String[] tmp = dir.list();
        int i = 0;
        while (i < tmp.length) {
            ol.addElement(tmp[i]);
            ++i;
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        Vector<String> files = new Vector<String>();
        int i2 = 0;
        while (i2 < ol.size()) {
            String thisObject = (String)ol.elementAt(i2);
            String newPath = curPath.equals(".") ? thisObject : String.valueOf(curPath) + File.separator + thisObject;
            File f = new File(newPath);
            if (f.isDirectory()) {
                this.addNodes(curDir, f);
            } else {
                String name = f.getName().toLowerCase();
                if (name.endsWith(".gsl") || name.endsWith(".w3x")) {
                    files.addElement(thisObject);
                }
            }
            ++i2;
        }
        int fnum = 0;
        while (fnum < files.size()) {
            curDir.add(new MyTreeNode(files.elementAt(fnum), false));
            ++fnum;
        }
        return curDir;
    }

    public JTree getTree() {
        return this.myTree;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        this.refresh(this.myDir);
    }

    public JButton getRefreshButton() {
        return this.refreshButton;
    }

    private static class MyRenderer
    extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 1;
        static Icon gslIcon = new ImageIcon("misc/icon.gif");
        static Icon w3xIcon = new ImageIcon("misc/wc3.gif");

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            MyTreeNode m = (MyTreeNode)value;
            if (m.isFolder) {
                leaf = false;
                if (sel) {
                    expanded = true;
                }
            }
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf) {
                if (m.getUserObject().toString().endsWith(".gsl")) {
                    this.setIcon(gslIcon);
                } else {
                    this.setIcon(w3xIcon);
                }
            }
            return this;
        }
    }

    static class MyTreeNode
    extends DefaultMutableTreeNode {
        private static final long serialVersionUID = 1;
        boolean isFolder;

        public MyTreeNode(Object input, boolean isFolder) {
            super(input);
            this.isFolder = isFolder;
        }
    }

}

