/*
 * Decompiled with CFR 0_119.
 */
package script.patterns;

import script.patterns.PatternTransformer;

public class NullTransformer
implements PatternTransformer {
    @Override
    public String transform(String input) {
        return input;
    }
}

