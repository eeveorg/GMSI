/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import program.misc.Log;

public class SLK_File {
    private HashMap<String, HashMap<String, Object>> table;
    public String name = "";
    private static Pattern intPattern = Pattern.compile("[-]?\\d+");
    private static Pattern floatPattern = Pattern.compile("[-]?\\d+\\.\\d+");

    public SLK_File(String fileName) throws IOException {
        int c;
        String[] s;
        Object[][] values;
        this.name = fileName;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        if (Log.doLog(1, 4)) {
            Log.println("Loading SLK File " + fileName);
        }
        if (!br.readLine().startsWith("ID") && Log.doLog(2, 1)) {
            Log.println("Required field \"ID\" not found in " + fileName);
        }
        while (!(s = br.readLine().split(";"))[0].startsWith("B")) {
        }
        if (s[0].startsWith("B")) {
            int x = -1;
            int y = -1;
            int i = 1;
            while (i < s.length) {
                if (s[i].startsWith("X")) {
                    x = Integer.valueOf(s[i].substring(1));
                } else if (s[i].startsWith("Y")) {
                    y = Integer.valueOf(s[i].substring(1));
                }
                ++i;
            }
            values = new Object[y][x];
        } else {
            if (Log.doLog(2, 2)) {
                Log.println("Required field \"B\" not found.");
            }
            if (Log.doLog(2, 2)) {
                Log.println("Using default 1024x1024 table size");
            }
            values = new Object[1024][1024];
        }
        int currRow = -1;
        int currCol = -1;
        Object value = null;
        int val = 0;
        while ((c = br.read()) != -1) {
            if (c == 13) {
                br.read();
                continue;
            }
            if (c == 67) {
                br.skip(2);
                val = 0;
                while ((c = br.read()) != 59) {
                    val = val * 10 + c - 48;
                }
                currRow = val;
                c = br.read();
                if (c == 89) {
                    val = 0;
                    while ((c = br.read()) != 59) {
                        val = val * 10 + c - 48;
                    }
                    currCol = val;
                    c = br.read();
                }
                if (c == 75) {
                    value = this.readField(br.readLine());
                } else if (Log.doLog(2, 2)) {
                    Log.println("SLK error!");
                }
                values[currCol - 1][currRow - 1] = value;
                continue;
            }
            if (c == 69) break;
            br.readLine();
            values[currCol - 1][currRow - 1] = value;
        }
        this.table = new HashMap(values.length);
        int i = 1;
        while (i < values.length) {
            HashMap<String, Object> tmpTable = new HashMap<String, Object>(values[i].length);
            this.table.put((String)values[i][0], tmpTable);
            int j = 1;
            while (j < values[i].length) {
                if (values[i][j] != null) {
                    tmpTable.put((String)values[0][j], values[i][j]);
                }
                ++j;
            }
            ++i;
        }
        br.close();
    }

    public HashMap<String, Object> getRow(String rowKey) {
        return this.table.get(rowKey);
    }

    public HashMap<String, HashMap<String, Object>> getTable() {
        return this.table;
    }

    public Object getField(String rowKey, String colKey) {
        return this.table.get(rowKey).get(colKey);
    }

    public Set<String> getKeys() {
        return this.table.keySet();
    }

    private Object readField(String fieldBlock) {
        Object value = null;
        if (fieldBlock.startsWith("\"")) {
            value = !fieldBlock.matches("\"\\s*[-_]\\s*\"") ? fieldBlock.substring(1, fieldBlock.length() - 1) : null;
        } else if (intPattern.matcher(fieldBlock).matches()) {
            value = Integer.parseInt(fieldBlock);
        } else if (floatPattern.matcher(fieldBlock).matches()) {
            value = Float.valueOf(Float.parseFloat(fieldBlock));
        } else if (fieldBlock.equals("TRUE")) {
            value = true;
        } else if (fieldBlock.equals("FALSE")) {
            value = false;
        } else if (!fieldBlock.equals("#VALUE!") && Log.doLog(2, 2)) {
            Log.println("Unable to parse fieldBlock " + fieldBlock);
        }
        return value;
    }

    public void printUnit(String unit_name) {
        HashMap<String, Object> s = this.table.get(unit_name);
        if (s != null) {
            for (String t : s.keySet()) {
                System.out.println(String.valueOf(t) + ": " + s.get(t));
            }
        }
    }
}

