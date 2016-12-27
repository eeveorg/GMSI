/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import imageIO.ImageUtils;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;

public class Trap_RescaleImage
extends Trap {
    public Trap_RescaleImage(Script s) {
        super(s, "rescaleImage");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object hint;
        Object o = this.getObjParam(0, true);
        int width = this.getIntParam(1, true);
        int height = this.getIntParam(2, true);
        int interp = this.getIntParam(3, true);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        if (width <= 0 || height <= 0) {
            throw new InternalScriptError("Trap error: width and height must be greater than zero, but they are " + width + "," + height);
        }
        if (interp == 0) {
            hint = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        } else if (interp == 1) {
            hint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        } else if (interp == 2) {
            hint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
        } else {
            throw new InternalScriptError("Trap error: The specified interpolation techinque is not valid!");
        }
        ObjObject obj = new ObjObject(ImageUtils.getScaledInstance(in, width, height, hint, true));
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Image"));
        obj.explicitCastTo(e);
        return obj;
    }
}

