/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.NullObject;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;
import systemCalls.image.Resizeable;
import systemCalls.image.TransparentTextField;

public class Trap_ShowClipbox
extends Trap
implements WindowListener,
MouseListener,
MouseMotionListener,
ActionListener {
    JFrame theBox = new JFrame("Clip image");
    Rectangle result;
    Resizeable resize;
    JLabel theImage;
    JLayeredPane layers;
    JButton okayButton;
    JButton cancelButton;
    JButton allButton;
    JButton nothingButton;
    JScrollPane scroll;
    TransparentTextField selection;
    private int startX;
    private int startY;
    private JLabel captionLabel;

    public Trap_ShowClipbox(Script s) {
        super(s, "showClipbox");
        this.theBox.setIconImage(new ImageIcon("misc/icon.gif").getImage());
        GridBagLayout gridbag = new GridBagLayout();
        this.theBox.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(8, 8, 8, 8);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 10;
        c.gridwidth = 10;
        c.weightx = 0.9;
        c.weighty = 0.9;
        this.theImage = new JLabel();
        this.layers = new JLayeredPane();
        this.layers.setLayout(null);
        this.layers.add((Component)this.theImage, JLayeredPane.DEFAULT_LAYER);
        this.layers.setSize(this.theImage.getSize());
        this.selection = new TransparentTextField();
        Resizeable r = new Resizeable(this.selection);
        r.setResizeInAllDirections(true);
        r.setDefaultMouseCursor(Cursor.getPredefinedCursor(13));
        this.resize = r;
        this.layers.add((Component)this.selection, JLayeredPane.DRAG_LAYER);
        this.scroll = new JScrollPane(this.layers);
        this.theBox.add((Component)this.scroll, c);
        this.theImage.addMouseListener(this);
        this.theImage.addMouseMotionListener(this);
        c.gridx = 10;
        c.fill = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        this.okayButton = new JButton("Okay");
        this.theBox.add((Component)this.okayButton, c);
        c.gridy = 2;
        this.cancelButton = new JButton("Cancel");
        this.theBox.add((Component)this.cancelButton, c);
        c.gridy = 3;
        this.nothingButton = new JButton("Clear selection");
        this.theBox.add((Component)this.nothingButton, c);
        c.gridy = 4;
        this.allButton = new JButton("Select whole image");
        this.theBox.add((Component)this.allButton, c);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 11;
        this.captionLabel = new JLabel("-", 0);
        this.theBox.add((Component)this.captionLabel, c);
        this.captionLabel.setFont(new Font("Arial", 1, 15));
        this.okayButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.nothingButton.addActionListener(this);
        this.allButton.addActionListener(this);
        this.theBox.addWindowListener(this);
        this.theBox.setAlwaysOnTop(true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public DataObject apply() throws InternalScriptError {
        BufferedImage in;
        Object o = this.getObjParam(0, true);
        String caption = this.getStrParam(1, "Select a region to be clipped");
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        in = (BufferedImage)o;
        this.result = null;
        this.captionLabel.setText(caption);
        int width = in.getWidth() + 165;
        int height = in.getHeight() + 86;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        boolean fullScreen = false;
        if (height < 224) {
            height = 224;
        }
        if (width > d.width) {
            width = d.width;
            fullScreen = true;
        }
        if (height > d.height - 120) {
            height = d.height - 120;
            fullScreen = true;
        }
        this.theBox.setSize(width, height);
        if (fullScreen) {
            this.theBox.setExtendedState(6);
        }
        this.theImage.setIcon(new ImageIcon(in));
        this.layers.setSize(in.getWidth(), in.getHeight());
        this.theImage.setSize(in.getWidth(), in.getHeight());
        this.layers.setPreferredSize(this.theImage.getSize());
        this.scroll.setPreferredSize(this.theImage.getSize());
        this.selection.setVisible(false);
        this.theBox.setVisible(true);
        try {
            JFrame jFrame = this.theBox;
            synchronized (jFrame) {
                this.theBox.wait();
            }
        }
        catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (this.result == null) {
            return nullResult;
        }
        ObjObject obj = new ObjObject(in.getSubimage(this.result.x, this.result.y, this.result.width, this.result.height));
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Image"));
        obj.explicitCastTo(e);
        return obj;
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cancelOption() {
        JFrame jFrame = this.theBox;
        synchronized (jFrame) {
            this.theBox.notifyAll();
        }
        this.result = null;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.cancelOption();
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

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        this.startX = arg0.getX();
        this.startY = arg0.getY();
        this.resize.setEnabled(false);
        this.selection.setVisible(true);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        this.resize.setEnabled(true);
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        int height;
        int width;
        int x = arg0.getX();
        int y = arg0.getY();
        if (x < this.startX) {
            width = this.startX - x;
        } else {
            width = x - this.startX;
            x = this.startX;
        }
        if (y < this.startY) {
            height = this.startY - y;
        } else {
            height = y - this.startY;
            y = this.startY;
        }
        this.selection.setBounds(x, y, width, height);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        Object o = arg0.getSource();
        if (o == this.okayButton) {
            if (this.selection.isVisible()) {
                this.result = this.selection.getBounds();
                if (this.result.x < 0) {
                    this.result.width += this.result.x;
                    this.result.x = 0;
                }
                if (this.result.y < 0) {
                    this.result.height += this.result.y;
                    this.result.y = 0;
                }
                if (this.result.width + this.result.x > this.theImage.getWidth()) {
                    this.result.width = this.theImage.getWidth() - this.result.x;
                }
                if (this.result.height + this.result.y > this.theImage.getHeight()) {
                    this.result.height = this.theImage.getHeight() - this.result.y;
                }
                this.theBox.dispose();
                JFrame jFrame = this.theBox;
                synchronized (jFrame) {
                    this.theBox.notifyAll();
                }
            } else {
                JOptionPane.showConfirmDialog(this.theBox, "Nothing selected!", "Error", -1, 0);
            }
        } else if (o == this.allButton) {
            this.selection.setVisible(true);
            this.selection.setBounds(this.theImage.getBounds());
        } else if (o == this.cancelButton) {
            this.theBox.dispose();
            this.cancelOption();
        } else if (o == this.nothingButton) {
            this.selection.setVisible(false);
        }
    }
}

