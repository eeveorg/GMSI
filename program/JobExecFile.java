/*
 * Decompiled with CFR 0_119.
 */
package program;

import java.io.File;
import program.Job;
import program.Program;
import program.misc.Log;
import script.Stopwatch;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.NameResolver;
import script.systemCalls.TrapHandler;

public class JobExecFile
implements Job {
    private File file;
    private boolean isMap;
    private String mapOrScript;

    public JobExecFile(File file, boolean isMap) {
        this.file = file;
        this.isMap = isMap;
        this.mapOrScript = isMap ? "map" : "script";
    }

    public JobExecFile(File file) {
        this.file = file;
        this.isMap = file.getName().endsWith(".w3x");
        this.mapOrScript = this.isMap ? "map" : "script";
    }

    @Override
    public boolean execute() {
        block7 : {
            try {
                try {
                    Program.getScript().reset();
                }
                catch (Exception e) {
                    Log.exception(e);
                }
                if (this.isMap) {
                    Program.getScript().getNameResolver().addExternalVar("map", new StringObject(this.file.getAbsolutePath()));
                }
                if (Log.doLog(0, 0)) {
                    Log.println("\n\n--- Executing " + this.mapOrScript + " " + this.file.getName() + " ---");
                }
                Stopwatch.reset("script");
                Stopwatch.start("script");
                Program.getMapLoader().reset();
                Program.getScript().execFile(this.isMap ? new File("misc/executeMap.gsl") : this.file, true);
                if (Log.doLog(0, 0)) {
                    Log.println("\n--- Finished execution of " + this.file.getName() + ". Time: " + Stopwatch.stopString("script") + " sec ---");
                }
            }
            catch (Throwable e1) {
                Log.exception(e1);
                if (!Log.doLog(0, 0)) break block7;
                Log.println("\n--- Execution was aborted due to an error ---");
            }
        }
        Program.getScript().getTrapHandler().finishTraps();
        return true;
    }
}

