/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.systemCalls.Trap;
import script.systemCalls.TrapHandler;

public class Trap_F2S
extends Trap {
    private static Pattern leadingZeros = Pattern.compile("^(0*)((?:[0-9]\\.)|(?:[1-9][0-9]*\\.))");
    private static Pattern tailZeros = Pattern.compile("\\.?(0*)$");
    private static StringBuilder zeroBuilder = new StringBuilder(16);
    static long[] tenpows = new long[9];

    static {
        Trap_F2S.tenpows[0] = 1;
        Trap_F2S.tenpows[1] = 10;
        Trap_F2S.tenpows[2] = 100;
        Trap_F2S.tenpows[3] = 1000;
        Trap_F2S.tenpows[4] = 10000;
        Trap_F2S.tenpows[5] = 100000;
        Trap_F2S.tenpows[6] = 1000000;
        Trap_F2S.tenpows[7] = 10000000;
        Trap_F2S.tenpows[8] = 100000000;
    }

    public Trap_F2S(Script s, TrapHandler t) {
        super(s, t, "f2s");
    }

    private static String zeros(int num) {
        zeroBuilder.setLength(0);
        int i = 0;
        while (i < num) {
            zeroBuilder.append("0");
            ++i;
        }
        return zeroBuilder.toString();
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        boolean minus;
        long cur;
        float f = this.getFloatParam(0);
        int significant = this.getIntParam(1);
        if (f == 0.0f) {
            return new StringObject("0");
        }
        boolean bl = minus = f < 0.0f;
        if (minus) {
            f = - f;
        }
        if (significant > 6) {
            significant = 6;
        } else if (significant < 1) {
            significant = 1;
        }
        long pow = tenpows[significant];
        int pot = 0;
        while ((cur = (long)((double)f * Math.pow(10.0, pot) + 0.5)) < pow) {
            ++pot;
        }
        pow = tenpows[significant + 1];
        while ((cur = (long)((double)f * Math.pow(10.0, pot) + 0.5)) >= pow) {
            --pot;
        }
        if (cur % 10 >= 5) {
            cur += 10;
        }
        System.out.println(--pot);
        String result = "" + (cur /= 10);
        if (pot > 0) {
            result = String.valueOf(Trap_F2S.zeros(pot)) + result;
            result = String.valueOf(result.substring(0, significant)) + "." + result.substring(significant);
        } else if (pot < 0) {
            result = String.valueOf(result) + Trap_F2S.zeros(- pot);
            pot = - pot + significant;
            result = String.valueOf(result.substring(0, pot)) + "." + result.substring(pot);
        } else {
            result = String.valueOf(result) + ".";
        }
        result = leadingZeros.matcher(result).replaceFirst("$2");
        result = tailZeros.matcher(result).replaceFirst("");
        if (minus) {
            result = "-" + result;
        }
        return new StringObject(result);
    }
}

