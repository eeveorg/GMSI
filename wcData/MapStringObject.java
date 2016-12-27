/*
 * Decompiled with CFR 0_119.
 */
package wcData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.InternalScriptError;
import script.dataObjects.StringObject;
import wcData.MapHandle;

public class MapStringObject
extends StringObject {
    private static Pattern trigStrPattern = Pattern.compile("TRIGSTR_([0-9]+)");

    private static String inlineTrigStr(String input, MapHandle m) throws InternalScriptError {
        Matcher match = trigStrPattern.matcher(input);
        if (match.matches()) {
            return m.getAndRemoveTrigstr(Integer.parseInt(match.group(1)));
        }
        return input;
    }

    public MapStringObject(String s, MapHandle m) throws InternalScriptError {
        super(MapStringObject.inlineTrigStr(s, m));
    }
}

