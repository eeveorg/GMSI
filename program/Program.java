/*
 * Decompiled with CFR 0_119.
 */
package program;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import program.Job;
import program.JobExecFile;
import program.JobListener;
import program.WorkerThread;
import program.misc.IniHandler;
import program.misc.UpdateFiles;
import program.ui.MonitorFrame;
import program.ui.MonitorPanel;
import program.ui.MyMainFrame;
import script.Script;
import script.dataObjects.ArrayObject;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.NameResolver;
import script.names.VarHandle;
import script.systemCalls.Trap;
import script.systemCalls.TrapHandler;
import systemCalls.Trap_AddImport;
import systemCalls.Trap_CreateObject;
import systemCalls.Trap_ExportFile;
import systemCalls.Trap_GetBaseId;
import systemCalls.Trap_GetFreeId;
import systemCalls.Trap_GetId;
import systemCalls.Trap_Int2ObjectId;
import systemCalls.Trap_IsIdPredefined;
import systemCalls.Trap_IsIdValid;
import systemCalls.Trap_LoadEnvironment;
import systemCalls.Trap_LoadImports;
import systemCalls.Trap_LoadObjects;
import systemCalls.Trap_LoadPlacedDoodads;
import systemCalls.Trap_LoadPlacedObjects;
import systemCalls.Trap_LoadRects;
import systemCalls.Trap_LoadSounds;
import systemCalls.Trap_LoadTriggers;
import systemCalls.Trap_ObjectId2Int;
import systemCalls.Trap_OpenMap;
import systemCalls.Trap_SaveEnvironment;
import systemCalls.Trap_SaveImports;
import systemCalls.Trap_SaveMap;
import systemCalls.Trap_SaveObjects;
import systemCalls.Trap_SavePlacedDoodads;
import systemCalls.Trap_SavePlacedObjects;
import systemCalls.Trap_SaveRects;
import systemCalls.Trap_SaveSounds;
import systemCalls.Trap_SaveTriggers;
import systemCalls.image.Trap_GetImageHeight;
import systemCalls.image.Trap_GetImageWidth;
import systemCalls.image.Trap_GetPixelA;
import systemCalls.image.Trap_GetPixelB;
import systemCalls.image.Trap_GetPixelG;
import systemCalls.image.Trap_GetPixelR;
import systemCalls.image.Trap_GetPixelRGBA;
import systemCalls.image.Trap_GetSubImage;
import systemCalls.image.Trap_LoadImage;
import systemCalls.image.Trap_RescaleImage;
import systemCalls.image.Trap_SaveImagePNG;
import systemCalls.image.Trap_SaveImageTGA;
import systemCalls.image.Trap_SetPixelRGBA;
import systemCalls.image.Trap_ShowClipbox;
import systemCalls.image.Trap_TakeScreenshot;
import wcData.MapLoader;

public class Program {
    public static final String version = "beta v2.1.20";
    public static final String name = "GMSI";
    private static Script script;
    private static MapLoader mapLoader;
    private static IniHandler ini;
    private static MyMainFrame mainFrame;
    private static WorkerThread workerThread;
    private static MonitorFrame monitorFrame;
    private static Object monitor;
    private static Job lastJob;
    private static LinkedList<JobListener> jobStartListeners;

    static {
        workerThread = new WorkerThread();
        monitor = new Object();
        lastJob = null;
        jobStartListeners = new LinkedList();
    }

    public static void main(String[] args) throws Exception {
        ini = new IniHandler("gmsi.ini", false);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (RuntimeException e) {
            System.out.println("Windows GUI not supported, using Swing standard Look and Feel");
        }
        Program.initScript();
        mapLoader = new MapLoader(new File("temp"), script);
        if (args.length == 0) {
            int result;
            ArrayObject scriptArgs = new ArrayObject();
            script.getNameResolver().addExternalVar("args", scriptArgs);
            System.out.println("No parameters specified, starting up GUI!");
            Runtime.getRuntime().addShutdownHook(new IniSaver(null));
            mainFrame = new MyMainFrame();
            mainFrame.setVisible(true);
            if (!ini.getPropertyBool("misc", "syncdone", false) && (result = JOptionPane.showConfirmDialog(mainFrame, "You haven't synchronized GMSI with your installation of WC3 yet.\n\nYou should do this to ensure that GMSI uses your language settings\nfor object default values.\n\nDo you want to synchronize with WC3 now?", "Synchronize with WC3?", 0, 2)) == 0) {
                Program.getMainFrame().setSelectedTab(1);
                Program.getMainFrame().getMonitor().setSelectedTab(1);
                Program.setJob(new UpdateFiles());
            }
            return;
        }
        monitorFrame = new MonitorFrame();
        monitorFrame.setVisible(true);
        int i = 0;
        while (i < args.length) {
            boolean isMap = false;
            if (args[i].endsWith(".gsl") || args[i].endsWith(".w3x")) {
                if (args[i].endsWith(".w3x")) {
                    isMap = true;
                }
                ArrayObject scriptArgs = new ArrayObject();
                int j = 0;
                while (j < args.length - ++i) {
                    scriptArgs.getEntry(j).setValue(new StringObject(args[j + i]));
                    ++j;
                }
                script.getNameResolver().addExternalVar("args", scriptArgs);
                File file = new File(args[i - 1]);
                if (isMap) {
                    Program.getScript().getNameResolver().addExternalVar("map", new StringObject(file.getAbsolutePath()));
                }
                Program.setJob(new JobExecFile(file));
                return;
            }
            ++i;
        }
        System.out.println("Usage:\n\njava -Xmx512m -jar GMSI.jar scriptname [param1, param2 ...]\n\nThe additional parameters are handed to the script\n\nExample:\njava -Xmx512m -jar GMSI.jar script/buildTooltips.gsl input/myMap.w3x");
    }

    public static MapLoader getMapLoader() {
        return mapLoader;
    }

    private static void initScript() throws Exception {
        String[] scriptPaths;
        script = new Script(new File[]{new File("script/autoexec/essentials.gsl"), new File("script/autoexec/autoexec.gsl")});
        String[] arrstring = scriptPaths = ini.getPropertyString("folders", "libFolders", "./script/lib,./script").split(",");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String path = arrstring[n2];
            script.addScriptPath(new File(path));
            ++n2;
        }
        script.setMaxEval(ini.getPropertyInt("script", "maxeval", 5000000));
        script.getNameResolver().addExternalVar("appVersion", new StringObject("beta v2.1.20"));
        script.getNameResolver().addExternalVar("appName", new StringObject("GMSI"));
        script.getNameResolver().addExternalVar("appPath", new StringObject(new File("").getAbsolutePath()));
        script.getNameResolver().addExternalVar("outputPath", new StringObject(new File(ini.getPropertyString("folders", "outputFolder", "./output")).getAbsolutePath()));
        script.getNameResolver().addExternalVar("inputPath", new StringObject(new File(ini.getPropertyString("folders", "inputFolder", "./input")).getAbsolutePath()));
        script.getNameResolver().addExternalVar("tempPath", new StringObject(new File(ini.getPropertyString("folders", "tempFolder", "./temp")).getAbsolutePath()));
        script.getNameResolver().addExternalVar("exportPath", new StringObject(new File(ini.getPropertyString("folders", "exportFolder", "./export")).getAbsolutePath()));
        script.getNameResolver().addExternalVar("scriptPath", new StringObject(new File(ini.getPropertyString("folders", "scriptFolder", "./script")).getAbsolutePath()));
        script.getNameResolver().addExternalVar("wc3Path", new StringObject(new File(Program.getIni().getPropertyString("folders", "wc3Folder", "")).getAbsolutePath()));
        script.getTrapHandler().addTrap(new Trap_OpenMap(script), "openMap");
        script.getTrapHandler().addTrap(new Trap_LoadObjects(script), "loadObjects");
        script.getTrapHandler().addTrap(new Trap_SaveObjects(script), "saveObjects");
        script.getTrapHandler().addTrap(new Trap_SaveMap(script), "saveMapToFile");
        script.getTrapHandler().addTrap(new Trap_LoadTriggers(script), "loadTriggers");
        script.getTrapHandler().addTrap(new Trap_SaveTriggers(script), "saveTriggers");
        script.getTrapHandler().addTrap(new Trap_LoadPlacedObjects(script), "loadPlacedObjects");
        script.getTrapHandler().addTrap(new Trap_SavePlacedObjects(script), "savePlacedObjects");
        script.getTrapHandler().addTrap(new Trap_LoadPlacedDoodads(script), "loadPlacedDoodads");
        script.getTrapHandler().addTrap(new Trap_SavePlacedDoodads(script), "savePlacedDoodads");
        script.getTrapHandler().addTrap(new Trap_LoadEnvironment(script), "loadEnvironment");
        script.getTrapHandler().addTrap(new Trap_SaveEnvironment(script), "saveEnvironment");
        script.getTrapHandler().addTrap(new Trap_LoadRects(script), "loadRects");
        script.getTrapHandler().addTrap(new Trap_SaveRects(script), "saveRects");
        script.getTrapHandler().addTrap(new Trap_LoadSounds(script), "loadSounds");
        script.getTrapHandler().addTrap(new Trap_SaveSounds(script), "saveSounds");
        script.getTrapHandler().addTrap(new Trap_LoadImports(script), "loadImports");
        script.getTrapHandler().addTrap(new Trap_SaveImports(script), "saveImports");
        script.getTrapHandler().addTrap(new Trap_ExportFile(script), "exportFile");
        script.getTrapHandler().addTrap(new Trap_GetFreeId(script), "getFreeId");
        script.getTrapHandler().addTrap(new Trap_GetBaseId(script), "getBaseId");
        script.getTrapHandler().addTrap(new Trap_GetId(script), "getObjectId");
        script.getTrapHandler().addTrap(new Trap_CreateObject(script), "createObject");
        script.getTrapHandler().addTrap(new Trap_IsIdValid(script), "isIdValid");
        script.getTrapHandler().addTrap(new Trap_IsIdPredefined(script), "isIdPredefined");
        script.getTrapHandler().addTrap(new Trap_AddImport(script), "addImport");
        new systemCalls.image.Trap_GetPixelA(script);
        new systemCalls.image.Trap_GetPixelR(script);
        new systemCalls.image.Trap_GetPixelG(script);
        new systemCalls.image.Trap_GetPixelB(script);
        new systemCalls.image.Trap_GetPixelRGBA(script);
        new systemCalls.image.Trap_GetSubImage(script);
        new systemCalls.image.Trap_LoadImage(script);
        new systemCalls.image.Trap_RescaleImage(script);
        new systemCalls.image.Trap_SetPixelRGBA(script);
        new systemCalls.image.Trap_GetImageWidth(script);
        new systemCalls.image.Trap_GetImageHeight(script);
        new systemCalls.image.Trap_SaveImageTGA(script);
        new systemCalls.image.Trap_SaveImagePNG(script);
        new systemCalls.image.Trap_ShowClipbox(script);
        new systemCalls.image.Trap_TakeScreenshot(script);
        new systemCalls.Trap_ObjectId2Int(script);
        new systemCalls.Trap_Int2ObjectId(script);
        script.reset();
    }

    public static Script getScript() {
        return script;
    }

    public static IniHandler getIni() {
        return ini;
    }

    public static MyMainFrame getMainFrame() {
        return mainFrame;
    }

    public static void fireJobFinished(Job j) {
        for (JobListener l : jobStartListeners) {
            l.jobFinished(j);
        }
    }

    public static void addJobListener(JobListener j) {
        jobStartListeners.add(j);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean setJob(Job j) {
        Object object = monitor;
        synchronized (object) {
            block6 : {
                if (!workerThread.isBusy()) break block6;
                JOptionPane.showMessageDialog(mainFrame, "The program is still busy, you cannot start another task yet", "Still busy!", 0);
                return false;
            }
            if (j instanceof JobExecFile) {
                lastJob = j;
            }
            workerThread = new WorkerThread(j);
        }
        for (JobListener l : jobStartListeners) {
            l.jobStarted(j);
        }
        workerThread.start();
        return true;
    }

    public static Job getLastJob() {
        return lastJob;
    }

    private static class IniSaver
    extends Thread {
        private IniSaver() {
        }

        @Override
        public void run() {
            try {
                ini.toFile(new File("gmsi.ini"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* synthetic */ IniSaver(IniSaver iniSaver) {
            IniSaver iniSaver2;
            iniSaver2();
        }
    }

}

