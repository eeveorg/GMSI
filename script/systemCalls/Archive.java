/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class Archive {
    static void create(File f) throws IOException {
        BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(f));
        w.write(new byte[]{1, 3, 3, 7});
        w.write(new byte[4]);
        w.close();
    }

    static int addFile(File archive, File add, String name) throws IOException {
        if (!add.exists()) {
            throw new IOException("File to be added does not exist!");
        }
        if (add.isDirectory()) {
            throw new IOException("Cannot add directories!");
        }
        if (!archive.exists()) {
            Archive.create(archive);
        }
        if (archive.isDirectory()) {
            throw new IOException("Archive is no valid gsl archive!");
        }
        RandomAccessFile r = new RandomAccessFile(archive, "rw");
        int code = r.readInt();
        if (code != 16974599) {
            throw new IOException("Archive is no valid gsl archive!");
        }
        int fileCount = r.readInt();
        r.seek(4);
        r.writeInt(fileCount + 1);
        r.seek(r.length());
        RandomAccessFile bi = new RandomAccessFile(add, "r");
        r.writeInt((int)bi.length());
        r.writeBytes(name);
        r.write(0);
        byte[] swap = new byte[(int)bi.length()];
        bi.readFully(swap);
        r.write(swap);
        bi.close();
        r.close();
        return fileCount + 1;
    }

    public static void main(String[] args) throws IOException {
    }
}

