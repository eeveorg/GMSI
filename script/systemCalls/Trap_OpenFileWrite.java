/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;

public class Trap_OpenFileWrite
extends Trap {
    private LinkedList<BufferedWriter> openFiles = new LinkedList();

    public Trap_OpenFileWrite(Script s) {
        super(s);
    }

    @Override
    public void finish() {
        for (BufferedWriter r : this.openFiles) {
            try {
                r.close();
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String fileName = this.getStrParam(0, true);
        boolean append = this.getBoolParam(1);
        try {
            BufferedWriter r = new BufferedWriter(new FileWriter(this.strToFile(fileName), append));
            this.openFiles.add(r);
            DataType e = new DataType(this.owner.getNameResolver().getUserDef("Writer"));
            ObjObject o = new ObjObject(r);
            DataObject i = o.explicitCastTo(e);
            return i;
        }
        catch (FileNotFoundException e) {
            throw new InternalScriptError("Trap Error: Unable to open file " + fileName + ": File not found!");
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
    }
}

