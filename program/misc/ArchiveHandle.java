/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import program.misc.Log;
import program.misc.MpqHandling;
import program.misc.Tools;

public class ArchiveHandle {
    private File map;
    private File tempIn;
    private File tempOut;
    private HashSet<String> extractedFiles = new HashSet();

    public ArchiveHandle(File mapFile, File tempIn, File tempOut) throws IOException {
        if (!mapFile.exists() || !mapFile.getName().endsWith(".w3x")) {
            throw new IOException(String.valueOf(mapFile.getAbsolutePath()) + " is not a valid map file!");
        }
        if (!tempIn.exists() || !tempIn.isDirectory()) {
            throw new Error(String.valueOf(tempIn.getAbsolutePath()) + " is not a valid folder!");
        }
        if (!tempOut.exists() || !tempOut.isDirectory()) {
            throw new Error(String.valueOf(tempIn.getAbsolutePath()) + " is not a valid folder!");
        }
        this.map = mapFile;
        this.tempIn = tempIn;
        this.tempOut = tempOut;
    }

    public File getTempOutPath() {
        return this.tempOut;
    }

    public File getFile(String fileName) {
        if (this.extractedFiles.contains(fileName)) {
            return new File(String.valueOf(this.tempIn.getAbsolutePath()) + "/" + fileName);
        }
        if (!this.extract(fileName)) {
            return null;
        }
        this.extractedFiles.add(fileName);
        return new File(String.valueOf(this.tempIn.getAbsolutePath()) + "/" + fileName);
    }

    public File copyMap(File outpath) throws IOException {
        return Tools.copyFile(this.map, outpath, true);
    }

    public File getMapFile() {
        return this.map;
    }

    private boolean extract(String filename) {
        try {
            if (Log.doLog(1, 3)) {
                Log.print("Trying to extract " + filename + " from " + this.map.getAbsolutePath());
            }
            boolean result = MpqHandling.ExtractFile(this.map.getAbsolutePath(), filename, String.valueOf(this.tempIn.getAbsolutePath()) + "/" + filename);
            if (Log.doLog(1, 3)) {
                if (result) {
                    Log.println(" --> found");
                } else {
                    Log.println(" --> not found!");
                }
            }
            return result;
        }
        catch (MpqHandling.MpqError e) {
            Log.exception(e);
            return false;
        }
    }

    public void updateFromTempOutput(File outpath) {
        LinkedList<String> files = Tools.findPaths(this.tempOut, null, false);
        for (String s : files) {
            File f = new File(this.getTempOutPath() + "\\" + s);
            try {
                if (Log.doLog(1, 3)) {
                    Log.println("Adding " + f.getAbsolutePath() + " to archive " + outpath.getAbsolutePath());
                }
                MpqHandling.ReplaceFile(outpath.getAbsolutePath(), f.getAbsolutePath(), s);
                continue;
            }
            catch (MpqHandling.MpqError e) {
                throw new RuntimeException(e);
            }
        }
    }
}

