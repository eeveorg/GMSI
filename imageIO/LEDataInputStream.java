/*
 * Decompiled with CFR 0_119.
 */
package imageIO;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LEDataInputStream
extends FilterInputStream
implements DataInput {
    DataInputStream dataIn;

    public LEDataInputStream(InputStream in) {
        super(in);
        this.dataIn = new DataInputStream(in);
    }

    @Override
    public void close() throws IOException {
        this.dataIn.close();
    }

    @Override
    public final synchronized int read(byte[] b) throws IOException {
        return this.dataIn.read(b, 0, b.length);
    }

    @Override
    public final synchronized int read(byte[] b, int off, int len) throws IOException {
        int rl = this.dataIn.read(b, off, len);
        return rl;
    }

    @Override
    public final void readFully(byte[] b) throws IOException {
        this.dataIn.readFully(b, 0, b.length);
    }

    @Override
    public final void readFully(byte[] b, int off, int len) throws IOException {
        this.dataIn.readFully(b, off, len);
    }

    @Override
    public final int skipBytes(int n) throws IOException {
        return this.dataIn.skipBytes(n);
    }

    @Override
    public final boolean readBoolean() throws IOException {
        int ch = this.dataIn.read();
        if (ch < 0) {
            throw new EOFException();
        }
        if (ch != 0) {
            return true;
        }
        return false;
    }

    @Override
    public final byte readByte() throws IOException {
        int ch = this.dataIn.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (byte)ch;
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        int ch = this.dataIn.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    @Override
    public final short readShort() throws IOException {
        int ch2;
        int ch1 = this.dataIn.read();
        if ((ch1 | (ch2 = this.dataIn.read())) < 0) {
            throw new EOFException();
        }
        return (short)((ch1 << 0) + (ch2 << 8));
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        int ch2;
        int ch1 = this.dataIn.read();
        if ((ch1 | (ch2 = this.dataIn.read())) < 0) {
            throw new EOFException();
        }
        return (ch1 << 0) + (ch2 << 8);
    }

    @Override
    public final char readChar() throws IOException {
        int ch2;
        int ch1 = this.dataIn.read();
        if ((ch1 | (ch2 = this.dataIn.read())) < 0) {
            throw new EOFException();
        }
        return (char)((ch1 << 0) + (ch2 << 8));
    }

    @Override
    public final int readInt() throws IOException {
        int ch4;
        int ch3;
        int ch2;
        int ch1 = this.dataIn.read();
        if ((ch1 | (ch2 = this.dataIn.read()) | (ch3 = this.dataIn.read()) | (ch4 = this.dataIn.read())) < 0) {
            throw new EOFException();
        }
        return (ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24);
    }

    @Override
    public final long readLong() throws IOException {
        int i1 = this.readInt();
        int i2 = this.readInt();
        return ((long)i1 & 0xFFFFFFFFL) + (long)(i2 << 32);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public final String readLine() throws IOException {
        return new String();
    }

    @Override
    public final String readUTF() throws IOException {
        return new String();
    }

    public static final String readUTF(DataInput in) throws IOException {
        return new String();
    }
}

