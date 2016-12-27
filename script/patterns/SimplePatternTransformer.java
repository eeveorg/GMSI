/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.patterns.PatternTransformer;

public class SimplePatternTransformer
implements PatternTransformer {
    private static Pattern word = Pattern.compile("\\%w");
    private static Pattern integer = Pattern.compile("\\%i");
    private static Pattern flot = Pattern.compile("\\%f");
    private static Pattern everything = Pattern.compile("\\%e");
    private static Pattern escape2 = Pattern.compile("\\%");
    private static Pattern escape = Pattern.compile("[^\\w^\\s^\\%]");
    private static LinkedList<MatcherPair> patterns = new LinkedList();

    static {
        patterns.add(new MatcherPair(word, "(\\\\w+)"));
        patterns.add(new MatcherPair(integer, "([\\\\-\\\\+]?\\\\d+)"));
        patterns.add(new MatcherPair(flot, "([\\\\-\\\\+]?(?:\\\\.?\\\\d+)|(?:\\\\d+\\\\.\\\\d?))"));
        patterns.add(new MatcherPair(everything, "((?:.|\\\\n|\\\\r)*?)"));
    }

    @Override
    public String transform(String input) {
        input = escape.matcher(input).replaceAll("\\\\$0");
        for (MatcherPair pair : patterns) {
            input = pair.getPattern().matcher(input).replaceAll(pair.getReplacement());
        }
        input = escape2.matcher(input).replaceAll("\\\\$0");
        return input;
    }

    private static class MatcherPair {
        private Pattern pattern;
        private String replacement;

        public MatcherPair(Pattern p, String r) {
            this.pattern = p;
            this.replacement = r;
        }

        public Pattern getPattern() {
            return this.pattern;
        }

        public String getReplacement() {
            return this.replacement;
        }
    }

}

