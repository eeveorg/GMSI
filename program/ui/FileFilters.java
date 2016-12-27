/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileFilters {

    public static class OnlyFoldersFilter
    extends FileFilter {
        @Override
        public boolean accept(File arg0) {
            if (arg0.isDirectory()) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "directories";
        }
    }

}

