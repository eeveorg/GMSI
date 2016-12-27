/*
 * Decompiled with CFR 0_119.
 */
package program.misc;

import java.util.HashMap;

public class Misc {
    private static HashMap<String, String> categoryMapping = new HashMap();

    static {
        categoryMapping.put("unit", "Unit");
        categoryMapping.put("abil", "Ability");
        categoryMapping.put("destr", "Destructable");
        categoryMapping.put("dood", "Doodad");
        categoryMapping.put("item", "Item");
        categoryMapping.put("buff", "Buff");
        categoryMapping.put("upgrade", "Upgr");
    }

    public static String getTypeFromCategory(String s) {
        return categoryMapping.get(s);
    }

    public static boolean isIdCouraged(String id) {
        if (id.length() != 4) {
            return false;
        }
        int stelle = id.length();
        while (stelle >= 2) {
            char val;
            if ((val = id.charAt(--stelle)) >= '0' && val <= '9' || val >= 'A' && val <= 'Z' || val >= 'a' && val <= 'z') continue;
            return false;
        }
        return true;
    }

    public static boolean isIdValid(String id) {
        if (id.length() != 4) {
            return false;
        }
        int stelle = id.length();
        while (stelle >= 2) {
            char val;
            if ((val = id.charAt(--stelle)) <= '\u00ff') continue;
            return false;
        }
        return true;
    }
}

