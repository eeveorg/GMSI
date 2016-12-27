/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;
import script.systemCalls.TrapHandler;

public class Trap_CopyFile
extends Trap {
    public Trap_CopyFile(Script s, TrapHandler handler, String string) {
        super(s, handler, "copyFile");
    }

    private static File copyFile(File src, File dest, boolean forceOverwrite) throws IOException {
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

    @Override
    public DataObject apply() throws InternalScriptError {
        String in = this.getStrParam(0, true);
        String out = this.getStrParam(1, true);
        boolean overwrite = this.getBoolParam(2, true);
        File inF = this.strToFile(in);
        File outF = this.strToFile(out);
        if (!inF.exists()) {
            throw new InternalScriptError("Copy file error: The input file <" + in + "> does not exist!");
        }
        if (inF.isDirectory()) {
            throw new InternalScriptError("Copy file error: The input file is a directory!");
        }
        File dir = outF.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try {
            Trap_CopyFile.copyFile(inF, outF, overwrite);
        }
        catch (IOException e) {
            throw new InternalScriptError("Copy file error: " + e.getMessage());
        }
        return voidResult;
    }
}

