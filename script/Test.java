/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.io.File;
import script.Script;

public class Test {
    public static void main(String[] args) throws Exception {
        Script s = new Script(null);
        s.execFile(new File("test2.gsl"), true);
    }
}

