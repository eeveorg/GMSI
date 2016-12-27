/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.IntVarSpace;
import script.names.VarSpace;

public class WTS_File {
    private static final String start = "\u00ef\u00bb\u00bf";

    public static ArrayObject read(File in) throws IOException {
        String line;
        ArrayObject res = new ArrayObject();
        BufferedReader r = new BufferedReader(new FileReader(in));
        IntVarSpace v = res.getIntSpace();
        r.skip(3);
        block2 : while ((line = r.readLine()) != null) {
            if (line.length() <= 0 || line.charAt(0) != 'S') continue;
            int num = Integer.parseInt(line.substring(7));
            while ((line = r.readLine()).length() <= 0 || line.charAt(0) != '{') {
            }
            StringBuilder b = new StringBuilder(line.length());
            do {
                if ((line = r.readLine()).length() > 0 && line.charAt(0) == '}') {
                    try {
                        v.put(num, new StringObject(b.substring(0, b.length() - 2)));
                        continue block2;
                    }
                    catch (InternalScriptError e) {
                        throw new Error(e);
                    }
                }
                b.append(line);
                b.append("\r\n");
            } while (true);
        }
        r.close();
        return res;
    }

    public static void write(File out, VarSpace v) throws IOException, InternalScriptError {
        BufferedWriter r = new BufferedWriter(new FileWriter(out));
        r.write("\u00ef\u00bb\u00bf");
        for (Object o : v.getKeys()) {
            r.write("STRING ");
            r.write(o.toString());
            r.write("\r\n{\r\n");
            r.write(v.get(o).toString());
            r.write("\r\n}\r\n\r\n");
        }
        r.close();
    }
}

