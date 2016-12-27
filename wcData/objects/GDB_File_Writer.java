/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import program.misc.Log;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;
import wcData.BlizzardDataOutputStream;
import wcData.objects.WC3DefaultValues;

public class GDB_File_Writer {
    private WC3DefaultValues input;

    public GDB_File_Writer(WC3DefaultValues s) {
        this.input = s;
    }

    public void toFile(File f, String category) throws IOException {
        if (Log.doLog(1, 3)) {
            Log.println("Generating GDB File " + f.getName());
        }
        BlizzardDataOutputStream b = new BlizzardDataOutputStream(f);
        HashMap<String, WC3DefaultValues.TableEntry> table = this.input.getTable(category);
        HashSet s = new HashSet();
        LinkedList<String> objects = new LinkedList<String>();
        HashMap<String, Integer> attrNums = new HashMap<String, Integer>();
        for (Map.Entry<String, WC3DefaultValues.TableEntry> object : table.entrySet()) {
            objects.add(object.getKey());
            s.addAll(object.getValue().keySet());
        }
        b.writeInt(s.size());
        int i = 0;
        ArrayList<String> attrs = new ArrayList<String>(s.size());
        for (String ss : s) {
            if (ss == null) continue;
            attrNums.put(ss, i);
            ++i;
            attrs.add(ss);
            b.writeString(ss);
        }
        b.writeInt(objects.size());
        for (String obj : objects) {
            WC3DefaultValues.TableEntry e = table.get(obj);
            b.writeString(obj);
            b.writeInt(e.size());
            int j = 0;
            while (j < attrs.size()) {
                if (e.get(attrs.get(j)) != null) {
                    b.writeInt(j);
                    DataObject cur = (DataObject)e.get(attrs.get(j));
                    b.writeString(cur.getAdditionalData().toString());
                    if (cur instanceof StringObject) {
                        b.writeChars(new char[]{'s'});
                        b.writeString(((StringObject)cur).getValue());
                    } else if (cur instanceof IntObject) {
                        b.writeChars(new char[]{'i'});
                        b.writeInt(((IntObject)cur).getValue());
                    } else if (cur instanceof FloatObject) {
                        b.writeChars(new char[]{'f'});
                        b.writeFloat(((FloatObject)cur).getValue());
                    } else {
                        throw new Error("Invalid Type!" + obj + ":" + (String)attrs.get(j));
                    }
                }
                ++j;
            }
        }
        b.close();
    }

    private void writeAliases(File f) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(f));
        for (Map.Entry<String, String> e : this.input.getAliasTable().entrySet()) {
            if (e.getKey().equals(e.getValue())) continue;
            w.write(String.valueOf(e.getKey()) + ":" + e.getValue() + "\n");
        }
        w.close();
    }

    public void updateGDBs(File path) throws IOException {
        path.mkdirs();
        this.writeAliases(new File(String.valueOf(path.getPath()) + "/aliases.ali"));
        this.toFile(new File(String.valueOf(path.getPath()) + "/units.gdb"), "unit");
        this.toFile(new File(String.valueOf(path.getPath()) + "/abils.gdb"), "abil");
        this.toFile(new File(String.valueOf(path.getPath()) + "/items.gdb"), "item");
        this.toFile(new File(String.valueOf(path.getPath()) + "/doods.gdb"), "dood");
        this.toFile(new File(String.valueOf(path.getPath()) + "/buffs.gdb"), "buff");
        this.toFile(new File(String.valueOf(path.getPath()) + "/destrs.gdb"), "destr");
        this.toFile(new File(String.valueOf(path.getPath()) + "/upgrs.gdb"), "upgrade");
        this.toFile(new File(String.valueOf(path.getPath()) + "/aliases.gdb"), "alias");
    }
}

