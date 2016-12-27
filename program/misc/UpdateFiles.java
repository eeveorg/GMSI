/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import program.Job;
import program.Program;
import program.misc.Log;
import program.misc.MpqHandling;
import program.misc.WC3Files;
import program.ui.Settings;
import script.Stopwatch;
import wcData.objects.WC3DefaultValues;

public class UpdateFiles
implements Job {
    Boolean inited = false;
    static Pattern klammer = Pattern.compile("\\[\\w*\\]");
    static Pattern inhalt = Pattern.compile("(\\w)+");

    private static LinkedList<StringPair> readRequired() {
        try {
            String line;
            BufferedReader b = new BufferedReader(new FileReader(new File("misc/required.req")));
            String section = "";
            LinkedList<StringPair> result = new LinkedList<StringPair>();
            while ((line = b.readLine()) != null) {
                Matcher m = klammer.matcher(line);
                if (m.matches()) {
                    m = inhalt.matcher(line);
                    if (!m.find()) continue;
                    section = m.group();
                    continue;
                }
                if (section.equals("")) continue;
                result.add(new StringPair(section, line));
            }
            return result;
        }
        catch (FileNotFoundException e) {
            Log.exception(e);
        }
        catch (IOException e) {
            Log.exception(e);
        }
        return null;
    }

    public static boolean updateFiles() throws MpqHandling.MpqError, IOException {
        LinkedList<StringPair> toExport;
        if (!WC3Files.isInited()) {
            try {
                WC3Files.init();
            }
            catch (MpqHandling.MpqError e) {
                Log.exception(e);
                return false;
            }
            catch (IOException e) {
                Log.exception(e);
                JOptionPane.showConfirmDialog(Program.getMainFrame(), "Unable to synchronize with WC3, because you haven't set your WC3 Folder in the Settings.\n\nSet a WC3 folder and then try to synchronize again", "WC3 Folder not set!", -1, 2);
                Settings.getFolders().setVisible(true);
                return false;
            }
        }
        if ((toExport = UpdateFiles.readRequired()) == null) {
            return false;
        }
        int[] result = new int[5];
        int i = 0;
        while (i < 5) {
            result[i] = 0;
            ++i;
        }
        if (Log.doLog(0, 3)) {
            Log.println("Trying to update " + toExport.size() + " files");
        }
        for (StringPair s : toExport) {
            int[] arrn = result;
            int n = UpdateFiles.export(s.value, "wc3data\\" + s.key + "\\" + new File(s.value).getName());
            arrn[n] = arrn[n] + 1;
        }
        if (Log.doLog(0, 3)) {
            Log.println("Update finished: Found " + result[1] + " files in war3patch.mpq, " + result[2] + " files in war3x.mpq, " + result[3] + " files in war3xlocal.mpq, " + result[4] + " files in war3.mpq. " + result[0] + " files were missing ");
        }
        try {
            WC3DefaultValues.refreshGDBsFromSLKs();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int export(String path, String newpath) throws MpqHandling.MpqError, IOException {
        File dir = new File(new File(newpath).getParent());
        dir.mkdirs();
        File war3 = WC3Files.getWar3();
        File war3x = WC3Files.getWar3x();
        File war3patch = WC3Files.getWar3patch();
        File war3xlocal = WC3Files.getWar3xlocal();
        if (MpqHandling.ExtractFile(war3patch.getCanonicalPath(), path, newpath)) {
            if (Log.doLog(0, 4)) {
                Log.println("Export info: File \"" + path + "\" found in war3patch file");
            }
            return 1;
        }
        if (MpqHandling.ExtractFile(war3xlocal.getCanonicalPath(), path, newpath)) {
            if (Log.doLog(0, 4)) {
                Log.println("Export info: File \"" + path + "\" found in war3xlocal file");
            }
            return 3;
        }
        if (MpqHandling.ExtractFile(war3x.getCanonicalPath(), path, newpath)) {
            if (Log.doLog(0, 4)) {
                Log.println("Export info: File \"" + path + "\" found in war3x file");
            }
            return 2;
        }
        if (MpqHandling.ExtractFile(war3.getCanonicalPath(), path, newpath)) {
            if (Log.doLog(0, 4)) {
                Log.println("Export info: File \"" + path + "\" found in war3 file");
            }
            return 4;
        }
        if (Log.doLog(0, 2)) {
            Log.println("Export warning: Found \"" + path + "\" neither in the map file nor in any warcraft 3 mpq file. Export failed");
        }
        return 0;
    }

    @Override
    public boolean execute() {
        if (Log.doLog(0, 0)) {
            Log.println("\n\n--- Synchronizing data with your WC3 installation ---");
        }
        Stopwatch.reset("sync");
        Stopwatch.start("sync");
        try {
            if (UpdateFiles.updateFiles()) {
                Program.getIni().setPropertyBool("misc", "syncdone", true);
            }
        }
        catch (MpqHandling.MpqError e) {
            Log.exception(e);
        }
        catch (IOException e) {
            Log.exception(e);
        }
        if (Log.doLog(0, 0)) {
            Log.println("--- Finished synchronization with WC3. Time: " + Stopwatch.stopString("sync") + " sec ---");
        }
        return false;
    }

    private static class StringPair {
        public String key;
        public String value;

        public StringPair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}

