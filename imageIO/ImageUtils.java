/*
 * Decompiled with CFR 0_119.
 */
package imageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ImageUtils {
    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        int w;
        int h;
        int type = img.getTransparency() == 1 ? 1 : 2;
        BufferedImage ret = img;
        if (higherQuality) {
            w = img.getWidth();
            h = img.getHeight();
        } else {
            w = targetWidth;
            h = targetHeight;
        }
        do {
            if (higherQuality && w > targetWidth && (w /= 2) < targetWidth) {
                w = targetWidth;
            }
            if (higherQuality && h > targetHeight && (h /= 2) < targetHeight) {
                h = targetHeight;
            }
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();
            ret = tmp;
        } while (w != targetWidth || h != targetHeight);
        return ret;
    }
}

