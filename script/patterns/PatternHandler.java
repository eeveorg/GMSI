/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import script.InternalScriptError;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.TypeDefinition;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.BoolObject;
import script.dataObjects.DataObject;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.names.IntVarSpace;
import script.names.NameResolver;
import script.names.VarSpace;

public class PatternHandler {
    private HashMap<String, Pattern> patterns = new HashMap();
    private Script owner;

    private Pattern getPattern(String p) throws InternalScriptError {
        Pattern result;
        if (this.patterns.containsKey(p)) {
            return this.patterns.get(p);
        }
        String oldStr = p;
        try {
            int insensitive = 0;
            int dotall = 32;
            do {
                if (p.startsWith("\\i")) {
                    insensitive = 2;
                } else {
                    if (!p.startsWith("\\l")) break;
                    dotall = 0;
                }
                p = p.substring(2);
            } while (true);
            result = Pattern.compile(p, dotall + insensitive);
        }
        catch (PatternSyntaxException e) {
            throw new InternalScriptError("Malformed Pattern:\n" + e.getMessage());
        }
        this.patterns.put(oldStr, result);
        return result;
    }

    public PatternHandler(Script owner) {
        this.owner = owner;
    }

    public DataObject find(String input, String pattern) throws InternalScriptError {
        Pattern p = this.getPattern(pattern);
        Matcher m = p.matcher(input);
        if (!m.find()) {
            return new NullObject();
        }
        StructObject o = new StructObject((StructDefinition)this.owner.getNameResolver().getUserDef("Match"));
        VarSpace v = o.getVarSpace();
        v.put("start", new IntObject(m.start()));
        v.put("end", new IntObject(m.end()));
        ArrayObject a = new ArrayObject();
        v.put("groups", a);
        v = a.getIntSpace();
        int max = m.groupCount();
        int i = 0;
        while (i <= max) {
            String cur = m.group(i);
            if (cur == null) {
                v.put(i, new NullObject());
            } else {
                v.put(i, new StringObject(cur));
            }
            ++i;
        }
        return o;
    }

    public ArrayObject findAll(String input, String pattern) throws InternalScriptError {
        Pattern p = this.getPattern(pattern);
        Matcher m = p.matcher(input);
        ArrayObject result = new ArrayObject();
        IntVarSpace resultSpace = result.getIntSpace();
        int index = 0;
        while (m.find()) {
            StructObject o = new StructObject((StructDefinition)this.owner.getNameResolver().getUserDef("Match"));
            VarSpace v = o.getVarSpace();
            v.put("start", new IntObject(m.start()));
            v.put("end", new IntObject(m.end()));
            ArrayObject a = new ArrayObject();
            v.put("groups", a);
            v = a.getIntSpace();
            int max = m.groupCount();
            int i = 0;
            while (i <= max) {
                String cur = m.group(i);
                if (cur == null) {
                    v.put(i, new NullObject());
                } else {
                    v.put(i, new StringObject(cur));
                }
                ++i;
            }
            resultSpace.put(index++, o);
        }
        return result;
    }

    public StringObject replace(String input, String pattern, String replacement) throws InternalScriptError {
        Pattern p = this.getPattern(pattern);
        Matcher m = p.matcher(input);
        try {
            return new StringObject(m.replaceFirst(replacement));
        }
        catch (IndexOutOfBoundsException e) {
            throw new InternalScriptError("Malformed replacement string: " + replacement + "\n" + e.getMessage());
        }
    }

    public StringObject replaceAll(String input, String pattern, String replacement) throws InternalScriptError {
        Pattern p = this.getPattern(pattern);
        Matcher m = p.matcher(input);
        try {
            return new StringObject(m.replaceAll(replacement));
        }
        catch (IndexOutOfBoundsException e) {
            throw new InternalScriptError("Malformed replacement string: " + replacement + "\n" + e.getMessage());
        }
    }

    public BoolObject matches(String input, String pattern) throws InternalScriptError {
        Pattern p = this.getPattern(pattern);
        return BoolObject.getBool(p.matcher(input).matches());
    }
}

