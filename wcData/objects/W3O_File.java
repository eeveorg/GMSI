/*
 * Decompiled with CFR 0_119.
 */
package wcData.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import program.Program;
import program.misc.Log;
import program.misc.Misc;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.StringObject;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;
import wcData.MapStringObject;
import wcData.objects.AttrIdentifier;
import wcData.objects.MetaData;
import wcData.objects.ObjectAttrEntry;
import wcData.objects.ObjectTableEntry;
import wcData.objects.WC3DefaultValues;
import wcData.objects.WC3Object;

public class W3O_File {
    private BlizzardDataInputStream bdis;
    private BlizzardDataOutputStream bdos;
    private int version = 2;
    private boolean useExtended;
    private String category;
    private WC3DefaultValues defaultValues;
    private MapHandle currentMap;
    private File file;
    private String name;
    private String prefix;
    private static HashMap<String, Integer> typeMapping = new HashMap();

    static {
        typeMapping.put("spellDetail", 0);
        typeMapping.put("morphFlags", 0);
        typeMapping.put("silenceFlags", 0);
        typeMapping.put("unreal", 2);
        typeMapping.put("bool", 0);
        typeMapping.put("stackFlags", 0);
        typeMapping.put("pickFlags", 0);
        typeMapping.put("defenseTypeInt", 0);
        typeMapping.put("attackBits", 0);
        typeMapping.put("interactionFlags", 0);
        typeMapping.put("detectionType", 0);
        typeMapping.put("versionFlags", 0);
        typeMapping.put("channelFlags", 0);
        typeMapping.put("channelType", 0);
        typeMapping.put("real", 1);
        typeMapping.put("deathType", 0);
        typeMapping.put("int", 0);
        typeMapping.put("fullFlags", 0);
        typeMapping.put("teamColor", 0);
    }

    public void toFile(File outPath, ArrayList<LinkedList<ObjectTableEntry>> input) throws IOException {
        if (input.get(0).size() + input.get(1).size() == 0) {
            return;
        }
        this.bdos = new BlizzardDataOutputStream(new File(String.valueOf(outPath.getAbsolutePath()) + "/" + this.name));
        this.bdos.writeInt(this.version);
        for (LinkedList<ObjectTableEntry> curList : input) {
            this.bdos.writeInt(curList.size());
            for (ObjectTableEntry e : curList) {
                String newId = e.getNewId();
                String origId = e.getOrigId();
                if (newId != null && !this.prefix.equals("")) {
                    newId = e.getNewId().substring(this.prefix.length());
                }
                this.bdos.writeFourByteString(origId);
                this.bdos.writeFourByteString(newId);
                this.bdos.writeInt(e.getKeys().size());
                for (Object o : e.getKeys()) {
                    DataObject val;
                    ObjectAttrEntry a = e.getRawEntry((String)o);
                    this.bdos.writeFourByteString(a.getIdent().getName());
                    String varTypeName = (String)MetaData.getMetaData().getEntry(e.getCategory(), a.getIdent().getName(), "type");
                    int varType = 3;
                    if (varTypeName == null) {
                        varType = -1;
                    } else if (typeMapping.containsKey(varTypeName)) {
                        varType = typeMapping.get(varTypeName);
                    }
                    if (varType == -1) {
                        throw new IOException("OMFG type == -1?!? " + a.getIdent().getName());
                    }
                    this.bdos.writeInt(varType);
                    if (this.useExtended) {
                        this.bdos.writeInt(a.getIdent().getLevel());
                        this.bdos.writeInt(a.getIdent().getDatapointer());
                    }
                    if ((val = a.getData()) instanceof StringObject) {
                        String strVal = ((StringObject)val).getValue();
                        if (strVal.length() > 1023) {
                            String objName;
                            try {
                                objName = e.get("Name").toString();
                            }
                            catch (InternalScriptError e1) {
                                objName = "unknown name";
                            }
                            catch (RuntimeException e1) {
                                objName = "unknown name";
                            }
                            this.bdos.close();
                            throw new IOException("Map object save error: The string used for object " + newId + "(" + objName + ")" + "'s attribute " + a.getIdent() + "(" + o + ") exceeds the maximum object editor string" + " length of 1023 chars (" + strVal.length() + " chars). Your editor will crash " + "if such a long string is used as an object editor value. Saving aborted!");
                        }
                        this.bdos.writeString(((StringObject)a.getData()).getValue());
                    } else if (a.getData() instanceof IntObject) {
                        this.bdos.writeInt(((IntObject)a.getData()).getValue());
                    } else if (a.getData() instanceof FloatObject) {
                        this.bdos.writeFloat(((FloatObject)a.getData()).getValue());
                    } else {
                        throw new IOException("OMFG type != str,int,float");
                    }
                    this.bdos.writeFourByteString(newId);
                }
            }
        }
        this.bdos.close();
    }

    public W3O_File(MapHandle m, String name, String cate, boolean extended, String prefi) throws IOException {
        this.name = name;
        this.prefix = "";
        this.currentMap = m;
        this.useExtended = extended;
        this.category = cate;
    }

    public void fromFile(File f) throws IOException {
        this.file = f;
        VarSpace v = this.currentMap.getObjectSpace();
        this.defaultValues = WC3DefaultValues.getDefaultValues();
        try {
            ObjectTableEntry curEntry;
            DataType type;
            ArrayObject o;
            this.bdis = new BlizzardDataInputStream(new FileInputStream(this.file));
            this.version = this.bdis.readInt();
            try {
                type = new DataType(Program.getScript().getNameResolver().getUserDef(Misc.getTypeFromCategory(this.category)));
            }
            catch (InternalScriptError e1) {
                throw new Error(e1);
            }
            int numObjects = this.bdis.readInt();
            int i = 0;
            while (i < numObjects) {
                block10 : {
                    curEntry = this.readObject(false);
                    try {
                        o = (ArrayObject)type.getNewInstance();
                        o.setStringMap(curEntry);
                        v.put(curEntry.getId(), o);
                    }
                    catch (InternalScriptError e) {
                        if (!Log.doLog(1, 2)) break block10;
                        Log.println("Map format warning: The object with ID " + curEntry.getId() + " in file " + f.getName() + " could not be loaded! It won't be in your map if you safe it");
                    }
                }
                ++i;
            }
            numObjects = this.bdis.readInt();
            i = 0;
            while (i < numObjects) {
                block11 : {
                    curEntry = this.readObject(true);
                    try {
                        o = (ArrayObject)type.getNewInstance();
                        o.setStringMap(curEntry);
                        v.put(curEntry.getId(), o);
                    }
                    catch (InternalScriptError e) {
                        if (!Log.doLog(1, 2)) break block11;
                        Log.println("Map format warning: The object with ID " + curEntry.getId() + " in file " + f.getName() + " could not be loaded! It won't be in your map if you safe it");
                    }
                }
                ++i;
            }
        }
        catch (IOException e) {
            this.bdis.close();
            throw e;
        }
        this.bdis.close();
    }

    private ObjectTableEntry readObject(boolean userDefined) throws IOException {
        String origObjId = this.bdis.readCharsAsString(4);
        String newObjId = this.bdis.readCharsAsStringCheckNull(4);
        int numMods = this.bdis.readInt();
        ObjectTableEntry result = userDefined ? new ObjectTableEntry(this.category, origObjId, String.valueOf(this.prefix) + newObjId, numMods * 3 / 2) : new ObjectTableEntry(this.category, origObjId, numMods * 3 / 2);
        int i = 0;
        while (i < numMods) {
            ObjectAttrEntry attr = this.readAttr();
            result.putRawEntry(this.defaultValues.identifierToGameName(attr.getIdent(), result), attr);
            ++i;
        }
        return result;
    }

    private ObjectAttrEntry readAttr() throws IOException {
        DataObject value2;
        String modId = this.bdis.readCharsAsString(4);
        int varType = this.bdis.readInt();
        int level = 0;
        int dataPointer = 0;
        if (this.useExtended) {
            level = this.bdis.readInt();
            dataPointer = this.bdis.readInt();
        }
        switch (varType) {
            case 0: {
				DataObject value2;
                value2 = new IntObject(this.bdis.readInt());
                break;
            }
            case 1: {
				DataObject value2;
                value2 = new FloatObject(this.bdis.readFloat());
                break;
            }
            case 2: {
				DataObject value2;
                value2 = new FloatObject(this.bdis.readFloat());
                break;
            }
            case 3: {
				DataObject value2;
                try {
                    value2 = new MapStringObject(this.bdis.readString(), this.currentMap);
                    break;
                }
                catch (InternalScriptError e) {
                    throw new IOException(e.getMessage());
                }
            }
            default: {
                throw new Error("Unknown varType: " + varType + " at offset " + this.bdis.getOffset() + " in file " + this.file.getAbsolutePath());
            }
        }
        ObjectAttrEntry result = this.useExtended ? new ObjectAttrEntry(new AttrIdentifier(modId, level, dataPointer), value2) : new ObjectAttrEntry(new AttrIdentifier(modId), value2);
        this.bdis.readInt();
        return result;
    }
}

