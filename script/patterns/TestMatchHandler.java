/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import java.io.PrintStream;
import script.InternalScriptError;
import script.patterns.MatchHandler;
import script.patterns.MatcherBuilder;
import script.patterns.NullTransformer;
import script.patterns.PatternTransformer;
import script.patterns.SimpleMatchHandler;

public class TestMatchHandler
implements MatchHandler {
    private String name = "";

    public TestMatchHandler(String s) {
        this.name = s;
    }

    @Override
    public String handleMatch(String[] groups) {
        int i = 0;
        while (i < groups.length) {
            System.out.println(String.valueOf(this.name) + ":" + groups[i]);
            ++i;
        }
        return "";
    }

    public static void main(String[] args) throws InternalScriptError {
        MatcherBuilder b = new MatcherBuilder(new NullTransformer());
        b.addMatch("\\#([0-9]+) ([0-9]+)\\#", new SimpleMatchHandler("$1 $1 \\$1 $2 $1"));
        b.addMatch("\\#(\\w+)\\#", new SimpleMatchHandler("$1 $1"));
        System.out.println(b.getPatternString());
        System.out.println(b.applyMatching("asf #14 32534# lkjlsjee  (#wer4#)"));
    }
}

