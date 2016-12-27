/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Stopwatch {
    private static HashMap<String, Stopwatch> watches = new HashMap();
    private long startTime;
    private long countedTime = 0;
    private boolean started = false;
    private String name;
    private static DecimalFormat format = new DecimalFormat("#.000");

    static {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(s);
    }

    public Stopwatch(String name) {
        this.name = name;
    }

    public static String stopString(String watchName) {
        int millisec = (int)(Stopwatch.stop(watchName) / 1000000);
        double sec = (double)millisec / 1000.0;
        return format.format(sec);
    }

    public static void stopAndPrint(String watchName) {
        Stopwatch w = watches.get(watchName);
        if (w == null) {
            throw new RuntimeException("Unknown Stopwatch: \"" + watchName + "\"");
        }
        w.stopAndPrint();
    }

    public static void start(String watchName) {
        if (watches.get(watchName) == null) {
            watches.put(watchName, new Stopwatch(watchName));
        }
        watches.get(watchName).start();
    }

    public static long stop(String watchName) {
        Stopwatch w = watches.get(watchName);
        if (w == null) {
            throw new RuntimeException("Unknown Stopwatch: \"" + watchName + "\"");
        }
        return w.stop();
    }

    public static long get(String watchName) {
        Stopwatch w = watches.get(watchName);
        if (w == null) {
            throw new RuntimeException("Unknown Stopwatch: \"" + watchName + "\"");
        }
        return w.getTime();
    }

    public void start() {
        this.started = true;
        this.startTime = System.nanoTime();
    }

    public long stop() {
        if (this.started) {
            this.countedTime += System.nanoTime() - this.startTime;
        }
        this.started = false;
        return this.countedTime;
    }

    public long stopAndPrint() {
        if (this.started) {
            this.countedTime += System.nanoTime() - this.startTime;
        }
        this.started = false;
        System.out.println("Stopwatch " + this.name + ": " + (float)this.countedTime / 1000000.0f + " ms");
        return this.countedTime;
    }

    public void reset() {
        this.countedTime = 0;
    }

    public static void reset(String watchName) {
        Stopwatch w = watches.get(watchName);
        if (w == null) {
            return;
        }
        w.reset();
    }

    public static void resetAll() {
        for (Map.Entry<String, Stopwatch> s : watches.entrySet()) {
            s.getValue().reset();
        }
    }

    public long getTime() {
        return this.countedTime;
    }

    public static void listAll() {
        for (Map.Entry<String, Stopwatch> s : watches.entrySet()) {
            System.out.println("Stopwatch " + s.getValue().name + ": " + (float)s.getValue().getTime() / 1000000.0f + " ms");
        }
    }
}

