/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;

public class Trap_OpenFileRead
extends Trap {
    private LinkedList<BufferedReader> openFiles = new LinkedList();

    public Trap_OpenFileRead(Script s) {
        super(s);
    }

    @Override
    public void finish() {
        for (BufferedReader r : this.openFiles) {
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
        try {
            BufferedReader r = new BufferedReader(new FileReader(this.strToFile(fileName)));
            this.openFiles.add(r);
            DataType e = new DataType(this.owner.getNameResolver().getUserDef("Reader"));
            ObjObject o = new ObjObject(r);
            DataObject i = o.explicitCastTo(e);
            return i;
        }
        catch (FileNotFoundException e) {
            throw new InternalScriptError("Trap Error: Unable to open file " + fileName + ": File not found!");
        }
    }
}

