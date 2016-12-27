/*
 * Decompiled with CFR 0_119.
 */
package program;

import program.Job;
import program.Program;
import program.misc.Log;

public class WorkerThread
extends Thread {
    private boolean busy;
    private Job currentJob;

    public WorkerThread(Job j) {
        this.busy = true;
        this.currentJob = j;
    }

    public WorkerThread() {
        this.busy = false;
    }

    @Override
    public void run() {
        try {
            try {
                this.currentJob.execute();
            }
            catch (OutOfMemoryError e) {
                if (Log.doLog(0, 1)) {
                    Log.println("--- Out of memory, restart the program! If this error occurs often you should increase max heap space of your Java Virtual Machine ---");
                }
                Program.fireJobFinished(this.currentJob);
                this.busy = false;
            }
        }
        finally {
            Program.fireJobFinished(this.currentJob);
            this.busy = false;
        }
    }

    public boolean isBusy() {
        return this.busy;
    }
}

