/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import imageIO.TGA32Encoder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.VoidObject;
import script.systemCalls.Trap;

public class Trap_SaveImageTGA
extends Trap {
    public Trap_SaveImageTGA(Script s) {
        super(s, "saveImageTGA");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object o = this.getObjParam(0, true);
        String file = this.getStrParam(1, true);
        File f = this.strToFile(file);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        if (f.isDirectory()) {
            throw new InternalScriptError("Trap error: Cannot save the image to file " + file + " because it is a directory, not a file!");
        }
        try {
            TGA32Encoder.encodeTGA(in, f);
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        return voidResult;
    }
}

