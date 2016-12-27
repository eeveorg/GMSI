/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.LinkedList;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.TypedDatafield;
import script.names.LocalVarHandle;
import script.names.VarHandle;

public class LocalNameStack {
    private LinkedList<TypedDatafield[]> stack = new LinkedList();
    private LocalVarHandle[] handleCache = new LocalVarHandle[1024];
    private int cacheOffset = 0;

    public LocalNameStack() {
        int i = 0;
        while (i < 1024) {
            this.handleCache[i] = new LocalVarHandle(null, -1);
            ++i;
        }
    }

    public void pushSpaceNew(int size) {
        this.cacheOffset = this.cacheOffset + size & 1023;
        this.stack.addFirst(new TypedDatafield[size]);
    }

    public void popBig() {
        this.cacheOffset = this.cacheOffset - this.stack.poll().length + 1024 & 1023;
    }

    public VarHandle getHandle(int num) {
        int cacheNum = this.cacheOffset + num & 1023;
        LocalVarHandle cached = this.handleCache[cacheNum];
        if (cached.space == this.stack.peek() && cached.num == num) {
            return cached;
        }
        this.handleCache[cacheNum] = new LocalVarHandle(this.stack.peek(), num);
        return this.handleCache[cacheNum];
    }

    public void addLocal(int num, DataObject content, DataType type) throws InternalScriptError {
        TypedDatafield[] space = this.stack.peek();
        if (space[num] == null) {
            space[num] = new TypedDatafield(content, type);
        } else {
            TypedDatafield d = space[num];
            d.content = content;
            d.type = type;
        }
    }
}

