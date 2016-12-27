/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import program.misc.Log;
import program.misc.LogWriter;
import script.systemCalls.EchoTarget;

public class AutoscrollPane
extends JTextPane
implements LogWriter,
EchoTarget {
    private CaretThread scrollThread;
    private Object monitor = new Object();
    private static final long serialVersionUID = 1;

    public AutoscrollPane() {
        this.setEditable(false);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.scrollThread = new CaretThread(this.getDocument(), this);
        this.scrollThread.start();
        Font font = new Font("Courier New", 0, 12);
        this.setFont(font);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void append(String s, AttributeSet se) {
        Document d = this.getDocument();
        Object object = this.monitor;
        synchronized (object) {
            try {
                int i = d.getLength();
                if (i > 25000) {
                    try {
                        d.remove(0, i - 25000);
                    }
                    catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
                d.insertString(d.getLength(), s, se);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        this.scrollThread.changed();
    }

    @Override
    public void print(String s, EchoTarget.EchoType t) {
        if (t == EchoTarget.EchoType.WARNING) {
            this.append(s, Log.warning);
        } else {
            this.append(s, null);
        }
    }

    private class CaretThread
    extends Thread {
        private Document d;
        private JTextPane j;
        private boolean changes;

        public CaretThread(Document d, JTextPane j) {
            this.changes = false;
            this.d = d;
            this.j = j;
            this.setDaemon(true);
        }

        public void changed() {
            this.changes = true;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            do {
                Object object = AutoscrollPane.this.monitor;
                synchronized (object) {
                    if (this.changes) {
                        this.j.setCaretPosition(this.d.getLength());
                    }
                    this.changes = false;
                }
                try {
                    Thread.sleep(100);
                    continue;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                break;
            } while (true);
        }
    }

}

