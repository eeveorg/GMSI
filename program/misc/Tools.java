/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import program.misc.Log;

public class Tools {
    public static String insertSlashBefore(String s) {
        if (s == null || s.equals("") || s.charAt(0) == '/' || s.charAt(0) == '\\') {
            return s;
        }
        return "/" + s;
    }

    public static String insertSlashAfter(String s) {
        if (s == null || s.equals("") || s.charAt(s.length() - 1) == '/' || s.charAt(s.length() - 1) == '\\') {
            return s;
        }
        return String.valueOf(s) + "/";
    }

    public static void deleteFileRecursive(File file) {
        if (file.isDirectory()) {
            File[] arrfile = file.listFiles();
            int n = arrfile.length;
            int n2 = 0;
            while (n2 < n) {
                File f = arrfile[n2];
                Tools.deleteFileRecursive(f);
                ++n2;
            }
        }
        if (file.delete()) {
            if (Log.doLog(1, 3)) {
                Log.println("Deleting file " + file.getAbsolutePath());
            }
        } else {
            throw new RuntimeException("Critical IO Error: Cannot delete " + file.getAbsolutePath() + "!");
        }
    }

    public static void deleteFile(File file) {
        if (file.delete()) {
            if (Log.doLog(1, 3)) {
                Log.println("Deleting file " + file.getAbsolutePath());
            }
        } else {
            throw new RuntimeException("Critical IO Error: Cannot delete " + file.getAbsolutePath() + "!");
        }
    }

    public static LinkedList<File> findFiles(File startdir, String extensionPattern) {
        LinkedList<File> files = new LinkedList<File>();
        Stack<File> dirs = new Stack<File>();
        Pattern p = null;
        if (extensionPattern != null) {
            p = Pattern.compile(extensionPattern, 2);
        }
        if (startdir.isDirectory()) {
            dirs.push(startdir);
        }
        while (dirs.size() > 0) {
            File[] arrfile = ((File)dirs.pop()).listFiles();
            int n = arrfile.length;
            int n2 = 0;
            while (n2 < n) {
                File file = arrfile[n2];
                if (file.isDirectory()) {
                    dirs.push(file);
                } else if (p == null || p.matcher(file.getName()).matches()) {
                    files.add(file);
                }
                ++n2;
            }
        }
        return files;
    }

    private static LinkedHashMap<String, File> buildFileMapFromPaths(File basePath, LinkedList<String> subpaths) {
        LinkedHashMap<String, File> result = new LinkedHashMap<String, File>();
        for (String s : subpaths) {
            File cur = new File(String.valueOf(basePath.getAbsolutePath()) + "\\" + s);
            result.put(s, cur);
        }
        return result;
    }

    public static void updateFolder(File source, File target) throws IOException {
        System.out.println("Updating folder " + target.getAbsolutePath() + " from " + source.getAbsolutePath() + "...");
        int kept = 0;
        int deleted = 0;
        int added = 0;
        int updated = 0;
        if (!source.isDirectory()) {
            throw new RuntimeException("source is no folder!");
        }
        if (!target.exists()) {
            target.mkdirs();
        }
        if (!target.isDirectory()) {
            throw new RuntimeException("target is no folder!");
        }
        LinkedHashMap<String, File> sourceFiles = Tools.buildFileMapFromPaths(source, Tools.findPaths(source, null, true));
        LinkedHashMap<String, File> targetFiles = Tools.buildFileMapFromPaths(target, Tools.findPaths(target, null, true));
        for (String s : targetFiles.keySet()) {
            File curFile = targetFiles.get(s);
            File sourceFile = sourceFiles.get(s);
            if (!curFile.exists()) {
                throw new Error("WTF file not exists!");
            }
            if (sourceFile == null || !sourceFile.exists()) {
                System.out.println(String.valueOf(s) + " doesn't exist in the update directory -> delete!");
                curFile.delete();
                ++deleted;
            } else if (!(curFile.isDirectory() || sourceFile.lastModified() <= curFile.lastModified() && sourceFile.length() == curFile.length())) {
                System.out.println(String.valueOf(s) + " was updated -> replace!");
                Tools.copyFile(sourceFile, curFile, true);
                ++updated;
            } else {
                ++kept;
            }
            sourceFiles.remove(s);
        }
        for (String s : sourceFiles.keySet()) {
            File sourceFile = sourceFiles.get(s);
            File targetFile = new File(String.valueOf(target.getAbsolutePath()) + "\\" + s);
            if (sourceFile.isDirectory()) {
                if (targetFile.exists()) continue;
                System.out.println(String.valueOf(s) + " is new -> add!");
                targetFile.mkdir();
                ++added;
                continue;
            }
            System.out.println(String.valueOf(s) + "  is new -> add!");
            Tools.copyFile(sourceFile, targetFile, false);
            ++added;
        }
        System.out.println("Finished update:\n\t" + kept + " files kept\n\t" + updated + " files updated\n\t" + deleted + " files deleted\n\t" + added + " files added");
    }

    public static void copyFolder(File source, File target) throws IOException {
        if (!source.isDirectory()) {
            throw new RuntimeException("source is no folder!");
        }
        if (!target.exists()) {
            target.mkdirs();
        }
        if (!target.isDirectory()) {
            throw new RuntimeException("target is no folder!");
        }
        LinkedList<String> filesToCopy = Tools.findPaths(source, null, false);
        for (String s : filesToCopy) {
            File sourceFile = new File(String.valueOf(source.getAbsolutePath()) + "\\" + s);
            File destFile = new File(String.valueOf(target.getAbsolutePath()) + "\\" + s);
            Tools.copyFile(sourceFile, destFile, false);
        }
    }

    public static LinkedList<String> findPaths(File startdir, String extensionPattern, boolean listFolders) {
        Pattern p = null;
        if (extensionPattern != null) {
            p = Pattern.compile(extensionPattern, 2);
        }
        return Tools.findPathsIntern(null, startdir, p, listFolders);
    }

    private static LinkedList<String> findPathsIntern(String prefix, File startdir, Pattern p, boolean listFolders) {
        if (!startdir.isDirectory()) {
            return new LinkedList<String>();
        }
        LinkedList<String> files = new LinkedList<String>();
        File[] arrfile = startdir.listFiles();
        int n = arrfile.length;
        int n2 = 0;
        while (n2 < n) {
            File file = arrfile[n2];
            if (file.isDirectory()) {
                files.addAll(Tools.findPathsIntern(prefix == null ? file.getName() : String.valueOf(prefix) + "\\" + file.getName(), file, p, listFolders));
                if (listFolders) {
                    files.add(prefix == null ? file.getName() : String.valueOf(prefix) + "\\" + file.getName());
                }
            } else if (p == null || p.matcher(file.getName()).matches()) {
                files.add(prefix == null ? file.getName() : String.valueOf(prefix) + "\\" + file.getName());
            }
            ++n2;
        }
        return files;
    }

    public static File copyFile(File src, File dest, boolean forceOverwrite) throws IOException {
        if (Log.doLog(1, 3)) {
            Log.println("Copying file from \"" + src.getAbsolutePath() + "\" to \"" + dest.getAbsolutePath() + "\"");
        }
        if (dest.exists()) {
            if (forceOverwrite) {
                dest.delete();
            } else {
                throw new IOException("Cannot overwrite existing file: " + dest);
            }
        }
        dest.getAbsoluteFile().getParentFile().mkdirs();
        byte[] buffer = new byte[4096];
        int read = 0;
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        }
        return dest;
    }
}

