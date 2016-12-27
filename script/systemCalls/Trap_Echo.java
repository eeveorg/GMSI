/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.io.PrintStream;
import script.InternalScriptError;
import script.Script;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.systemCalls.EchoTarget;
import script.systemCalls.Trap;

public class Trap_Echo
extends Trap {
    public static EchoTarget targ = new ConsoleTarget(null);

    public Trap_Echo(Script s) {
        super(s);
    }

    @Override
    public DataObject apply() throws InternalScriptError {
        String message = this.getStrParam(0, "null");
        targ.print(message, null);
        return new StringObject(message);
    }

    private static class ConsoleTarget
    implements EchoTarget {
        private ConsoleTarget() {
        }

        @Override
        public void print(String s, EchoTarget.EchoType t) {
            System.out.print(s);
        }

        /* synthetic */ ConsoleTarget(ConsoleTarget consoleTarget) {
            ConsoleTarget consoleTarget2;
            consoleTarget2();
        }
    }

}

