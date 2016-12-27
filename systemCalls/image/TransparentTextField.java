/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

class TransparentTextField
extends JLabel {
    public TransparentTextField() {
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        this.setBackground(Color.white);
        AlphaComposite alphaComp = AlphaComposite.getInstance(3, 0.5f);
        g2d.setComposite(alphaComp);
        g2d.setColor(this.getBackground());
        Rectangle tBounds = g2d.getClip().getBounds();
        g2d.fillRect((int)tBounds.getX(), (int)tBounds.getY(), (int)tBounds.getWidth(), (int)tBounds.getHeight());
        super.paintComponent(g2d);
        this.setForeground(Color.black);
    }
}

