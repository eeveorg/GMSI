/*
 * Decompiled with CFR 0_119.
 */
package systemCalls;

import java.io.File;
import java.io.IOException;
import program.Program;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StructObject;
import script.systemCalls.Trap;

public class Trap_OpenMap
extends Trap {
    public Trap_OpenMap(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String archiveName = this.getStrParam(0, true);
        if (archiveName == null) {
            throw new InternalScriptError("Trap error: mapName may not be null!");
        }
        try {
            return Program.getMapLoader().openMap(new File(archiveName));
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
    }
}

