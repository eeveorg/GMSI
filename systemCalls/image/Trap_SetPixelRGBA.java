/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_SetPixelRGBA
extends Trap {
    public Trap_SetPixelRGBA(Script s) {
        super(s, "setPixelRGBA");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object o = this.getObjParam(0, true);
        int x = this.getIntParam(1, true);
        int y = this.getIntParam(2, true);
        int r = this.getIntParam(3, true);
        int g = this.getIntParam(4, true);
        int b = this.getIntParam(5, true);
        int a = this.getIntParam(6, true);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        in.setRGB(x, y, new Color(r, g, b, a).getRGB());
        return voidResult;
    }
}

