/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import script.InternalScriptError;
import script.ParseError;
import script.Script;
import script.SyntaxError;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_Include
extends Trap {
    private HashSet<String> included = new HashSet();

    public Trap_Include(Script s) {
        super(s);
    }

    @Override
    public void reset() {
        this.included = new HashSet();
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String fileName = this.getStrParam(0, true);
        File f = new File(String.valueOf(this.owner.getCurrentPath().getAbsolutePath()) + "/" + fileName);
        if (!f.exists() || !f.isFile()) {
            LinkedList<File> lookup = this.owner.getScriptPaths();
            boolean found = false;
            for (File e : lookup) {
                f = new File(String.valueOf(e.getPath()) + "/" + fileName);
                if (!f.exists() || !f.isFile()) continue;
                found = true;
                break;
            }
            if (!found) {
                throw new InternalScriptError("Trap error: Failed to include \"" + fileName + "\", file not found in any lookup path!");
            }
        }
        if (this.included.contains(f.getAbsolutePath())) {
            return voidResult;
        }
        try {
            this.included.add(f.getAbsolutePath());
            this.owner.execFile(f, true);
        }
        catch (IOException e1) {
            throw new InternalScriptError("IO error while trying to include " + f.getAbsolutePath() + ":" + e1.getMessage());
        }
        catch (SyntaxError e1) {
            throw new InternalScriptError("Syntax error in included File " + f.getAbsolutePath() + ":\n" + e1.getMessage());
        }
        catch (ParseError e1) {
            throw new InternalScriptError("Parse error in included File " + f.getAbsolutePath() + ":\n" + e1.getMessage());
        }
        return voidResult;
    }
}

