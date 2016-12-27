/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.ptr.IntByReference
 *  com.sun.jna.ptr.PointerByReference
 *  com.sun.jna.win32.StdCallLibrary
 */
package program.misc;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MpqHandling {
    static {
        Native.setProtected((boolean)true);
        System.setProperty("jna.library.path", new File("misc/").getAbsolutePath());
    }

    public static void RenameFile(String archiveFile, String sourceName, String destName) throws MpqError {
        Pointer archiveHandle = CLibrary.INSTANCE.MpqOpenArchiveForUpdate(archiveFile, 4, 16);
        if (archiveHandle.toString().equals("native@0xffffffff")) {
            throw new MpqError("Couldn't open archive " + archiveFile + " for update");
        }
        if (!CLibrary.INSTANCE.MpqRenameFile(archiveHandle, sourceName, destName)) {
            CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0);
            throw new MpqError("Couldn't rename " + sourceName + " in archive " + archiveFile + " to " + destName);
        }
        if (!CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0)) {
            throw new MpqError("Couldn't close archive " + archiveFile);
        }
    }

    public static void UpdateFile(String archiveFile, String sourceName, String destName) throws MpqError {
        Pointer archiveHandle = CLibrary.INSTANCE.MpqOpenArchiveForUpdate(archiveFile, 4, 16);
        if (archiveHandle.toString().equals("native@0xffffffff")) {
            throw new MpqError("Couldn't open archive " + archiveFile + " for update");
        }
        if (!CLibrary.INSTANCE.MpqAddFileToArchive(archiveHandle, sourceName, destName, 513)) {
            CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0);
            throw new MpqError("Couldn't add " + sourceName + " to archive " + archiveFile + " ( name in Archive: " + destName + ")");
        }
        if (!CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0)) {
            throw new MpqError("Couldn't close archive " + archiveFile);
        }
    }

    public static void CompactArchive(String archiveFile) throws MpqError {
        Pointer archiveHandle = CLibrary.INSTANCE.MpqOpenArchiveForUpdate(archiveFile, 4, 16);
        if (archiveHandle.toString().equals("native@0xffffffff")) {
            throw new MpqError("Couldn't open archive " + archiveFile + " for update");
        }
        if (!CLibrary.INSTANCE.MpqCompactArchive(archiveHandle)) {
            CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0);
            throw new MpqError("Couldn't compact archive " + archiveFile);
        }
        if (!CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0)) {
            throw new MpqError("Couldn't close archive " + archiveFile);
        }
    }

    public static void ReplaceFile(String archiveFile, String sourceName, String destName) throws MpqError {
        MpqHandling.DeleteFile(archiveFile, destName);
        MpqHandling.UpdateFile(archiveFile, sourceName, destName);
    }

    public static boolean DeleteFile(String archiveFile, String fileToDel) throws MpqError {
        Pointer archiveHandle = CLibrary.INSTANCE.MpqOpenArchiveForUpdate(archiveFile, 4, 16);
        if (archiveHandle.toString().equals("native@0xffffffff")) {
            throw new MpqError("Couldn't open archive " + archiveFile + " for update");
        }
        if (!CLibrary.INSTANCE.MpqDeleteFile(archiveHandle, fileToDel)) {
            CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0);
            return false;
        }
        if (!CLibrary.INSTANCE.MpqCloseUpdatedArchive(archiveHandle, 0)) {
            throw new MpqError("Couldn't close archive " + archiveFile);
        }
        return true;
    }

    public static boolean ExtractFile(String archiveFile, String sourceName, String destName) throws MpqError {
        PointerByReference archiv = new PointerByReference();
        if (!CLibrary.INSTANCE.SFileOpenArchive(archiveFile, 0, 0, archiv)) {
            throw new MpqError("Couldn't open archive " + archiveFile);
        }
        PointerByReference file = new PointerByReference();
        if (!CLibrary.INSTANCE.SFileOpenFile(sourceName, file)) {
            CLibrary.INSTANCE.SFileCloseArchive(archiv.getPointer().getPointer(0));
            return false;
        }
        byte[] b = new byte[65536];
        IntByReference i = new IntByReference();
        try {
            FileOutputStream w = new FileOutputStream(destName);
            do {
                if (!CLibrary.INSTANCE.SFileReadFile(file.getPointer().getPointer(0), b, 65536, i, null)) {
                    CLibrary.INSTANCE.SFileCloseFile(file.getPointer().getPointer(0));
                    CLibrary.INSTANCE.SFileCloseArchive(archiv.getPointer().getPointer(0));
                    throw new MpqError("Couldn't read from file " + sourceName + " in archive " + archiveFile);
                }
                if (i.getValue() <= 0) continue;
                w.write(b, 0, i.getValue());
            } while (i.getValue() > 0);
            w.close();
        }
        catch (IOException e) {
            CLibrary.INSTANCE.SFileCloseFile(file.getPointer().getPointer(0));
            CLibrary.INSTANCE.SFileCloseArchive(archiv.getPointer().getPointer(0));
            throw new MpqError("Couldn't write to file " + destName);
        }
        if (!CLibrary.INSTANCE.SFileCloseFile(file.getPointer().getPointer(0))) {
            CLibrary.INSTANCE.SFileCloseArchive(archiv.getPointer().getPointer(0));
            throw new MpqError("Couldn't close file " + sourceName + " in archive " + archiveFile);
        }
        if (!CLibrary.INSTANCE.SFileCloseArchive(archiv.getPointer().getPointer(0))) {
            throw new MpqError("Couldn't close archive " + archiveFile);
        }
        return true;
    }

    public static interface CLibrary
    extends StdCallLibrary {
        public static final CLibrary INSTANCE = (CLibrary)Native.loadLibrary((String)"SFmpq", CLibrary.class);

        public String MpqGetVersionString();

        public Pointer MpqOpenArchiveForUpdate(String var1, int var2, int var3);

        public boolean MpqAddFileToArchive(Pointer var1, String var2, String var3, int var4);

        public boolean MpqCompactArchive(Pointer var1);

        public boolean MpqCloseUpdatedArchive(Pointer var1, int var2);

        public boolean MpqRenameFile(Pointer var1, String var2, String var3);

        public boolean SFileOpenArchive(String var1, int var2, int var3, PointerByReference var4);

        public boolean SFileOpenFileEx(Pointer var1, String var2, int var3, PointerByReference var4);

        public boolean SFileReadFile(Pointer var1, byte[] var2, int var3, IntByReference var4, Pointer var5);

        public boolean SFileCloseFile(Pointer var1);

        public boolean SFileCloseArchive(Pointer var1);

        public boolean SFileOpenFile(String var1, PointerByReference var2);

        public boolean MpqDeleteFile(Pointer var1, String var2);
    }

    public static class MpqError
    extends Exception {
        private static final long serialVersionUID = 1;

        public MpqError(String s) {
            super(s);
        }
    }

}

