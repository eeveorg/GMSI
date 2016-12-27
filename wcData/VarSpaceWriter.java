/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.io.IOException;
import program.Program;
import script.InternalScriptError;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.TypeDefinition;
import script.dataObjects.ArrayObject;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.FloatObject;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.IntVarSpace;
import script.names.NameResolver;
import script.names.VarSpace;
import wcData.BlizzardDataInputStream;
import wcData.MapHandle;
import wcData.MapStringObject;

public class VarSpaceWriter {
    protected VarSpace writeTo;
    protected BlizzardDataInputStream in;
    protected MapHandle h;

    public VarSpaceWriter(MapHandle h, BlizzardDataInputStream in, VarSpace writeTo) {
        this.writeTo = writeTo;
        this.in = in;
        this.h = h;
    }

    public boolean writeBool(Object name) throws InternalScriptError, IOException {
        boolean b = this.in.readBool();
        this.writeTo.put(name, BoolObject.getBool(b));
        return b;
    }

    public int writeInt(Object name) throws InternalScriptError, IOException {
        int i = this.in.readInt();
        this.writeTo.put(name, new IntObject(i));
        return i;
    }

    public int writeNBytesInt(Object name, int numBytes) throws InternalScriptError, IOException {
        int i = this.in.readNByteInt(numBytes);
        this.writeTo.put(name, new IntObject(i));
        return i;
    }

    public int writeByte(Object name, boolean signed) throws InternalScriptError, IOException {
        int i = this.in.readByte();
        if (!signed) {
            i = (i + 256) % 256;
        }
        this.writeTo.put(name, new IntObject(i));
        return i;
    }

    public String writeNCharsAsString(Object name, int charCount) throws InternalScriptError, IOException {
        String s = this.in.readCharsAsStringCheckNull(charCount);
        this.writeTo.put(name, new StringObject(s));
        return s;
    }

    public String writeString(Object name) throws InternalScriptError, IOException {
        String s = this.in.readString();
        if (this.h == null) {
            this.writeTo.put(name, new StringObject(s));
        } else {
            this.writeTo.put(name, new MapStringObject(s, this.h));
        }
        return s;
    }

    public float writeFloat(Object name) throws InternalScriptError, IOException {
        float f = this.in.readFloat();
        this.writeTo.put(name, new FloatObject(f));
        return f;
    }

    public Object write(Class c, Object name) throws InternalScriptError, IOException {
        if (c.equals(StringObject.class)) {
            return this.writeString(name);
        }
        if (c.equals(IntObject.class)) {
            return this.writeInt(name);
        }
        if (c.equals(BoolObject.class)) {
            return this.writeBool(name);
        }
        if (c.equals(FloatObject.class)) {
            return Float.valueOf(this.writeFloat(name));
        }
        throw new Error("wrong use of write!" + c.getSimpleName());
    }

    public ArrayObject writeArray(Object name, int numWrites, Class c) throws InternalScriptError, IOException {
        ArrayObject result = new ArrayObject();
        VarSpaceWriter w = new VarSpaceWriter(this.h, this.in, result.getIntSpace());
        int i = 0;
        while (i < numWrites) {
            w.write(c, i);
            ++i;
        }
        this.writeTo.put(name, result);
        return result;
    }

    public VarSpaceWriter writeArray(Object name) throws InternalScriptError, IOException {
        if (this.writeTo.hasName(name) && !(this.writeTo.get(name) instanceof NullObject)) {
            if (!(this.writeTo.get(name) instanceof ArrayObject)) {
                throw new Error("WTF VarSpaceWriter");
            }
            return new VarSpaceWriter(this.h, this.in, ((ArrayObject)this.writeTo.get(name)).getIntSpace());
        }
        ArrayObject result = new ArrayObject();
        this.writeTo.put(name, result);
        return new VarSpaceWriter(this.h, this.in, result.getIntSpace());
    }

    public VarSpaceWriter writeStruct(Object name, String structName) throws InternalScriptError, IOException {
        if (this.writeTo.hasName(name) && !(this.writeTo.get(name) instanceof NullObject)) {
            if (!(this.writeTo.get(name) instanceof StructObject)) {
                throw new Error("WTF VarSpaceWriter");
            }
            return new VarSpaceWriter(this.h, this.in, ((StructObject)this.writeTo.get(name)).getVarSpace());
        }
        StructObject result = new StructObject((StructDefinition)Program.getScript().getNameResolver().getUserDef(structName));
        this.writeTo.put(name, result);
        return new VarSpaceWriter(this.h, this.in, result.getVarSpace());
    }

    public void writeBitfield(int numByte, Object[] names) throws InternalScriptError, IOException {
        if (names.length > 8 * numByte) {
            throw new InternalScriptError("too many bools for bitfield!");
        }
        int currentBit = 1;
        int bitField = this.in.readNByteInt(numByte);
        int i = 0;
        int max = names.length;
        while (i < max) {
            if (names[i] != null) {
                this.writeTo.put(names[i], BoolObject.getBool((bitField & currentBit) == currentBit));
            }
            currentBit <<= 1;
            ++i;
        }
    }

    public VarSpace getVarSpace() {
        return this.writeTo;
    }
}

