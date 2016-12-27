/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;
import wcData.BLP_File;

public class Trap_LoadImage
extends Trap {
    public Trap_LoadImage(Script s) {
        super(s, "loadImage");
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        BufferedImage out;
        String file = this.getStrParam(0, true);
        File f = this.strToFile(file);
        if (!f.exists()) {
            throw new InternalScriptError("Trap error: Cannot load the image from file " + file + " because it doesn't exist!");
        }
        if (f.isDirectory()) {
            throw new InternalScriptError("Trap error: Cannot load the image from file " + file + " because it is a directory, not a file!");
        }
        try {
            out = file.endsWith(".blp") ? BLP_File.read(f) : ImageIO.read(f);
        }
        catch (IOException e) {
            throw new InternalScriptError(e.getMessage());
        }
        ObjObject obj = new ObjObject(out);
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Image"));
        obj.explicitCastTo(e);
        return obj;
    }
}

