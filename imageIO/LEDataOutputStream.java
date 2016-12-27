/*
 * Decompiled with CFR 0_119.
 */
package imageIO;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LEDataOutputStream
extends FilterOutputStream
implements DataOutput {
    protected int written = 0;

    public LEDataOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public synchronized void write(int b) throws IOException {
        this.out.write(b);
        ++this.written;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
        this.written += len;
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public final void writeBoolean(boolean v) throws IOException {
        this.out.write(v ? 1 : 0);
        ++this.written;
    }

    @Override
    public final void writeByte(int v) throws IOException {
        this.out.write(v);
        ++this.written;
    }

    @Override
    public final void writeShort(int v) throws IOException {
        OutputStream out = this.out;
        out.write(v >>> 0 & 255);
        out.write(v >>> 8 & 255);
        this.written += 2;
    }

    @Override
    public final void writeChar(int v) throws IOException {
        OutputStream out = this.out;
        out.write(v >>> 0 & 255);
        out.write(v >>> 8 & 255);
        this.written += 2;
    }

    @Override
    public final void writeInt(int v) throws IOException {
        OutputStream out = this.out;
        out.write(v >>> 0 & 255);
        out.write(v >>> 8 & 255);
        out.write(v >>> 16 & 255);
        out.write(v >>> 24 & 255);
        this.written += 4;
    }

    @Override
    public final void writeLong(long v) throws IOException {
        OutputStream out = this.out;
        out.write((int)(v >>> 0) & 255);
        out.write((int)(v >>> 8) & 255);
        out.write((int)(v >>> 16) & 255);
        out.write((int)(v >>> 24) & 255);
        out.write((int)(v >>> 32) & 255);
        out.write((int)(v >>> 40) & 255);
        out.write((int)(v >>> 48) & 255);
        out.write((int)(v >>> 56) & 255);
        this.written += 8;
    }

    @Override
    public final void writeFloat(float v) throws IOException {
        this.writeInt(Float.floatToIntBits(v));
    }

    @Override
    public final void writeDouble(double v) throws IOException {
        this.writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public final void writeBytes(String s) throws IOException {
        OutputStream out = this.out;
        int len = s.length();
        int i = 0;
        while (i < len) {
            out.write((byte)s.charAt(i));
            ++i;
        }
        this.written += len;
    }

    @Override
    public final void writeChars(String s) throws IOException {
        OutputStream out = this.out;
        int len = s.length();
        int i = 0;
        while (i < len) {
            char v = s.charAt(i);
            out.write(v >>> 8 & 255);
            out.write(v >>> 0 & 255);
            ++i;
        }
        this.written += len * 2;
    }

    @Override
    public final void writeUTF(String str) throws IOException {
    }

    public final int size() {
        return this.written;
    }
}

