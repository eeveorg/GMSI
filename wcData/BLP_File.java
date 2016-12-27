/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import imageIO.ImageUtils;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;

public class BLP_File {
    public static BufferedImage read(File f) throws IOException {
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(f));
        if (!in.readCharsAsString(4).equals("BLP1")) {
            throw new IOException(String.valueOf(f.getName()) + " is no valid blp file");
        }
        int type = in.readInt();
        int alpha = in.readInt();
        int width = in.readInt();
        int height = in.readInt();
        int teamColorFlag = in.readInt();
        int always1 = in.readInt();
        int[] mipmapOffsets = new int[16];
        int i = 0;
        while (i < 16) {
            mipmapOffsets[i] = in.readInt();
            System.out.println("offset" + i + ":" + mipmapOffsets[i]);
            ++i;
        }
        int[] mipmapSizes = new int[16];
        int i2 = 0;
        while (i2 < 16) {
            mipmapSizes[i2] = in.readInt();
            System.out.println(String.valueOf(i2) + ":" + mipmapSizes[i2]);
            ++i2;
        }
        if (type == 0) {
            System.out.println(f.length());
            System.out.println("jpeg!");
            int jpgHeaderSize = in.readInt();
            byte[] jpgHeader = new byte[jpgHeaderSize];
            in.read(jpgHeader);
            byte[] zeros = new byte[(int)((long)mipmapOffsets[0] - in.getOffset())];
            in.read(zeros);
            byte[][] mipmaps = new byte[16][];
            int i3 = 0;
            while (i3 < 16) {
                mipmaps[i3] = new byte[mipmapSizes[i3]];
                in.read(mipmaps[i3]);
                ++i3;
            }
            File tmpFile = new File("temp/temp.jpg");
            BufferedOutputStream tmp = new BufferedOutputStream(new FileOutputStream(tmpFile));
            tmp.write(jpgHeader);
            tmp.write(mipmaps[0]);
            tmp.close();
            BufferedInputStream read = new BufferedInputStream(new FileInputStream(new File("temp/temp.jpg")));
            JPEGImageDecoder jpg = JPEGCodec.createJPEGDecoder(read);
            Raster r = jpg.decodeAsRaster();
            read.close();
            tmpFile.delete();
            int picwidth = r.getWidth();
            int picheight = r.getHeight();
            BufferedImage b = new BufferedImage(picwidth, picheight, 6);
            int x = 0;
            while (x < picwidth) {
                int y = 0;
                while (y < picheight) {
                    int[] pix = new int[4];
                    pix = r.getPixel(x, y, pix);
                    b.setRGB(x, y, new Color(pix[2], pix[1], pix[0], pix[3]).getRGB());
                    ++y;
                }
                ++x;
            }
            return b;
        }
        if (type == 1) {
            System.out.println("paletted!");
            byte[][] palette = new byte[4][256];
            int i4 = 0;
            while (i4 < palette.length) {
                in.read(palette[i4]);
                ++i4;
            }
            byte[][] colors = new byte[width][height];
            int i5 = 0;
            while (i5 < palette.length) {
                in.read(colors[i5]);
                ++i5;
            }
            byte[][] alphaChannel = null;
            if (teamColorFlag != 5) {
                alphaChannel = new byte[width][height];
                int i6 = 0;
                while (i6 < palette.length) {
                    in.read(alphaChannel[i6]);
                    ++i6;
                }
            }
        } else {
            throw new IOException(String.valueOf(f.getName()) + " has an unknown BLP type: " + type);
        }
        return null;
    }

    private static BufferedImage[] generateMipMaps(BufferedImage input) {
        int pow;
        int num = 0;
        int curWidth = input.getWidth();
        int curHeight = input.getHeight();
        while ((pow = (int)Math.pow(2.0, ++num - 1)) < curWidth || pow < curHeight) {
        }
        BufferedImage[] result = new BufferedImage[num];
        result[0] = input;
        int i = 1;
        while (i < num) {
            curWidth /= 2;
            if ((curHeight /= 2) == 0) {
                curHeight = 1;
            }
            if (curWidth == 0) {
                curWidth = 1;
            }
            result[i] = ImageUtils.getScaledInstance(result[i - 1], curWidth, curHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            ++i;
        }
        return result;
    }

    public static void writePalettedBLP(BufferedImage b, File f, boolean useAlpha, boolean increasePalette) throws IOException {
        BlizzardDataOutputStream out = new BlizzardDataOutputStream(f);
        out.writeNByteString("BLP1", 4);
        out.writeInt(1);
        out.writeInt(useAlpha ? 8 : 0);
        out.writeInt(b.getWidth());
        out.writeInt(b.getHeight());
        out.writeInt(useAlpha ? 4 : 5);
        out.writeInt(1);
        BufferedImage[] mips = BLP_File.generateMipMaps(b);
    }

    public static void main(String[] args) throws IOException {
        File f = new File("export/omfg.blp");
        BLP_File.read(f);
    }
}

