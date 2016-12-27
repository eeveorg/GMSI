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

    public boolean hasDefaultValue(String string, AttrIdentifier attrIdentifier) {
        return this.metatable.get(string).containsKey(attrIdentifier.getName());
    }

    public ObjectAttrEntry getDefaultValue(String string, AttrIdentifier attrIdentifier) {
        HashMap<String, Object> hashMap = this.metatable.get(string).get(attrIdentifier.getName());
        if (hashMap == null) {
            return null;
        }
        DataObject dataObject = this.defaultValues.get(hashMap.get("type"));
        if (dataObject == null) {
            dataObject = new StringObject("");
        }
        return new ObjectAttrEntry(attrIdentifier, dataObject);
    }

    protected static MetaData getMetaData() {
        if (metaDataCache == null) {
            try {
                metaDataCache = new MetaData(new File("./wc3data/slk"));
            }
            catch (IOException iOException) {
                throw new Error(iOException);
            }
        }
        return metaDataCache;
    }

    private MetaData(File file) throws IOException {
        IntObject intObject = new IntObject(0);
        FloatObject floatObject = new FloatObject(0.0f);
        this.defaultValues.put("spellDetail", intObject);
        this.defaultValues.put("morphFlags", intObject);
        this.defaultValues.put("silenceFlags", intObject);
        this.defaultValues.put("unreal", floatObject);
        this.defaultValues.put("bool", intObject);
        this.defaultValues.put("stackFlags", intObject);
        this.defaultValues.put("pickFlags", intObject);
        this.defaultValues.put("defenseTypeInt", intObject);
        this.defaultValues.put("attackBits", intObject);
        this.defaultValues.put("interactionFlags", intObject);
        this.defaultValues.put("detectionType", intObject);
        this.defaultValues.put("versionFlags", intObject);
        this.defaultValues.put("channelFlags", intObject);
        this.defaultValues.put("channelType", intObject);
        this.defaultValues.put("real", floatObject);
        this.defaultValues.put("deathType", intObject);
        this.defaultValues.put("int", intObject);
        this.defaultValues.put("fullFlags", intObject);
        this.defaultValues.put("teamColor", intObject);
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\UnitMetaData.slk"), "unit");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\UnitMetaData.slk"), "item");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\DestructableMetaData.slk"), "destr");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\DoodadMetaData.slk"), "dood");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\AbilityMetaData.slk"), "abil");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\AbilityBuffMetaData.slk"), "buff");
        this.addMetaSlkFile(new SLK_File(String.valueOf(file.getPath()) + "\\UpgradeMetaData.slk"), "upgrade");
        this.backwardMapping.createBackwardMapping();
    }

    protected Object getEntry(String string, String string2, String string3) {
        if (!this.metatable.containsKey(string)) {
            return null;
        }
        HashMap<String, HashMap<String, Object>> hashMap = this.metatable.get(string);
        if (!hashMap.containsKey(string2)) {
            return null;
        }
        return hashMap.get(string2).get(string3);
    }

    private void addMetaSlkFile(SLK_File sLK_File, String string) {
        boolean bl = false;
        boolean bl2 = false;
        if (string.equals("unit")) {
            bl2 = true;
        } else if (string.equals("item")) {
            bl = true;
        }
        for (String string2 : sLK_File.getTable().keySet()) {
            HashMap<String, HashMap<String, Object>> hashMap;
            if (bl2 && ((Integer)sLK_File.getField(string2, "useHero")).equals(0) && ((Integer)sLK_File.getField(string2, "useUnit")).equals(0) && ((Integer)sLK_File.getField(string2, "useBuilding")).equals(0) || bl && ((Integer)sLK_File.getField(string2, "useItem")).equals(0)) continue;
            if (!this.metatable.containsKey(string)) {
                this.metatable.put(string, new HashMap());
            }
            if (!(hashMap = this.metatable.get(string)).containsKey(string2)) {
                hashMap.put(string2, new HashMap());
            }
            HashMap<String, Object> hashMap2 = hashMap.get(string2);
            for (String string3 : sLK_File.getRow(string2).keySet()) {
                hashMap2.put(string3, sLK_File.getField(string2, string3));
            }
        }
    }

    public String backwardLookup(String string, String string2, String string3, int n) {
        try {
            return this.backwardMapping.getFirstMapping(string, string3, n, string2, 0);
        }
        catch (BackwardMapping.NoSuchMappingException noSuchMappingException) {
            return null;
        }
    }

    protected AttrIdentifier tryLookup(String string, int n, String string2, String string3, int n2) throws UnknownAttributeException {
        String string4;
        int n3;
        block5 : {
            string4 = null;
            n3 = -1;
            try {
                string4 = this.backwardMapping.getFirstMapping(string2, string, n, string3, n2);
            }
            catch (BackwardMapping.NoSuchMappingException noSuchMappingException) {
                Matcher matcher = this.lastDigit.matcher(string);
                if (!matcher.find()) break block5;
                string = string.substring(0, string.length() - matcher.group(1).length());
                try {
                    string4 = this.backwardMapping.getFirstMapping(string2, string, n, string3, n2);
                }
                catch (BackwardMapping.NoSuchMappingException noSuchMappingException2) {
                    string4 = null;
                }
                if (string4 == null) break block5;
                n3 = Integer.parseInt(matcher.group());
            }
        }
        if (string4 == null) {
            return null;
        }
        return new AttrIdentifier(string4, n3, n2);
    }

    public static class UnknownAttributeException
    extends Exception {
        private static final long serialVersionUID = 1;

        public UnknownAttributeException(String string) {
            super(string);
        }
    }

}

