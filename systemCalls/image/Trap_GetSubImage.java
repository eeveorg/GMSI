/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.image.BufferedImage;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;

public class Trap_GetSubImage
extends Trap {
    public Trap_GetSubImage(Script s) {
        super(s, "getSubImage");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        Object o = this.getObjParam(0, true);
        int x = this.getIntParam(1, true);
        int y = this.getIntParam(2, true);
        int width = this.getIntParam(3, true);
        int height = this.getIntParam(4, true);
        if (!(o instanceof BufferedImage)) {
            throw new InternalScriptError("Trap error: The first parameter handed to rescaleImage is no valid image!");
        }
        BufferedImage in = (BufferedImage)o;
        if (width <= 0 || height <= 0) {
            throw new InternalScriptError("Trap error: width and height must be greater than zero, but they are " + width + "," + height);
        }
        ObjObject obj = new ObjObject(in.getSubimage(x, y, width, height));
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Image"));
        obj.explicitCastTo(e);
        return obj;
    }
}

