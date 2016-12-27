/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.File;
import java.io.IOException;
import program.Program;
import program.misc.MpqHandling;

public class WC3Files {
    static String wc3folder;
    static File war3;
    static File war3x;
    static File war3xlocal;
    static File war3patch;
    static Boolean inited;

    static {
        inited = false;
    }

    public static boolean isInited() {
        return inited;
    }

    public static File getWar3() {
        if (!inited.booleanValue()) {
            return null;
        }
        return war3;
    }

    public static File getWar3x() {
        if (!inited.booleanValue()) {
            return null;
        }
        return war3x;
    }

    public static File getWar3xlocal() {
        if (!inited.booleanValue()) {
            return null;
        }
        return war3xlocal;
    }

    public static File getWar3patch() {
        if (!inited.booleanValue()) {
            return null;
        }
        return war3patch;
    }

    public static void init() throws MpqHandling.MpqError, IOException {
        wc3folder = Program.getIni().getPropertyString("folders", "wc3Folder", "");
        if (wc3folder.equals("")) {
            throw new IOException("Export error: You haven't specified a wc3 folder in the settings!");
        }
        if (!wc3folder.endsWith("/") && !wc3folder.endsWith("\\")) {
            wc3folder = String.valueOf(wc3folder) + "\\";
        }
        if (!(WC3Files.war3 = new File(String.valueOf(wc3folder) + "war3.mpq")).exists()) {
            throw new MpqHandling.MpqError("Export error: war3.mpq not found in your wc3 folder, check if you have entered the right folder at gmsi.ini");
        }
        war3x = new File(String.valueOf(wc3folder) + "war3x.mpq");
        if (!war3x.exists()) {
            throw new MpqHandling.MpqError("Export error: war3x.mpq not found in your wc3 folder, check if you have entered the right folder at gmsi.ini");
        }
        war3xlocal = new File(String.valueOf(wc3folder) + "war3xlocal.mpq");
        if (!war3xlocal.exists()) {
            throw new MpqHandling.MpqError("Export error: war3xlocal.mpq not found in your wc3 folder, check if you have entered the right folder at gmsi.ini");
        }
        war3patch = new File(String.valueOf(wc3folder) + "war3patch.mpq");
        if (!war3patch.exists()) {
            throw new MpqHandling.MpqError("Export error: war3patch.mpq not found in your wc3 folder, check if you have entered the right folder at gmsi.ini");
        }
        inited = true;
    }
}

