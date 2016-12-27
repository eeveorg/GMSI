/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.image.BufferedImage;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.systemCalls.Trap;

public class Trap_GetImageHeight
extends Trap {
    public Trap_GetImageHeight(Script s) {
        super(s, "getImageHeight");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object o = this.getObjParam(0, true);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        return new IntObject(in.getHeight());
    }
}

