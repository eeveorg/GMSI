/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;
import wcData.objects.AttrIdentifier;
import wcData.objects.BackwardMapping;
import wcData.objects.ObjectAttrEntry;
import wcData.objects.SLK_File;

public class MetaData {
    private HashMap<String, HashMap<String, HashMap<String, Object>>> metatable = new HashMap();
    private BackwardMapping backwardMapping = new BackwardMapping(this.metatable);
    private static MetaData metaDataCache = null;
    private HashMap<String, DataObject> defaultValues = new HashMap(32);
    private Pattern lastDigit = Pattern.compile("([0-9]+)\\z");

    public boolean hasDefaultValue(String category, AttrIdentifier attr) {
        return this.metatable.get(category).containsKey(attr.getName());
    }

    public ObjectAttrEntry getDefaultValue(String category, AttrIdentifier attr) {
        HashMap<String, Object> entry = this.metatable.get(category).get(attr.getName());
        if (entry == null) {
            return null;
        }
        DataObject o = this.defaultValues.get(entry.get("type"));
        if (o == null) {
            o = new StringObject("");
        }
        return new ObjectAttrEntry(attr, o);
    }

    protected static MetaData getMetaData() {
        if (metaDataCache == null) {
            try {
                metaDataCache = new MetaData(new File("./wc3data/slk"));
            }
            catch (IOException e) {
                throw new Error(e);
            }
        }
        return metaDataCache;
    }

    private MetaData(File folder) throws IOException {
        IntObject Int = new IntObject(0);
        FloatObject Flo = new FloatObject(0.0f);
        this.defaultValues.put("spellDetail", Int);
        this.defaultValues.put("morphFlags", Int);
        this.defaultValues.put("silenceFlags", Int);
        this.defaultValues.put("unreal", Flo);
        this.defaultValues.put("bool", Int);
        this.defaultValues.put("stackFlags", Int);
        this.defaultValues.put("pickFlags", Int);
        this.defaultValues.put("defenseTypeInt", Int);
        this.defaultValues.put("attackBits", Int);
        this.defaultValues.put("interactionFlags", Int);
        this.defaultValues.put("detectionType", Int);
        this.defaultValues.put("versionFlags", Int);
        this.defaultValues.put("channelFlags", Int);
        this.defaultValues.put("channelType", Int);
        this.defaultValues.put("real", Flo);
        this.defaultValues.put("deathType", Int);
        this.defaultValues.put("int", Int);
        this.defaultValues.put("fullFlags", Int);
        this.defaultValues.put("teamColor", Int);
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\UnitMetaData.slk"), "unit");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\UnitMetaData.slk"), "item");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\DestructableMetaData.slk"), "destr");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\DoodadMetaData.slk"), "dood");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\AbilityMetaData.slk"), "abil");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\AbilityBuffMetaData.slk"), "buff");
        this.addMetaSlkFile(new SLK_File(String.valueOf(folder.getPath()) + "\\UpgradeMetaData.slk"), "upgrade");
        this.backwardMapping.createBackwardMapping();
    }

    protected Object getEntry(String type, String attrID, String field) {
        if (!this.metatable.containsKey(type)) {
            return null;
        }
        HashMap<String, HashMap<String, Object>> typeRow = this.metatable.get(type);
        if (!typeRow.containsKey(attrID)) {
            return null;
        }
        return typeRow.get(attrID).get(field);
    }

    private void addMetaSlkFile(SLK_File f, String type) {
        boolean onlyItems = false;
        boolean onlyUnits = false;
        if (type.equals("unit")) {
            onlyUnits = true;
        } else if (type.equals("item")) {
            onlyItems = true;
        }
        for (String s : f.getTable().keySet()) {
            HashMap<String, HashMap<String, Object>> typerow;
            if (onlyUnits && ((Integer)f.getField(s, "useHero")).equals(0) && ((Integer)f.getField(s, "useUnit")).equals(0) && ((Integer)f.getField(s, "useBuilding")).equals(0) || onlyItems && ((Integer)f.getField(s, "useItem")).equals(0)) continue;
            if (!this.metatable.containsKey(type)) {
                this.metatable.put(type, new HashMap());
            }
            if (!(typerow = this.metatable.get(type)).containsKey(s)) {
                typerow.put(s, new HashMap());
            }
            HashMap<String, Object> row = typerow.get(s);
            for (String s2 : f.getRow(s).keySet()) {
                row.put(s2, f.getField(s, s2));
            }
        }
    }

    public String backwardLookup(String category, String object, String attrName, int index) {
        try {
            return this.backwardMapping.getFirstMapping(category, attrName, index, object, 0);
        }
        catch (BackwardMapping.NoSuchMappingException e) {
            return null;
        }
    }

    protected AttrIdentifier tryLookup(String attrName, int index, String type, String object, int datapointer) throws UnknownAttributeException {
        int level;
        String key;
        block5 : {
            key = null;
            level = -1;
            try {
                key = this.backwardMapping.getFirstMapping(type, attrName, index, object, datapointer);
            }
            catch (BackwardMapping.NoSuchMappingException e) {
                Matcher m = this.lastDigit.matcher(attrName);
                if (!m.find()) break block5;
                attrName = attrName.substring(0, attrName.length() - m.group(1).length());
                try {
                    key = this.backwardMapping.getFirstMapping(type, attrName, index, object, datapointer);
                }
                catch (BackwardMapping.NoSuchMappingException e1) {
                    key = null;
                }
                if (key == null) break block5;
                level = Integer.parseInt(m.group());
            }
        }
        if (key == null) {
            return null;
        }
        return new AttrIdentifier(key, level, datapointer);
    }

    public static class UnknownAttributeException
    extends Exception {
        private static final long serialVersionUID = 1;

        public UnknownAttributeException(String s) {
            super(s);
        }
    }

}

