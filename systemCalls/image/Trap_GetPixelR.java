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

public class Trap_GetPixelR
extends Trap {
    public Trap_GetPixelR(Script s) {
        super(s, "getPixelR");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object o = this.getObjParam(0, true);
        int x = this.getIntParam(1, true);
        int y = this.getIntParam(2, true);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        return new IntObject((in.getRGB(x, y) & 16711680) >> 16);
    }
}

