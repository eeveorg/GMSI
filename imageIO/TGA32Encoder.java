/*
 * Decompiled with CFR 0_119.
 */
package imageIO;

import imageIO.LEDataOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class TGA32Encoder {
    public static void encodeTGA(BufferedImage image, File outFile) throws IOException {
        LEDataOutputStream out = new LEDataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        int w = image.getWidth();
        int h = image.getHeight();
        out.write(0);
        out.write(0);
        out.write(2);
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);
        out.writeShort(0);
        out.writeShort(0);
        out.writeShort(w);
        out.writeShort(h);
        out.write(32);
        out.write(8);
        int width = image.getWidth();
        int height = image.getHeight();
        int y = height - 1;
        while (y > -1) {
            byte[] outBuf = new byte[width * 4];
            int x = 0;
            while (x < width) {
                int color = image.getRGB(x, y);
                outBuf[x * 4 + 3] = (byte)((color & -16777216) >> 24);
                outBuf[x * 4 + 2] = (byte)((color & 16711680) >> 16);
                outBuf[x * 4 + 1] = (byte)((color & 65280) >> 8);
                outBuf[x * 4] = (byte)((color & 255) >> 0);
                ++x;
            }
            out.write(outBuf);
            --y;
        }
        out.close();
    }
}

