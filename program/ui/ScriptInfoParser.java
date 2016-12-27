/*
 * Decompiled with CFR 0_119.
 */
package program.ui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ScriptInfoParser {
    private HashMap<String, InfoParseResult> cache = new HashMap();
    static SimpleAttributeSet capt = new SimpleAttributeSet();
    private static SimpleAttributeSet key = new SimpleAttributeSet();
    private static SimpleAttributeSet error = new SimpleAttributeSet();
    private static SimpleAttributeSet vers = new SimpleAttributeSet();
    private static Pattern keyword;

    static {
        StyleConstants.setBold(capt, true);
        StyleConstants.setUnderline(capt, true);
        StyleConstants.setBold(key, true);
        StyleConstants.setBold(error, true);
        StyleConstants.setForeground(error, Color.RED);
        StyleConstants.setFontSize(vers, 11);
        StyleConstants.setItalic(vers, true);
        StyleConstants.setBold(vers, true);
        StyleConstants.setAlignment(vers, 2);
        keyword = Pattern.compile("\\A\\@(\\w+)\\s+(.*)");
    }

    public void clearCache() {
        this.cache = new HashMap();
    }

    private void handleKeyword(String keyword, String value, ScriptInformation writeTo) {
        value = value.trim();
        if (keyword == null) {
            writeTo.infoText = value;
        } else if (keyword.equals("author")) {
            writeTo.author = value;
        } else if (keyword.equals("date")) {
            writeTo.date = value;
        } else if (keyword.equals("param")) {
            writeTo.params.add(value);
        } else if (keyword.equals("version")) {
            writeTo.version = value;
        } else if (keyword.equals("type")) {
            if (value.equalsIgnoreCase("Library")) {
                writeTo.isLibrary = true;
            }
            writeTo.type = value;
        }
    }

    private ScriptInformation doParse(File f) throws IOException {
        String line;
        BufferedReader b = new BufferedReader(new FileReader(f));
        boolean valid = false;
        while ((line = b.readLine()) != null) {
            if ((line = line.trim()).equals("")) continue;
            if (!line.startsWith("/*")) break;
            valid = true;
            break;
        }
        if (!valid) {
            return null;
        }
        LinkedList<String> lines = new LinkedList<String>();
        while ((line = b.readLine()) != null) {
            int end = (line = line.trim()).indexOf("*/");
            if (end != -1) {
                line = line.substring(0, end);
                lines.add(line);
                break;
            }
            lines.add(line);
        }
        b.close();
        ScriptInformation result = new ScriptInformation(null);
        String currentKeyword = null;
        StringBuilder currentValue = new StringBuilder();
        Iterator iterator = lines.iterator();
        while (iterator.hasNext()) {
            Matcher m;
            String lin = (String)iterator.next();
            if (lin.startsWith("*")) {
                lin = lin.substring(1).trim();
            }
            if ((m = keyword.matcher(lin)).matches()) {
                this.handleKeyword(currentKeyword, currentValue.toString(), result);
                currentKeyword = m.group(1);
                currentValue = new StringBuilder();
                currentValue.append(m.group(2));
                currentValue.append("\n");
                continue;
            }
            currentValue.append(lin);
            currentValue.append("\n");
        }
        this.handleKeyword(currentKeyword, currentValue.toString(), result);
        return result;
    }

    public InfoParseResult parseFile(File f) {
        try {
            if (this.cache.containsKey(f.getAbsolutePath())) {
                return this.cache.get(f.getAbsolutePath());
            }
            DefaultStyledDocument result = new DefaultStyledDocument();
            InfoParseResult ret = new InfoParseResult(result, false, false, f);
            result.insertString(0, "Script info for " + f.getName() + ":\n\n", capt);
            try {
                ScriptInformation info = this.doParse(f);
                if (info == null) {
                    result.insertString(result.getLength(), "- no information available for this script file -", null);
                    ret.allowExecution = true;
                    ret.allowViewSource = true;
                } else {
                    info.appendToDocument(result);
                    ret.allowViewSource = true;
                    ret.allowExecution = !info.isLibrary;
                }
                this.cache.put(f.getAbsolutePath(), ret);
                return ret;
            }
            catch (IOException e) {
                result.insertString(result.getLength(), "- the file could not be read! -", error);
                return ret;
            }
        }
        catch (BadLocationException e) {
            throw new Error(e);
        }
    }

    public static class InfoParseResult {
        Document result;
        boolean allowExecution = false;
        boolean allowViewSource = false;
        File file;

        public InfoParseResult(Document result, boolean exec, boolean view, File file) {
            this.result = result;
            this.allowExecution = exec;
            this.allowViewSource = view;
            this.file = file;
        }
    }

    private static class ScriptInformation {
        String infoText;
        String author;
        String version;
        String date;
        boolean isLibrary;
        String type;
        LinkedList<String> params;

        private ScriptInformation() {
            this.infoText = null;
            this.author = "unknown";
            this.version = null;
            this.date = null;
            this.isLibrary = false;
            this.type = null;
            this.params = new LinkedList();
        }

        public void appendToDocument(Document d) {
            try {
                d.insertString(d.getLength(), "Author: ", key);
                d.insertString(d.getLength(), this.author, null);
                if (this.type != null) {
                    d.insertString(d.getLength(), "\nType: ", key);
                    d.insertString(d.getLength(), this.type, null);
                }
                if (this.version != null) {
                    d.insertString(d.getLength(), "\nv " + this.version, vers);
                }
                d.insertString(d.getLength(), "\n\n" + this.infoText, null);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        /* synthetic */ ScriptInformation(ScriptInformation scriptInformation) {
            ScriptInformation scriptInformation2;
            scriptInformation2();
        }
    }

}

