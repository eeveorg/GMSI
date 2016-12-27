/*
 * Decompiled with CFR 0_119.
 */
package systemCalls.image;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import script.InternalScriptError;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.ObjObject;
import script.names.NameResolver;
import script.systemCalls.Trap;

public class Trap_TakeScreenshot
extends Trap {
    Robot r;
    Rectangle screen;

    public Trap_TakeScreenshot(Script s) {
        super(s, "takeScreenshot");
        try {
            this.r = new Robot();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
        Toolkit t = Toolkit.getDefaultToolkit();
        this.screen = new Rectangle(0, 0, t.getScreenSize().width, t.getScreenSize().height);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        BufferedImage out = this.r.createScreenCapture(this.screen);
        ObjObject obj = new ObjObject(out);
        DataType e = new DataType(this.owner.getNameResolver().getUserDef("Image"));
        obj.explicitCastTo(e);
        return obj;
    }
}

