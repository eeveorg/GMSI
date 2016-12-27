/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import program.misc.IniSubscriber;

public class IniHandler {
    private boolean caseSensitive;
    private LinkedHashMap<String, LinkedHashMap<String, IniSubscriber>> subscribers = new LinkedHashMap();
    private LinkedHashMap<String, LinkedHashMap<String, String>> sections = new LinkedHashMap();
    private HashMap<IniSubscriber, String[]> subsc = new HashMap();

    public IniHandler(String pathname, boolean caseSensitive) throws FileNotFoundException, IOException {
        this(new FileReader(pathname), caseSensitive);
    }

    public IniHandler(InputStream input, boolean caseSensitive) throws FileNotFoundException, IOException {
        this(new InputStreamReader(input), caseSensitive);
    }

    public IniHandler(Reader input, boolean caseSensitive) throws FileNotFoundException, IOException {
        this.initialize(new BufferedReader(input), caseSensitive);
    }

    private String caseSens(String input) {
        if (this.caseSensitive) {
            return input;
        }
        return input.toLowerCase();
    }

    private void initialize(BufferedReader r, boolean caseSensitive) throws IOException {
        String line;
        this.caseSensitive = caseSensitive;
        int lineNum = 0;
        String section = null;
        while ((line = r.readLine()) != null) {
            line = line.trim();
            ++lineNum;
            if (line.startsWith("\u00ef\u00bb\u00bf")) {
                line = line.substring(3);
            }
            if (line.equals("") || line.startsWith(";") || line.startsWith("/")) continue;
            if (line.startsWith("[")) {
                if (!line.endsWith("]")) {
                    throw new IOException("] expected in section header on line " + lineNum);
                }
                section = this.caseSens(line.substring(1, line.length() - 1));
                continue;
            }
            if (section == null) {
                throw new IOException("[section] header expected on line " + lineNum);
            }
            int index = line.indexOf(61);
            if (index < 0) continue;
            String key = this.caseSens(line.substring(0, index).trim());
            String value = line.substring(index + 1).trim();
            LinkedHashMap map = this.sections.get(section);
            if (map == null) {
                map = new LinkedHashMap();
                this.sections.put(section, map);
            }
            map.put(key, value);
        }
        r.close();
    }

    public String getPropertyString(String section, String key, String defaultValue) {
        String value;
        if (defaultValue == null) {
            throw new Error("default value may not be null");
        }
        LinkedHashMap<String, String> map = this.sections.get(this.caseSens(section));
        if (map == null) {
            this.sections.put(this.caseSens(section), new LinkedHashMap());
            map = this.sections.get(this.caseSens(section));
        }
        if ((value = map.get(this.caseSens(key))) == null) {
            value = defaultValue;
            map.put(this.caseSens(key), defaultValue);
        }
        return value;
    }

    public int getPropertyInt(String section, String key, int defaultValue) {
        String s = this.getPropertyString(section, key, String.valueOf(defaultValue));
        return Integer.parseInt(s);
    }

    public boolean getPropertyBool(String section, String key, boolean defaultValue) {
        String s = this.getPropertyString(section, key, defaultValue ? "true" : "false");
        return s.equalsIgnoreCase("true");
    }

    private void addSubscriber(String section, String key, IniSubscriber i) {
        if (!this.subscribers.containsKey(this.caseSens(section))) {
            this.subscribers.put(this.caseSens(section), new LinkedHashMap());
        }
        this.subscribers.get(this.caseSens(section)).put(this.caseSens(key), i);
        this.subsc.put(i, new String[]{section, key});
    }

    public String subscribePropertyString(IniSubscriber i, String section, String key, String defaultValue) {
        this.addSubscriber(section, key, i);
        return this.getPropertyString(section, key, defaultValue);
    }

    public int subscribePropertyInt(IniSubscriber i, String section, String key, int defaultValue) {
        this.addSubscriber(section, key, i);
        return this.getPropertyInt(section, key, defaultValue);
    }

    public boolean subscribePropertyBool(IniSubscriber i, String section, String key, boolean defaultValue) {
        this.addSubscriber(section, key, i);
        return this.getPropertyBool(section, key, defaultValue);
    }

    public void setPropertyString(String section, String key, String value) {
        if (value == null) {
            throw new Error("value may not be null");
        }
        LinkedHashMap map = this.sections.get(this.caseSens(section));
        if (map == null) {
            map = this.sections.put(this.caseSens(section), new LinkedHashMap());
        }
        map.put(this.caseSens(key), value);
    }

    public void setPropertyInt(String section, String key, int value) {
        this.setPropertyString(section, key, String.valueOf(value));
    }

    public void setPropertyBool(String section, String key, boolean value) {
        this.setPropertyString(section, key, value ? "true" : "false");
    }

    public void toFile(File f) throws IOException {
        for (String s1 : this.subscribers.keySet()) {
            LinkedHashMap<String, IniSubscriber> l1 = this.subscribers.get(s1);
            for (String s2 : l1.keySet()) {
                Iterator<String> newVal = ((IniSubscriber)l1.get(s2)).inform();
                if (newVal instanceof String) {
                    this.setPropertyString(s1, s2, (String)((Object)newVal));
                    continue;
                }
                if (newVal instanceof Integer) {
                    this.setPropertyInt(s1, s2, (Integer)((Object)newVal));
                    continue;
                }
                if (newVal instanceof Boolean) {
                    this.setPropertyBool(s1, s2, (Boolean)((Object)newVal));
                    continue;
                }
                throw new Error("WTF ini!");
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(f));
        for (String category : this.sections.keySet()) {
            out.write("[" + category.toUpperCase() + "]\r\n");
            LinkedHashMap<String, String> curCat = this.sections.get(category);
            for (String entry : curCat.keySet()) {
                String content = curCat.get(entry);
                out.write(String.valueOf(entry.toLowerCase()) + "=" + content + "\r\n");
            }
            out.write("\r\n");
        }
        out.close();
    }

    public void update(IniSubscriber i) {
        String[] vals = this.subsc.get(i);
        Object newVal = i.inform();
        if (newVal instanceof String) {
            this.setPropertyString(vals[0], vals[1], (String)newVal);
        } else if (newVal instanceof Integer) {
            this.setPropertyInt(vals[0], vals[1], (Integer)newVal);
        } else if (newVal instanceof Boolean) {
            this.setPropertyBool(vals[0], vals[1], (Boolean)newVal);
        } else {
            throw new Error("WTF ini!");
        }
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>> getFileContent() {
        return this.sections;
    }
}

