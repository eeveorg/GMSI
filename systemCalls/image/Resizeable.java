/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Resizeable
extends MouseAdapter
implements MouseMotionListener {
    public static final boolean DEBUG = false;
    int fix_pt_x = -1;
    int fix_pt_y = -1;
    private boolean mXAdjusting;
    private boolean mYAdjusting;
    Component mResizeable;
    private Double mAspectRatio;
    private Cursor mDefaultCursor;
    private Dimension mMinSize;
    private Dimension mMaxSize;
    private boolean mResizeInAllDirections = false;
    private boolean mSetPreferredSize = false;
    private boolean enabled = true;
    private boolean mDragging = false;
    private int drag_x;
    private int drag_y;
    private int drag_loc_x;
    private int drag_loc_y;

    public Resizeable(Component c) {
        this.mResizeable = c;
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
    }

    public Resizeable(Window w, Component drag) {
        drag.addMouseMotionListener(this);
        drag.addMouseListener(this);
        this.mResizeable = w;
    }

    public void setEnabled(boolean en) {
        this.enabled = en;
    }

    public boolean isRespectingMinSize() {
        if (this.mMinSize == null) {
            return true;
        }
        return false;
    }

    public boolean isRespectingMaxSize() {
        if (this.mMaxSize == null) {
            return true;
        }
        return false;
    }

    public void setRespectingMaxSize(boolean b) {
        this.mMaxSize = b ? this.mResizeable.getMaximumSize() : null;
    }

    public void setRespectingMinSize(boolean b) {
        this.mMinSize = b ? this.mResizeable.getMinimumSize() : null;
    }

    public void setMaintainAspect(double x, double y) {
        this.setMaintainAspect(new Double(Math.atan2(y, x)));
    }

    public void setMaintainAspect(Double angle) {
        this.mAspectRatio = angle;
        if (angle != null) {
            this.setResizeInAllDirections(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        this.setCursorType(me.getPoint());
    }

    public boolean isResizingInAllDirections() {
        return this.mResizeInAllDirections;
    }

    public void setResizeInAllDirections(boolean b) {
        this.mResizeInAllDirections = b;
        if (b) {
            this.setMaintainAspect(null);
        }
    }

    protected void setCursorType(Point p) {
        boolean e;
        if (this.mDragging) {
            return;
        }
        if (!this.enabled) {
            return;
        }
        boolean n = false;
        boolean w = false;
        boolean s = p.y + 4 >= this.mResizeable.getHeight();
        boolean bl = e = p.x + 4 >= this.mResizeable.getWidth();
        if (this.mResizeInAllDirections) {
            n = p.y <= 4;
            boolean bl2 = w = p.x <= 4;
        }
        if (e) {
            if (s) {
                this.mResizeable.setCursor(Cursor.getPredefinedCursor(5));
                return;
            }
            if (n) {
                this.mResizeable.setCursor(Cursor.getPredefinedCursor(7));
                return;
            }
            this.mResizeable.setCursor(Cursor.getPredefinedCursor(11));
            return;
        }
        if (w) {
            if (s) {
                this.mResizeable.setCursor(Cursor.getPredefinedCursor(4));
                return;
            }
            if (n) {
                this.mResizeable.setCursor(Cursor.getPredefinedCursor(6));
                return;
            }
            this.mResizeable.setCursor(Cursor.getPredefinedCursor(10));
            return;
        }
        if (s) {
            this.mResizeable.setCursor(Cursor.getPredefinedCursor(9));
            return;
        }
        if (n) {
            this.mResizeable.setCursor(Cursor.getPredefinedCursor(8));
        } else if (this.mDefaultCursor != null) {
            this.mResizeable.setCursor(this.mDefaultCursor);
        }
    }

    public void setDefaultMouseCursor(Cursor c) {
        this.mDefaultCursor = c;
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        Cursor c = this.mResizeable.getCursor();
        Point loc = this.mResizeable.getLocation();
        this.mXAdjusting = false;
        this.mYAdjusting = false;
        this.mDragging = false;
        this.mResizeable.getParent().repaint();
        if (c.equals(Cursor.getPredefinedCursor(5))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = loc.y;
            return;
        }
        if (c.equals(Cursor.getPredefinedCursor(11))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = -1;
            return;
        }
        if (c.equals(Cursor.getPredefinedCursor(9))) {
            this.fix_pt_x = -1;
            this.fix_pt_y = loc.y;
            return;
        }
        if (c.equals(Cursor.getPredefinedCursor(4))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = loc.y;
            this.mXAdjusting = true;
            return;
        }
        if (c.equals(Cursor.getPredefinedCursor(10))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = -1;
            this.mXAdjusting = true;
            return;
        }
        if (c.equals(Cursor.getPredefinedCursor(6))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = loc.y;
            this.mXAdjusting = true;
            this.mYAdjusting = true;
        }
        if (c.equals(Cursor.getPredefinedCursor(8))) {
            this.fix_pt_y = loc.y;
            this.fix_pt_x = -1;
            this.mYAdjusting = true;
        }
        if (c.equals(Cursor.getPredefinedCursor(7))) {
            this.fix_pt_x = loc.x;
            this.fix_pt_y = loc.y;
            this.mYAdjusting = true;
        }
        if (c.equals(Cursor.getPredefinedCursor(13))) {
            this.mDragging = true;
            this.drag_x = loc.x + me.getX();
            this.drag_y = loc.y + me.getY();
            this.drag_loc_x = loc.x;
            this.drag_loc_y = loc.y;
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        this.fix_pt_x = -1;
        this.fix_pt_y = -1;
        this.mDragging = false;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        this.setCursorType(me.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int height;
        Point p = me.getPoint();
        if (this.mDragging) {
            this.mResizeable.setLocation(this.drag_loc_x + p.x + this.mResizeable.getX() - this.drag_x, this.drag_loc_y + p.y + this.mResizeable.getY() - this.drag_y);
        }
        if (this.fix_pt_x == -1 && this.fix_pt_y == -1) {
            return;
        }
        int width = this.fix_pt_x == -1 ? this.mResizeable.getWidth() : p.x;
        int n = height = this.fix_pt_y == -1 ? this.mResizeable.getHeight() : p.y;
        if (this.mXAdjusting) {
            if (this.mAspectRatio == null) {
                this.mResizeable.setLocation(new Point(this.mResizeable.getX() + p.x, this.mResizeable.getY()));
                width = this.mResizeable.getWidth() - p.x;
            } else {
                width = this.mResizeable.getWidth() - p.x;
            }
        }
        if (this.mYAdjusting) {
            if (this.mAspectRatio == null) {
                this.mResizeable.setLocation(new Point(this.mResizeable.getX(), this.mResizeable.getY() + p.y));
                height = this.mResizeable.getHeight() - p.y;
            } else {
                height = this.mResizeable.getHeight() - p.y;
            }
        }
        if (this.mMinSize != null) {
            width = width < this.mMinSize.width ? this.mMinSize.width : width;
            int n2 = height = height < this.mMinSize.height ? this.mMinSize.height : height;
        }
        if (this.mMaxSize != null) {
            width = width > this.mMaxSize.width ? this.mMaxSize.width : width;
            int n3 = height = height > this.mMaxSize.height ? this.mMaxSize.height : height;
        }
        if (this.mAspectRatio == null) {
            if (this.mSetPreferredSize) {
                this.mResizeable.setPreferredSize(new Dimension(width > 1 ? width : 1, height > 1 ? height : 1));
                this.mResizeable.setSize(new Dimension(width > 1 ? width : 1, height > 1 ? height : 1));
                Container c = this.mResizeable.getParent();
                if (c != null && c instanceof JComponent) {
                    ((JComponent)c).revalidate();
                }
            } else {
                this.mResizeable.setSize(new Dimension(width > 1 ? width : 1, height > 1 ? height : 1));
            }
        } else {
            double distance = Math.sqrt(width * width + height * height);
            width = (int)(distance * Math.cos(this.mAspectRatio));
            height = (int)(distance * Math.sin(this.mAspectRatio));
            if (this.mMinSize != null) {
                width = width < this.mMinSize.width ? this.mMinSize.width : width;
                int n4 = height = height < this.mMinSize.height ? this.mMinSize.height : height;
            }
            if (this.mMaxSize != null) {
                width = width > this.mMaxSize.width ? this.mMaxSize.width : width;
                int n5 = height = height > this.mMaxSize.height ? this.mMaxSize.height : height;
            }
            if (this.mSetPreferredSize) {
                this.mResizeable.setPreferredSize(new Dimension(width > 1 ? width : 1, height > 1 ? height : 1));
            } else {
                this.mResizeable.setSize(new Dimension(width > 1 ? width : 1, height > 1 ? height : 1));
            }
        }
    }

    public boolean isAdjustingPreferredSize() {
        return this.mSetPreferredSize;
    }

    public void setAdjustPreferredSize(boolean b) {
        this.mSetPreferredSize = b;
    }

    public static void main(String[] args) {
        JPanel jp = new JPanel();
        JButton jb = new JButton("hello");
        JButton jb2 = new JButton("hello2");
        JButton jb3 = new JButton("hello3");
        jp.setPreferredSize(new Dimension(300, 300));
        jp.setSize(300, 300);
        jp.setLayout(null);
        jp.add(jb);
        jp.add(jb2);
        jp.add(jb3);
        jb.setSize(40, 40);
        jb.setLocation(2, 2);
        jb2.setSize(30, 20);
        jb2.setLocation(30, 10);
        jb3.setSize(30, 30);
        jb3.setLocation(10, 50);
        Resizeable _r = new Resizeable(jb);
        _r.setDefaultMouseCursor(Cursor.getPredefinedCursor(0));
        Resizeable r = new Resizeable(jb2);
        r.setMaintainAspect(30.0, 20.0);
        r.setDefaultMouseCursor(Cursor.getPredefinedCursor(0));
        Resizeable r2 = new Resizeable(jb3);
        r2.setResizeInAllDirections(true);
        jb2.setMinimumSize(new Dimension(15, 10));
        jb2.setMaximumSize(new Dimension(210, 140));
        r.setRespectingMaxSize(true);
        r.setRespectingMinSize(true);
        JFrame jf = new JFrame();
        jf.add(jp);
        jf.setDefaultCloseOperation(3);
        jf.setLocation(100, 100);
        jf.pack();
        jf.setVisible(true);
    }
}

