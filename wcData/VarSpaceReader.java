/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import script.InternalScriptError;
import script.dataObjects.ArrayObject;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.IntVarSpace;
import script.names.VarSpace;
import wcData.BlizzardDataOutputStream;
import wcData.MapHandle;

public class VarSpaceReader {
    VarSpace readFrom;
    private BlizzardDataOutputStream out;
    private MapHandle h;

    public VarSpaceReader(MapHandle h, BlizzardDataOutputStream out, VarSpace readFrom) {
        this.readFrom = readFrom;
        this.out = out;
        this.h = h;
    }

    public void writeString(Object name) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        this.out.writeString(((StringObject)o).getValue());
    }

    public int writeInt(Object name) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        int result = ((IntObject)o).getValue();
        this.out.writeInt(result);
        return result;
    }

    public void writeByte(Object name) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        this.out.writeByte(((IntObject)o).getValue());
    }

    public void writeNBytesInt(Object name, int numBytes) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        this.out.writeNByteInt(((IntObject)o).getValue(), numBytes);
    }

    public void writeBool(Object name) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        this.out.writeBool(((BoolObject)o).getValue());
    }

    public void writeFloat(Object name) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        this.out.writeFloat(((FloatObject)o).getValue());
    }

    public void writeArray(Object name, Class c) throws IOException, InternalScriptError {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        ArrayObject a = (ArrayObject)o;
        VarSpaceReader r = new VarSpaceReader(this.h, this.out, a.getIntSpace());
        for (Object i : a.getIntSpace().getKeys()) {
            r.write(i, c);
        }
    }

    public void write(Object name, Class c) throws InternalScriptError, IOException {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        if (c == StringObject.class) {
            this.out.writeString(((StringObject)o).getValue());
            return;
        }
        if (c == IntObject.class) {
            this.out.writeInt(((IntObject)o).getValue());
            return;
        }
        if (c == BoolObject.class) {
            this.out.writeBool(((BoolObject)o).getValue());
            return;
        }
        if (c == FloatObject.class) {
            this.out.writeFloat(((FloatObject)o).getValue());
            return;
        }
        throw new Error("wrong use of write: " + o.getClass().getSimpleName());
    }

    public String writeNCharsAsString(Object name, int charCount) throws InternalScriptError, IOException {
        String result;
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (!(o instanceof NullObject)) {
            result = ((StringObject)o).getValue();
        } else {
            result = "";
            int i = 0;
            while (i < charCount) {
                result = String.valueOf(result) + "\u0000";
                ++i;
            }
        }
        this.out.writeNByteString(result, charCount);
        return result;
    }

    public VarSpaceReader writeArray(Object name) throws InternalScriptError, IOException {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            return new VarSpaceReader(this.h, this.out, null);
        }
        if (!(o instanceof ArrayObject)) {
            throw new InternalScriptError("Save error: Entry " + name + " is not an array, but a " + o.getType());
        }
        ArrayObject result = (ArrayObject)this.readFrom.get(name);
        return new VarSpaceReader(this.h, this.out, result.getIntSpace());
    }

    public VarSpaceReader writeStruct(Object name) throws InternalScriptError, IOException {
        DataObject o = this.readFrom.get(name);
        if (o == null) {
            throw new InternalScriptError("Save error: Missing entry " + name);
        }
        if (o instanceof NullObject) {
            throw new InternalScriptError("Save error: Entry " + name + " is null!");
        }
        if (!(o instanceof StructObject)) {
            throw new InternalScriptError("Save error: Entry " + name + " is not a struct, but a " + o.getType());
        }
        StructObject result = (StructObject)this.readFrom.get(name);
        return new VarSpaceReader(this.h, this.out, result.getVarSpace());
    }

    public int getVarSpaceSize() {
        if (this.readFrom == null) {
            return 0;
        }
        return this.readFrom.getKeys().size();
    }

    public Set<? extends Object> getVarSpaceKeys() {
        if (this.readFrom == null) {
            return new HashSet();
        }
        return this.readFrom.getKeys();
    }

    public int writeBitfield(int numByte, Object[] names) throws InternalScriptError, IOException {
        if (names.length > 8 * numByte) {
            throw new InternalScriptError("too many bools for bitfield!");
        }
        int currentBit = 1;
        int bitField = 0;
        int i = 0;
        int max = names.length;
        while (i < max) {
            if (names[i] != null) {
                DataObject o = this.readFrom.get(names[i]);
                if (o == null) {
                    throw new InternalScriptError("Save error: Missing entry " + names[i]);
                }
                if (o instanceof NullObject) {
                    throw new InternalScriptError("Save error: Entry " + names[i] + " is null!");
                }
                if (((BoolObject)o).getValue()) {
                    bitField |= currentBit;
                }
            }
            currentBit <<= 1;
            ++i;
        }
        this.out.writeNByteInt(bitField, numByte);
        return bitField;
    }

    public VarSpace getVarSpace() {
        return this.readFrom;
    }
}

