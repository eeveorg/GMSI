/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import program.Program;
import program.misc.IniHandler;
import program.misc.Log;
import program.misc.Misc;
import program.misc.Tools;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;
import script.names.NameResolver;
import script.names.StandardVarSpace;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.objects.AttrIdentifier;
import wcData.objects.GDB_File_Writer;
import wcData.objects.MetaData;
import wcData.objects.ObjectAttrEntry;
import wcData.objects.ObjectTableEntry;
import wcData.objects.SLK_File;
import wcData.objects.WC3Object;

public class WC3DefaultValues {
    private MetaData metaData = MetaData.getMetaData();
    private static WC3DefaultValues cache = null;
    private boolean inited = false;
    private HashMap<String, TableEntry> values = new HashMap(4500);
    private HashMap<String, String> aliases = new HashMap();
    private HashMap<String, TableEntry> aliasValues = new HashMap();
    private static HashMap<String, String> txtErrors = null;
    private static Pattern quoted = Pattern.compile("\"(.+?)\"", 32);

    protected HashMap<String, String> getAliasTable() {
        return this.aliases;
    }

    public static void refreshGDBsFromSLKs() throws IOException {
        if (Log.doLog(0, 3)) {
            Log.println("-- Rebuilding GDB Files from updated data --");
        }
        if (Log.doLog(0, 3)) {
            Log.println("Gathering data...");
        }
        WC3DefaultValues v = new WC3DefaultValues();
        v.loadFromSLKandTXT(new File("./wc3data/slk"), new File("./wc3data/txt"));
        if (Log.doLog(0, 3)) {
            Log.println("Finished gathering data, rebuilding GDB Files...");
        }
        GDB_File_Writer w = new GDB_File_Writer(v);
        w.updateGDBs(new File("./wc3data/gdb"));
        cache = null;
    }

    public static void main(String[] args) throws IOException {
        WC3DefaultValues.refreshGDBsFromSLKs();
    }

    public static WC3DefaultValues getDefaultValues() {
        if (cache == null) {
            cache = new WC3DefaultValues();
            try {
                cache.loadFromGDB(new File("./wc3data/gdb"));
            }
            catch (IOException e) {
                throw new Error(e);
            }
        }
        return cache;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public ObjectAttrEntry getDefaultValue(WC3Object obj, String attrName) {
        if (!this.values.containsKey(obj.getOrigId())) {
            return null;
        }
        e = this.values.get(obj.getOrigId());
        if (e.containsKey(attrName)) ** GOTO lbl25
        if (!obj.getCategory().equals("abil")) ** GOTO lbl21
        alias = this.aliases.get(obj.getOrigId());
        if (alias != null) {
            e = this.values.get(alias);
            if (e == null) {
                e = this.aliasValues.get(alias);
            }
            if (e == null || !e.containsKey(attrName)) {
                try {
                    return this.metaData.getDefaultValue(obj.getCategory(), this.gameNameToIdentifier(attrName, obj));
                }
                catch (MetaData.UnknownAttributeException e1) {
                    return null;
                }
            }
        } else {
            try {
                return this.metaData.getDefaultValue(obj.getCategory(), this.gameNameToIdentifier(attrName, obj));
            }
            catch (MetaData.UnknownAttributeException e1) {
                return null;
            }
lbl21: // 1 sources:
            try {
                return this.metaData.getDefaultValue(obj.getCategory(), this.gameNameToIdentifier(attrName, obj));
            }
            catch (MetaData.UnknownAttributeException e1) {
                return null;
            }
        }
lbl25: // 2 sources:
        o = (DataObject)e.get(attrName);
        return new ObjectAttrEntry((AttrIdentifier)o.getAdditionalData(), o);
    }

    public boolean hasDefaultValue(WC3Object obj, String attrName) {
        if (!this.values.containsKey(obj.getOrigId())) {
            return false;
        }
        TableEntry e = this.values.get(obj.getOrigId());
        if (!e.containsKey(attrName)) {
            try {
                return this.metaData.hasDefaultValue(obj.getCategory(), this.gameNameToIdentifier(attrName, obj));
            }
            catch (MetaData.UnknownAttributeException e1) {
                return false;
            }
        }
        return true;
    }

    public boolean hasObject(String id) {
        return this.values.containsKey(id);
    }

    public ArrayObject getObject(String id) {
        ArrayObject o;
        DataType type;
        if (!this.values.containsKey(id)) {
            return null;
        }
        TableEntry e = this.values.get(id);
        ObjectTableEntry entry = new ObjectTableEntry(e.getCategory(), e.getOrigId(), 16);
        try {
            type = new DataType(Program.getScript().getNameResolver().getUserDef(Misc.getTypeFromCategory(e.getCategory())));
        }
        catch (InternalScriptError e2) {
            throw new Error(e2);
        }
        try {
            o = (ArrayObject)type.getNewInstance();
        }
        catch (EvaluationError e1) {
            throw new Error("wc3defValues Error:", e1);
        }
        catch (InternalScriptError e1) {
            throw new Error("wc3defValues Error:", e1);
        }
        o.setStringMap(entry);
        return o;
    }

    public StandardVarSpace getAllObjects() throws InternalScriptError {
        StandardVarSpace result = new StandardVarSpace();
        for (String s : this.values.keySet()) {
            result.put(s, this.getObject(s));
        }
        return result;
    }

    public AttrIdentifier gameNameToIdentifier(String attrName, WC3Object obj) throws MetaData.UnknownAttributeException {
        return this.gameNameToIdentifier(attrName, 0, obj);
    }

    public AttrIdentifier gameNameToIdentifier(String attrName, int attrIndex, WC3Object obj) throws MetaData.UnknownAttributeException {
        int c;
        int level = 0;
        int datapointer = 0;
        String objAlias = null;
        String realAttrName = attrName;
        String objId = obj.getOrigId();
        if (attrName.startsWith("Data") && attrName.length() >= 5 && (c = attrName.charAt(4) - 64) >= 1 && c <= 8) {
            level = Integer.parseInt(attrName.substring(5));
            attrName = "Data";
            datapointer = c;
            AttrIdentifier key = this.metaData.tryLookup(attrName, attrIndex, obj.getCategory(), objId, datapointer);
            if (key == null) {
                objAlias = this.aliases.get(objId);
                if (objAlias != null) {
                    key = this.metaData.tryLookup(attrName, attrIndex, obj.getCategory(), objAlias, datapointer);
                }
                if (key == null) {
                    throw new MetaData.UnknownAttributeException("Unknown attribute: \"" + realAttrName + "\" for object " + obj.getId() + " of type " + obj.getCategory());
                }
            }
            key.setLevel(level);
            return key;
        }
        AttrIdentifier key = this.metaData.tryLookup(attrName, attrIndex, obj.getCategory(), objId, datapointer);
        if (key == null && objAlias != null) {
            key = this.metaData.tryLookup(attrName, attrIndex, obj.getCategory(), objAlias, datapointer);
        }
        if (key == null) {
            throw new MetaData.UnknownAttributeException("Unknown attribute: \"" + realAttrName + "\" for object " + obj.getId() + " of type " + obj.getCategory());
        }
        if (key.getLevel() != -1) {
            level = key.getLevel();
        }
        if (obj.getCategory() == "abil" && (Integer)this.metaData.getEntry(obj.getCategory(), key.getName(), "repeat") == 3 && level == 0) {
            level = 1;
        }
        if (datapointer != 0 || level != 0) {
            return new AttrIdentifier(key.getName(), level, datapointer);
        }
        return new AttrIdentifier(key.getName());
    }

    public String identifierToGameName(AttrIdentifier attr, WC3Object obj) {
        String result = (String)this.metaData.getEntry(obj.getCategory(), attr.getName(), "field");
        int index = (Integer)this.metaData.getEntry(obj.getCategory(), attr.getName(), "index");
        if (index == 1) {
            result = String.valueOf(result) + "1";
        }
        if (attr.isDatafield()) {
            result = String.valueOf(result) + String.valueOf((char)(64 + attr.getDatapointer()));
            result = String.valueOf(result) + attr.getLevel();
        } else if (attr.getLevel() > 0) {
            Integer repeat = (Integer)this.metaData.getEntry(obj.getCategory(), attr.getName(), "repeat");
            String levelSuffix = repeat != null && (repeat == 3 || repeat == 1) ? (attr.getLevel() > 1 ? "" + (attr.getLevel() - 1) : "") : "" + attr.getLevel();
            result = String.valueOf(result) + levelSuffix;
        }
        return result;
    }

    protected HashMap<String, TableEntry> getTable(String category) {
        if (category.equals("alias")) {
            return this.aliasValues;
        }
        HashMap<String, TableEntry> result = new HashMap<String, TableEntry>();
        for (Map.Entry<String, TableEntry> e : this.values.entrySet()) {
            if (!e.getValue().getCategory().equals(category)) continue;
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }

    private void loadFromGDB(File gdbFolder) throws IOException {
        String s;
        if (this.inited) {
            throw new Error("Default values already inited!");
        }
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/units.gdb"), "unit");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/abils.gdb"), "abil");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/destrs.gdb"), "destr");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/doods.gdb"), "dood");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/items.gdb"), "item");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/upgrs.gdb"), "upgrade");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/buffs.gdb"), "buff");
        this.loadGDBFile(new File(String.valueOf(gdbFolder.getPath()) + "/aliases.gdb"), "alias");
        BufferedReader r = new BufferedReader(new FileReader(new File(String.valueOf(gdbFolder.getPath()) + "/aliases.ali")));
        while ((s = r.readLine()) != null) {
            this.aliases.put(s.substring(0, 4), s.substring(5));
        }
        r.close();
    }

    private void loadGDBFile(File f, String cat) throws IOException {
        HashMap<String, TableEntry> targetTable;
        if (Log.doLog(1, 4)) {
            Log.println("Openening GDB File " + f.getAbsolutePath());
        }
        BlizzardDataInputStream in = new BlizzardDataInputStream(new FileInputStream(f));
        if (cat.equals("alias")) {
            cat = "abil";
            targetTable = this.aliasValues;
        } else {
            targetTable = this.values;
        }
        int numAttrs = in.readInt();
        String[] attrs = new String[numAttrs];
        int i = 0;
        while (i < numAttrs) {
            attrs[i] = in.readString();
            ++i;
        }
        int numObjs = in.readInt();
        int i2 = 0;
        while (i2 < numObjs) {
            String objName = in.readString();
            int numCurAttrs = in.readInt();
            TableEntry curEntry = new TableEntry(cat, objName, numCurAttrs * 3 / 2);
            targetTable.put(objName, curEntry);
            int j = 0;
            while (j < numCurAttrs) {
                DataObject o;
                int attrId = in.readInt();
                String attrIdent = in.readString();
                char c = in.readChar();
                switch (c) {
                    case 's': {
                        o = new StringObject(in.readString());
                        break;
                    }
                    case 'i': {
                        o = new IntObject(in.readInt());
                        break;
                    }
                    case 'f': {
                        o = new FloatObject(in.readFloat());
                        break;
                    }
                    default: {
                        throw new Error("Error in GDB File!:" + c);
                    }
                }
                o.setAdditionalData(AttrIdentifier.stringToAttrIdent(attrIdent));
                curEntry.put(attrs[attrId], o);
                ++j;
            }
            ++i2;
        }
        in.close();
    }

    private void loadFromSLKandTXT(File slkFolder, File txtFolder) throws IOException {
        if (this.inited) {
            throw new Error("Default values already inited!");
        }
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/UnitBalance.slk"), "unit");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/UnitData.slk"), "unit");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/UnitAbilities.slk"), "unit");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/UnitWeapons.slk"), "unit");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/unitUI.slk"), "unit");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/AbilityData.slk"), "abil");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/DestructableData.slk"), "destr");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/Doodads.slk"), "dood");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/ItemData.slk"), "item");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/UpgradeData.slk"), "upgrade");
        this.addSlkFile(new SLK_File(String.valueOf(slkFolder.getPath()) + "/AbilityBuffData.slk"), "buff");
        this.loadTxts(txtFolder);
        this.inited = true;
    }

    private DataObject getDefaultInstance(AttrIdentifier ident, String type, String object) throws AttrIdentifier.InvalidAttrIdentException {
        String curType = (String)this.metaData.getEntry(type, ident.getName(), "type");
        if (curType == null) {
            throw new AttrIdentifier.InvalidAttrIdentException("Cannot get default instance from ident: " + ident);
        }
        if (curType.equals("int") || curType.equals("bool")) {
            return new IntObject(0);
        }
        if (curType.equals("real") || curType.equals("unreal")) {
            return new FloatObject(0.0f);
        }
        return new StringObject("");
    }

    private static HashSet<String> loadForbiddenColumns() {
        HashSet<String> forbiddenColumns = new HashSet<String>(50);
        forbiddenColumns.add("comment(s)");
        forbiddenColumns.add("valid");
        forbiddenColumns.add("version");
        forbiddenColumns.add("sort");
        forbiddenColumns.add("sortWeap");
        forbiddenColumns.add("sort2");
        forbiddenColumns.add("threat");
        forbiddenColumns.add("InBeta");
        forbiddenColumns.add("avgdmg1");
        forbiddenColumns.add("dmod1");
        forbiddenColumns.add("maxdmg1");
        forbiddenColumns.add("mindmg1");
        forbiddenColumns.add("avgdmg2");
        forbiddenColumns.add("dmod2");
        forbiddenColumns.add("maxdmg2");
        forbiddenColumns.add("mindmg2");
        forbiddenColumns.add("DmgUpg");
        forbiddenColumns.add("DPS");
        forbiddenColumns.add("hiddenInEditor");
        forbiddenColumns.add("sortUI");
        forbiddenColumns.add("unitClass");
        forbiddenColumns.add("useInEditor");
        forbiddenColumns.add("sortBalance");
        forbiddenColumns.add("weap1");
        forbiddenColumns.add("weap2");
        forbiddenColumns.add("comment");
        forbiddenColumns.add("comments");
        forbiddenColumns.add("code");
        forbiddenColumns.add("doodClass");
        forbiddenColumns.add("comments");
        forbiddenColumns.add("used");
        forbiddenColumns.add("scriptname");
        forbiddenColumns.add("sortAbil");
        forbiddenColumns.add("realHP");
        forbiddenColumns.add("realdef");
        forbiddenColumns.add("realM");
        forbiddenColumns.add("abilTest");
        forbiddenColumns.add("mincool1");
        forbiddenColumns.add("mincool2");
        forbiddenColumns.add("RngTst2");
        return forbiddenColumns;
    }

    private void addSlkFile(SLK_File f, String type) {
        HashSet<String> forbiddenColumns = WC3DefaultValues.loadForbiddenColumns();
        for (String object : f.getTable().keySet()) {
            if (object == null) continue;
            if (!this.values.containsKey(object)) {
                this.values.put(object, new TableEntry(type, object));
            }
            HashMap<String, Object> row = f.getRow(object);
            TableEntry e = this.values.get(object);
            if (f.getField(object, "code") != null) {
                this.aliases.put(object, f.getField(object, "code").toString());
            }
            for (String attrName : row.keySet()) {
                DataObject o;
                AttrIdentifier ident;
                if (attrName == null) continue;
                Object res = f.getField(object, attrName);
                if (forbiddenColumns.contains(attrName)) continue;
                try {
                    ident = this.gameNameToIdentifier(attrName, e);
                }
                catch (MetaData.UnknownAttributeException e1) {
                    continue;
                }
                try {
                    o = this.getDefaultInstance(ident, type, object);
                }
                catch (AttrIdentifier.InvalidAttrIdentException e1) {
                    Log.exception(e1);
                    continue;
                }
                if (o instanceof FloatObject && res instanceof Integer) {
                    res = Float.valueOf(((Integer)res).intValue());
                }
                DataObject d = DataObject.wrap(res);
                d.setAdditionalData(ident);
                e.put(attrName, d);
            }
        }
    }

    private void loadTxts(File folder) {
        if (txtErrors == null) {
            WC3DefaultValues.loadTxtErrors();
        }
        LinkedList<File> txtFiles = Tools.findFiles(folder, ".*\\.txt$");
        for (File f : txtFiles) {
            String category;
            if (Log.doLog(1, 4)) {
                Log.println("Loading txt file \"" + f.getName() + "\"");
            }
            if (f.getName().contains("Unit")) {
                category = "unit";
            } else if (f.getName().contains("Ability")) {
                category = "abil";
            } else if (f.getName().contains("Upgrade")) {
                category = "upgrade";
            } else if (f.getName().contains("Item")) {
                category = "item";
            } else {
                throw new Error("No category for " + f.getName());
            }
            this.loadTxt(f, category);
        }
    }

    private void loadTxt(File file, String category) {
        IniHandler r;
        try {
            r = new IniHandler(file.getPath(), true);
        }
        catch (FileNotFoundException e) {
            Log.exception(e);
            return;
        }
        catch (IOException e) {
            Log.exception(e);
            return;
        }
        LinkedHashMap<String, LinkedHashMap<String, String>> entries = r.getFileContent();
        Iterator<String> iterator = entries.keySet().iterator();
        while (iterator.hasNext()) {
            HashMap row;
            TableEntry curEntry;
            String obj = iterator.next();
            if (obj.equals("ANsp")) {
                curEntry = this.values.get("Ansp");
                row = entries.get("ANsp");
                obj = "Ansp";
            } else if (!this.values.containsKey(obj)) {
                if (!category.equals("abil")) continue;
                curEntry = new TableEntry("abil", obj, 8);
                this.aliasValues.put(obj, curEntry);
                row = entries.get(obj);
            } else {
                curEntry = this.values.get(obj);
                row = entries.get(obj);
            }
            for (String curAttr : row.keySet()) {
                String value = (String)row.get(curAttr);
                boolean isSpecial = false;
                if (((String)row.get(curAttr)).contains(",") && !curAttr.startsWith("Requires")) {
                    String s = this.metaData.backwardLookup(category, obj, curAttr, 1);
                    if (s == null) {
                        s = this.metaData.backwardLookup(category, obj, curAttr, 0);
                        Integer repeat = (Integer)this.metaData.getEntry(category, s, "repeat");
                        if (repeat != null && (repeat == 1 || repeat == 3)) {
                            isSpecial = true;
                        }
                    } else {
                        isSpecial = true;
                    }
                }
                if (isSpecial) {
                    String[] vals = this.getSpecialTxtValues((String)row.get(curAttr));
                    int i = 0;
                    while (i < vals.length) {
                        DataObject o = this.getTxtValue(category, curEntry, curAttr, vals[i], i);
                        curEntry.put(WC3DefaultValues.transformTxtAttrName(String.valueOf(curAttr) + (i >= 1 ? Integer.valueOf(i) : "")), o);
                        ++i;
                    }
                    continue;
                }
                DataObject o = this.getTxtValue(category, curEntry, curAttr, value, 0);
                if (o == null) continue;
                curEntry.put(WC3DefaultValues.transformTxtAttrName(curAttr), o);
            }
        }
    }

    private static void loadTxtErrors() {
        txtErrors = new HashMap();
        txtErrors.put("Specialart", "SpecialArt");
        txtErrors.put("Effectart", "EffectArt");
        txtErrors.put("Targetart", "TargetArt");
        txtErrors.put("Researchart", "ResearchArt");
        txtErrors.put("Casterart", "CasterArt");
        txtErrors.put("Effectart", "EffectArt");
    }

    private static String transformTxtAttrName(String curAttr) {
        if (txtErrors.containsKey(curAttr)) {
            return txtErrors.get(curAttr);
        }
        return curAttr;
    }

    private DataObject getTxtValue(String category, TableEntry object, String curAttr, String value, int index) {
        DataObject o;
        AttrIdentifier ident;
        try {
            ident = this.gameNameToIdentifier(curAttr, index, object);
        }
        catch (MetaData.UnknownAttributeException e) {
            try {
                ident = this.gameNameToIdentifier(curAttr, object);
            }
            catch (MetaData.UnknownAttributeException e1) {
                if (Log.doLog(2, 5)) {
                    Log.println("TXT-Warning: Unknown attribute \"" + curAttr + "\" for object " + object.getId() + " of type " + object.getCategory());
                }
                return null;
            }
        }
        ObjectAttrEntry atr = this.metaData.getDefaultValue(category, ident);
        if (atr == null) {
            String newCategory = object.getCategory();
            atr = this.metaData.getDefaultValue(newCategory, ident);
        }
        if (atr == null) {
            throw new Error("Unable to get " + ident);
        }
        if (value.startsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        DataObject o2 = atr.getData();
        try {
            o = new StringObject(value).explicitCastTo(o2.getType());
        }
        catch (InternalScriptError e) {
            throw new Error(e);
        }
        o.setAdditionalData(ident);
        return o;
    }

    private String[] getSpecialTxtValues(String in) {
        if (in.startsWith("\"")) {
            LinkedList<String> entries = new LinkedList<String>();
            Matcher m = quoted.matcher(in);
            while (m.find()) {
                entries.add(m.group(1));
            }
            String[] out = new String[entries.size()];
            int i = 0;
            for (String s : entries) {
                out[i++] = s;
            }
            return out;
        }
        String[] out = in.split(",");
        return out;
    }

    protected static class TableEntry
    extends HashMap<String, DataObject>
    implements WC3Object {
        private static final long serialVersionUID = 1;
        private String alias = null;
        private String id;
        private String cat;

        public TableEntry(String category, String id) {
            this.cat = category;
            this.id = id;
        }

        public TableEntry(String category, String id, int initialSize) {
            super(initialSize);
            this.cat = category;
            this.id = id;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        @Override
        public String getCategory() {
            return this.cat;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getOrigId() {
            return this.id;
        }
    }

}

