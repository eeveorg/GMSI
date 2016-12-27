/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import javax.swing.JScrollBar;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import program.misc.ConsoleWriter;
import program.misc.LogWriter;
import script.EvaluationError;

public class Log {
    private static int loglevel;
    private static File logfile;
    private static BufferedWriter logWriter;
    private static boolean logFileEnabled;
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int MOREINFO = 4;
    public static final int DEBUG = 5;
    public static final int CAPTION = 0;
    public static final int GLOBAL = 0;
    public static final int IO = 1;
    public static final int INTERNAL = 2;
    public static final int SCRIPT = 3;
    private static final SimpleAttributeSet normal;
    public static final SimpleAttributeSet error;
    public static final SimpleAttributeSet warning;
    private static final SimpleAttributeSet echo;
    private static final SimpleAttributeSet caption;
    private static final SimpleAttributeSet test;
    private static final SimpleAttributeSet highlight;
    public static JScrollBar scroll;
    private static LogWriter writer;
    private static LogWriter errorWriter;
    private static int[] categoryLevels;
    private static int curLevel;
    private static int curCategory;

    static {
        logWriter = null;
        logFileEnabled = false;
        normal = new SimpleAttributeSet();
        error = new SimpleAttributeSet();
        warning = new SimpleAttributeSet();
        echo = new SimpleAttributeSet();
        caption = new SimpleAttributeSet();
        test = new SimpleAttributeSet();
        highlight = new SimpleAttributeSet();
        writer = new ConsoleWriter();
        errorWriter = null;
        categoryLevels = new int[10];
        Log.categoryLevels[0] = 4;
        Log.categoryLevels[1] = 4;
        Log.categoryLevels[2] = 4;
        Log.categoryLevels[3] = 4;
        StyleConstants.setForeground(normal, Color.green);
        StyleConstants.setBold(normal, false);
        StyleConstants.setItalic(caption, true);
        StyleConstants.setForeground(caption, Color.green);
        StyleConstants.setForeground(warning, Color.yellow);
        StyleConstants.setForeground(error, Color.red);
        StyleConstants.setBold(warning, true);
        StyleConstants.setBold(error, false);
        StyleConstants.setForeground(echo, Color.white);
        StyleConstants.setForeground(highlight, Color.decode("0xFF8888"));
        StyleConstants.setBold(highlight, true);
        StyleConstants.setForeground(test, Color.orange);
    }

    public static boolean doLog(int logCategory, int logLevel) {
        if (categoryLevels[logCategory] < logLevel) {
            return false;
        }
        curLevel = logLevel;
        curCategory = logCategory;
        return true;
    }

    public static void setLogWriter(LogWriter l) {
        writer = l;
    }

    public static void setErrorWriter(LogWriter l) {
        errorWriter = l;
    }

    public static int getLogLevel() {
        return loglevel;
    }

    private static AttributeSet getAttrSet() {
        if (curLevel == 2) {
            return warning;
        }
        if (curLevel == 1) {
            return error;
        }
        if (curLevel == 0) {
            return caption;
        }
        return null;
    }

    public static void print(String message) {
        writer.append(message, Log.getAttrSet());
        if (curLevel <= 2 && errorWriter != null) {
            errorWriter.append(message, Log.getAttrSet());
        }
    }

    public static void println(String message) {
        writer.append(String.valueOf(message) + "\n", Log.getAttrSet());
        if (curLevel <= 2 && errorWriter != null) {
            errorWriter.append(String.valueOf(message) + "\n", Log.getAttrSet());
        }
    }

    public static void printError(String message) {
        writer.append(message, Log.getAttrSet());
    }

    public static void println(String message, AttributeSet s) {
        writer.append(String.valueOf(message) + "\n", s);
        if (curLevel <= 2 && errorWriter != null) {
            errorWriter.append(String.valueOf(message) + "\n", s);
        }
    }

    public static void exception(Throwable a) {
        curLevel = 1;
        if (a instanceof EvaluationError) {
            EvaluationError e = (EvaluationError)a;
            Log.printError("Evaluation Error at line " + e.getLine() + " in file " + e.getSourceFile() + ":\n");
            Log.println(e.getMessage(), highlight);
            Log.printError("At code: " + e.getCode());
            if (!e.getCallStack().equals("")) {
                Log.printError(e.getCallStack());
            }
        } else {
            Log.println(a.getMessage());
            a.printStackTrace();
        }
    }

    public static void setEnableLogging(boolean enable) {
        logFileEnabled = enable;
    }

    private static String generateTabs(int level) {
        String result = "";
        int i = 1;
        while (i < level) {
            result = String.valueOf(result) + "  ";
            ++i;
        }
        return result;
    }

    public static void close() {
        Log.tofile(1, ">> Program terminated <<");
        if (logWriter != null) {
            try {
                logWriter.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public static void tofile(int level, String message) {
        if (loglevel < level || !logFileEnabled) {
            return;
        }
        try {
            logWriter.write(String.valueOf(Log.generateTabs(level)) + message + "\r\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogFile(File logfi) {
        if (!logFileEnabled) {
            return;
        }
        logfile = logfi;
        try {
            logWriter = new BufferedWriter(new FileWriter(logfile));
            logWriter.write("--- log started ---\r\nDate: " + new Date().toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLevel(int level) {
        loglevel = level;
    }
}

