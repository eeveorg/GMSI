/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.InternalScriptError;
import script.patterns.MatchHandler;
import script.patterns.PatternTransformer;

public class MatcherBuilder {
    private PatternTransformer patternTransformer;
    private Pattern globalMatch = null;
    private String patternString = "";
    private int currentOffset = 0;
    private LinkedList<Integer> offsets = new LinkedList();
    private ArrayList<MatchHandler> handlers = new ArrayList();

    public MatcherBuilder(PatternTransformer t) {
        this.patternTransformer = t;
    }

    public String getPatternString() {
        return this.patternString;
    }

    public void addMatch(String pattern, MatchHandler handler) {
        this.globalMatch = null;
        pattern = "(" + this.patternTransformer.transform(pattern) + ")";
        Pattern p = Pattern.compile(pattern);
        this.offsets.add(p.matcher("").groupCount());
        this.currentOffset += this.offsets.getLast().intValue();
        this.patternString = this.patternString.equals("") ? pattern : String.valueOf(this.patternString) + "|" + pattern;
        this.handlers.add(handler);
    }

    public String applyMatching(String input) throws InternalScriptError {
        if (this.globalMatch == null) {
            this.globalMatch = Pattern.compile(this.patternString);
        }
        StringBuilder b = new StringBuilder(input.length() * 3 / 2);
        Matcher m = this.globalMatch.matcher(input);
        int lastPatternEnd = 0;
        while (m.find()) {
            String patternResult;
            b.append(input.substring(lastPatternEnd, m.start()));
            lastPatternEnd = m.end();
            int matchingPattern = 0;
            int curPattern = 1;
            while (m.group(curPattern) == null) {
                curPattern += this.offsets.get(matchingPattern).intValue();
                ++matchingPattern;
            }
            String[] args = new String[this.offsets.get(matchingPattern) - 1];
            int j = 0;
            int max = this.offsets.get(matchingPattern) - 1;
            while (j < max) {
                args[j] = m.group(curPattern + 1 + j);
                ++j;
            }
            try {
                patternResult = this.handlers.get(matchingPattern).handleMatch(args);
            }
            catch (InternalScriptError e) {
                throw new InternalScriptError(String.valueOf(e.getMessage()) + "\nfound in string:\n" + input);
            }
            b.append(patternResult);
        }
        b.append(input.substring(lastPatternEnd));
        return b.toString();
    }
}

