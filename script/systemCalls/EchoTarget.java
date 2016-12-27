/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

public interface EchoTarget {
    public void print(String var1, EchoType var2);

    public static class EchoType {
        public static EchoType WARNING = new EchoType();

        private EchoType() {
        }
    }

}

