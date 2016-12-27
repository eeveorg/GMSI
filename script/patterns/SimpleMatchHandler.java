/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.InternalScriptError;
import script.patterns.MatchHandler;

public class SimpleMatchHandler
implements MatchHandler {
    private int numReplacements;
    private int approxLength = 0;
    private static Pattern p = Pattern.compile("\\$([1-9][0-9]*)");
    private ArrayList<String> snippets = new ArrayList();
    private ArrayList<Integer> patternNums = new ArrayList();

    public SimpleMatchHandler(String pattern) {
        Matcher m = p.matcher(pattern);
        int lastEnd = 0;
        while (m.find()) {
            this.snippets.add(pattern.substring(lastEnd, m.start()));
            this.patternNums.add(Integer.parseInt(m.group(1)) - 1);
            lastEnd = m.end();
        }
        this.snippets.add(pattern.substring(lastEnd));
        this.numReplacements = this.patternNums.size();
        int i = 0;
        int max = this.snippets.size();
        while (i < max) {
            this.approxLength += this.snippets.get(i).length();
            ++i;
        }
        this.approxLength += 5 * this.numReplacements;
    }

    @Override
    public String handleMatch(String[] groups) throws InternalScriptError {
        StringBuilder b = new StringBuilder(this.approxLength);
        int i = 0;
        while (i < this.numReplacements) {
            b.append(this.snippets.get(i));
            try {
                b.append(groups[this.patternNums.get(i)]);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new InternalScriptError("Matcher error: The replacement pattern contains a wildcard with number " + (this.patternNums.get(i) + 1) + ". The search pattern doesn't contain that many wildcards!");
            }
            ++i;
        }
        b.append(this.snippets.get(this.numReplacements));
        return b.toString();
    }
}

