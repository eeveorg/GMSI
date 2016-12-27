/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;

public class Trap_FileDialog
extends Trap {
    public Trap_FileDialog(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String startFolder = this.getStrParam(1, true);
        String title = this.getStrParam(0, true);
        String allowedExtensions = this.getStrParam(2, false);
        CustomFilter filter = allowedExtensions == null ? null : new CustomFilter(allowedExtensions);
        JFileChooser openChoose = new JFileChooser(startFolder);
        openChoose.setFileSelectionMode(0);
        openChoose.setMultiSelectionEnabled(false);
        openChoose.setDialogTitle(title);
        openChoose.setFileFilter(filter);
        if (openChoose.showDialog(null, "Okay") == 0) {
            return new StringObject(openChoose.getSelectedFile().getAbsolutePath());
        }
        return nullResult;
    }

    private class CustomFilter
    extends FileFilter {
        private String ext;
        private String[] exts;

        public CustomFilter(String ext) {
            this.ext = ext;
            if (ext == null) {
                this.exts = null;
                ext = "All";
                return;
            }
            this.exts = ext.split(",");
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            if (this.exts == null) {
                return true;
            }
            String fileName = f.getName().toLowerCase();
            String[] arrstring = this.exts;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String ex = arrstring[n2];
                if (fileName.endsWith(ex.toLowerCase())) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return String.valueOf(this.ext) + " files";
        }
    }

}

