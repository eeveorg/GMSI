/*
 * Decompiled with CFR 0_119.
 */
package program;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class test {
    public static double lol(double t) {
        double arg = Math.pow(2.718281828459045, -5.0 * t / 7.0);
        return 0.5 - (arg *= 1.0 + 5.0 * t / 7.0);
    }

    public String incString(String toInc) {
        int stelle;
        int val;
        block4 : {
            block5 : {
                stelle = toInc.length() - 1;
                do {
                    if ((val = toInc.charAt(stelle)) >= 48 && val < 57) {
                        ++val;
                        break block4;
                    }
                    if (val == 57) {
                        val = 65;
                        break block4;
                    }
                    if (val >= 65 && val < 90) {
                        ++val;
                        break block4;
                    }
                    if (val != 90) break block5;
                    val = 48;
                    toInc = String.valueOf(toInc.substring(0, stelle)) + (char)val + toInc.substring(stelle + 1, toInc.length());
                } while (--stelle != -1);
                return null;
            }
            return null;
        }
        return String.valueOf(toInc.substring(0, stelle)) + (char)val + toInc.substring(stelle + 1, toInc.length());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new File("").getAbsolutePath());
    }

    public static void testFIle() throws IOException {
    }
}

