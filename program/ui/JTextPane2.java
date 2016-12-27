/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class JTextPane2
extends JTextPane {
    private static final long serialVersionUID = 1;
    boolean wrap = true;

    public JTextPane2() {
    }

    public JTextPane2(boolean wrap) {
        this.wrap = wrap;
    }

    public JTextPane2(StyledDocument doc) {
        super(doc);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (this.wrap) {
            return super.getScrollableTracksViewportWidth();
        }
        return false;
    }

    @Override
    public void setSize(Dimension d) {
        if (!this.wrap && d.width < this.getParent().getSize().width) {
            d.width = this.getParent().getSize().width;
        }
        super.setSize(d);
    }

    void setLineWrap(boolean wrap) {
        this.setVisible(false);
        this.wrap = wrap;
        this.setVisible(true);
    }
}

