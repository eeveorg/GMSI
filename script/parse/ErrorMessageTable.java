/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorMessageTable {
    private HashMap<Integer, String> errors = new HashMap();
    private Pattern zustand = Pattern.compile("\\@([0-9]+)\\:");

    public ErrorMessageTable(File f) throws IOException {
        String line;
        BufferedReader r = new BufferedReader(new FileReader(f));
        StringBuilder currentError = new StringBuilder();
        int currentNum = -1;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("@")) {
                if (currentNum != -1) {
                    this.errors.put(currentNum, currentError.substring(0, currentError.length() - 1));
                }
                Matcher m = this.zustand.matcher(line);
                line = m.replaceFirst("");
                currentNum = Integer.parseInt(m.group(1));
                currentError = new StringBuilder();
            }
            currentError.append(line);
            currentError.append("\n");
        }
        this.errors.put(currentNum, currentError.substring(0, currentError.length() - 1));
    }

    public String getErrorMessage(int zustand) {
        if (this.errors.containsKey(zustand)) {
            return this.errors.get(zustand);
        }
        return "No suggestions available";
    }
}

