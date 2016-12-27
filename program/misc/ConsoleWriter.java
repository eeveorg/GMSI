/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.PrintStream;
import javax.swing.text.AttributeSet;
import program.misc.LogWriter;

public class ConsoleWriter
implements LogWriter {
    @Override
    public void append(String message, AttributeSet attrs) {
        System.out.print(message);
    }
}

