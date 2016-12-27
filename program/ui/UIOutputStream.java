/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.text.AttributeSet;
import program.misc.LogWriter;

public class UIOutputStream
extends OutputStream {
    private LogWriter writer;
    private AttributeSet attrs;

    public UIOutputStream(LogWriter w, AttributeSet attrs) {
        this.writer = w;
        this.attrs = attrs;
    }

    @Override
    public void write(int b) throws IOException {
        this.writer.append("" + (char)b, this.attrs);
    }

    @Override
    public void write(byte[] bs, int offset, int length) throws IOException {
        this.writer.append(new String(bs, offset, length), this.attrs);
    }
}

