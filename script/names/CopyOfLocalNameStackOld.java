/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.LinkedList;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.names.StandardVarSpace;
import script.names.VarHandle;
import script.names.VarSpace;

public class CopyOfLocalNameStackOld {
    private LinkedList<LinkedList<StandardVarSpace>> stack = new LinkedList();

    public void pushBig(StandardVarSpace arg0) {
        LinkedList<StandardVarSpace> toPush = new LinkedList<StandardVarSpace>();
        toPush.addFirst(arg0);
        this.stack.addFirst(toPush);
    }

    public void pushBigNew() {
        LinkedList<StandardVarSpace> toPush = new LinkedList<StandardVarSpace>();
        toPush.addFirst(new StandardVarSpace());
        this.stack.addFirst(toPush);
    }

    public void pushSmall(StandardVarSpace arg0) {
        this.stack.peek().addFirst(arg0);
    }

    public void pushSmallNew() {
        this.stack.peek().addFirst(new StandardVarSpace());
    }

    public void popSmall() {
        StandardVarSpace v = this.stack.peek().poll();
    }

    public void popBig() {
        this.stack.poll();
    }

    public boolean hasName(String name) {
        LinkedList<StandardVarSpace> nameSpace = this.stack.peek();
        for (StandardVarSpace curSpace : nameSpace) {
            if (!curSpace.hasName(name)) continue;
            return true;
        }
        return false;
    }

    public VarHandle getHandle(String name) {
        LinkedList<StandardVarSpace> nameSpace = this.stack.peek();
        for (StandardVarSpace curSpace : nameSpace) {
            if (!curSpace.hasName(name)) continue;
            return new VarHandle(curSpace, name);
        }
        return null;
    }

    public void addLocal(String name, DataObject content) throws InternalScriptError {
        LinkedList<StandardVarSpace> nameSpace = this.stack.peek();
        for (StandardVarSpace curSpace : nameSpace) {
            if (!curSpace.hasName(name)) continue;
            throw new InternalScriptError("Redefinition of the local variable \"" + name + "\"!");
        }
        this.stack.peek().peek().put(name, content);
    }
}

