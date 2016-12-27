/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import program.misc.Log;

class BackwardMapping {
    private HashMap<String, HashMap<String, BackwardMappingEntry>> mapping = new HashMap();
    private HashMap<String, HashMap<String, HashMap<String, Object>>> metadata;

    protected BackwardMapping(HashMap<String, HashMap<String, HashMap<String, Object>>> metadata) {
        this.metadata = metadata;
    }

    protected void createBackwardMapping() {
        for (String category : this.metadata.keySet()) {
            HashMap<String, HashMap<String, Object>> row = this.metadata.get(category);
            for (String newValue : row.keySet()) {
                String newKey = String.valueOf(row.get(newValue).get("field"));
                int index = (Integer)row.get(newValue).get("index");
                if (newValue.equals("uico") && category.equals("item") || newValue.equals("iico") && category.equals("unit")) continue;
                if (category.equals("abil")) {
                    String objects = "";
                    int dataOffset = (Integer)row.get(newValue).get("data");
                    objects = (String)row.get(newValue).get("useSpecific");
                    if (objects == null) {
                        objects = "";
                    }
                    this.addAbilMapping(newKey.toLowerCase(), newValue, index, objects, dataOffset);
                    continue;
                }
                this.addMapping(category, newKey.toLowerCase(), newValue, index);
            }
        }
    }

    protected String getMapping(String type, String key, int index, String origObjectId, int dataOffset) throws DoubleMappingException, NoSuchMappingException {
        return this.getEntry(key.toLowerCase(), type).getMapping(index, origObjectId, dataOffset);
    }

    protected String getFirstMapping(String type, String key, int index, String origObjectId, int dataOffset) throws NoSuchMappingException {
        return this.getEntry(key.toLowerCase(), type).getFirstMapping(index, origObjectId, dataOffset);
    }

    private void addAbilMapping(String key, String value, int index, String allowedObjects, int dataOffset) {
        HashMap<String, BackwardMappingEntry> row;
        AbilBackwardMappingEntry entry;
        if (!this.mapping.containsKey("abil")) {
            this.mapping.put("abil", new HashMap());
        }
        if (!(row = this.mapping.get("abil")).containsKey(key)) {
            entry = new AbilBackwardMappingEntry(key);
            row.put(key, entry);
        } else {
            entry = (AbilBackwardMappingEntry)row.get(key);
        }
        entry.add(value, index, allowedObjects, dataOffset);
    }

    private void addMapping(String type, String key, String value, int index) {
        HashMap<String, BackwardMappingEntry> row;
        if (!this.mapping.containsKey(type)) {
            this.mapping.put(type, new HashMap());
        }
        if (!(row = this.mapping.get(type)).containsKey(key)) {
            row.put(key, new BackwardMappingEntry(key));
        }
        if (row.get(key).add(value, index) && Log.doLog(2, 5)) {
            Log.println("Doublemapping: " + type + "->" + key + "->" + row.get(key).getMappingListString(index, null, 0) + " " + value);
        }
    }

    private BackwardMappingEntry getEntry(String key, String type) throws NoSuchMappingException {
        if (!this.mapping.containsKey(type)) {
            throw new NoSuchMappingException("Backward Mapping error: No such type:\"" + type + "\"");
        }
        HashMap<String, BackwardMappingEntry> row = this.mapping.get(type);
        if (!row.containsKey(key)) {
            throw new NoSuchMappingException("Backward Mapping error: No such mapping exists \"" + type + "->" + key + "\"");
        }
        return row.get(key);
    }

    private class AbilBackwardMappingEntry
    extends BackwardMappingEntry {
        private HashMap<String, HashMap<Integer, LinkedList<String>>> specialEntries;

        public AbilBackwardMappingEntry(String name) {
            super(name);
            this.specialEntries = new HashMap();
        }

        @Override
        public String toString() {
            String result = "Mapping for " + this.name + "\n";
            for (Integer i : this.entries.keySet()) {
                result = String.valueOf(result) + "  [" + i + "]: " + this.getMappingListString(i, null, 0) + "\n";
            }
            for (String s : this.specialEntries.keySet()) {
                HashMap<Integer, LinkedList<String>> row = this.specialEntries.get(s);
                for (Integer i : row.keySet()) {
                    result = String.valueOf(result) + "  " + s + "[" + i + "]: " + this.getMappingListString(0, s, i) + "\n";
                }
            }
            return result;
        }

        public boolean add(String value, int index, String allowedObjects, int dataOffset) {
            boolean collision = false;
            if (!allowedObjects.equals("")) {
                String[] objectList;
                String[] arrstring = objectList = allowedObjects.split(",");
                int n = arrstring.length;
                int n2 = 0;
                while (n2 < n) {
                    HashMap<Integer, LinkedList<String>> entry;
                    String object = arrstring[n2];
                    if (!this.specialEntries.containsKey(object)) {
                        this.specialEntries.put(object, new HashMap());
                    }
                    if (!(entry = this.specialEntries.get(object)).containsKey(dataOffset)) {
                        entry.put(dataOffset, new LinkedList());
                    }
                    LinkedList<String> list = entry.get(dataOffset);
                    list.add(value);
                    if (list.size() > 1) {
                        collision = true;
                        if (Log.doLog(2, 5)) {
                            Log.println("Doublemapping: abil->" + value + " " + object);
                        }
                        for (String s : list) {
                            if (!Log.doLog(2, 5)) continue;
                            Log.println("--->" + s);
                        }
                    }
                    ++n2;
                }
            } else {
                return this.add(value, index);
            }
            return collision;
        }

        @Override
        public String getFirstMapping(int index, String origObjectId, int dataOffset) throws NoSuchMappingException {
            if (origObjectId == null || origObjectId.equals("") || !this.specialEntries.containsKey(origObjectId)) {
                return super.getFirstMapping(index, origObjectId, dataOffset);
            }
            HashMap<Integer, LinkedList<String>> row = this.specialEntries.get(origObjectId);
            if (!row.containsKey(dataOffset)) {
                throw new NoSuchMappingException("Cannot get mapping for origObject " + origObjectId + " with offset " + dataOffset + " because the offset is not present");
            }
            return row.get(dataOffset).getFirst();
        }

        @Override
        public String getMapping(int index, String origObjectId, int dataOffset) throws DoubleMappingException, NoSuchMappingException {
            if (origObjectId == null || origObjectId.equals("") || !this.specialEntries.containsKey(origObjectId)) {
                return super.getMapping(index, origObjectId, dataOffset);
            }
            HashMap<Integer, LinkedList<String>> row = this.specialEntries.get(origObjectId);
            if (!row.containsKey(dataOffset)) {
                throw new NoSuchMappingException("Cannot get mapping for origObject " + origObjectId + " with offset " + dataOffset + " because the offset is not present");
            }
            LinkedList<String> list = row.get(dataOffset);
            if (list.size() > 1) {
                throw new DoubleMappingException("Cannot get mapping, because more mappings exist: " + this.getMappingListString(index, origObjectId, dataOffset));
            }
            return list.getFirst();
        }

        @Override
        public String getMappingListString(int index, String origObjectId, int dataOffset) {
            String result = "";
            if (origObjectId == null || origObjectId.equals("") || !this.specialEntries.containsKey(origObjectId)) {
                return super.getMappingListString(index, origObjectId, dataOffset);
            }
            HashMap<Integer, LinkedList<String>> row = this.specialEntries.get(origObjectId);
            if (!row.containsKey(dataOffset)) {
                return "";
            }
            for (String s : row.get(dataOffset)) {
                result = String.valueOf(result) + "," + s;
            }
            return result.substring(1);
        }
    }

    private class BackwardMappingEntry {
        protected HashMap<Integer, LinkedList<String>> entries;
        protected String name;

        public BackwardMappingEntry(String name) {
            this.entries = new HashMap();
            this.name = name;
        }

        public String toString() {
            String result = "Mapping for " + this.name + "\n";
            for (Integer i : this.entries.keySet()) {
                result = String.valueOf(result) + "  [" + i + "]: " + this.getMappingListString(i, null, 0) + "\n";
            }
            return result;
        }

        public boolean add(String value, int index) {
            if (index == -1) {
                index = 0;
            }
            if (!this.entries.containsKey(index)) {
                this.entries.put(index, new LinkedList());
            }
            LinkedList<String> list = this.entries.get(index);
            list.add(value);
            if (list.size() > 1) {
                return true;
            }
            return false;
        }

        public String getFirstMapping(int index, String origObjectId, int dataOffset) throws NoSuchMappingException {
            if (!this.entries.containsKey(index)) {
                throw new NoSuchMappingException("Cannot get mapping, because it doesn't exist for index " + index);
            }
            return this.entries.get(index).getFirst();
        }

        public String getMapping(int index, String origObjectId, int dataOffset) throws DoubleMappingException, NoSuchMappingException {
            if (!this.entries.containsKey(index)) {
                throw new NoSuchMappingException("Cannot get mapping, because no mapping for index " + index + " exists");
            }
            if (this.entries.get(index).size() > 1) {
                throw new DoubleMappingException("Cannot get mapping, because more mappings exist: " + this.getMappingListString(index, null, 0));
            }
            return this.entries.get(index).getFirst();
        }

        public String getMappingListString(int index, String origObjectId, int dataOffset) {
            String result = "";
            for (String s : this.entries.get(index)) {
                result = String.valueOf(result) + "," + s;
            }
            return result.substring(1);
        }
    }

    public class DoubleMappingException
    extends Exception {
        private static final long serialVersionUID = 1;

        public DoubleMappingException(String s) {
            super(s);
        }
    }

    public class NoSuchMappingException
    extends Exception {
        private static final long serialVersionUID = 1;

        public NoSuchMappingException(String s) {
            super(s);
        }
    }

}

