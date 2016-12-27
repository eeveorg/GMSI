/*
 * Decompiled with CFR 0_119.
 */
package script.systemCalls;

import java.util.HashMap;
import java.util.Set;
import script.ParseError;
import script.Script;
import script.systemCalls.Trap;
import script.systemCalls.Trap_AddFileToGSLArchive;
import script.systemCalls.Trap_AddMacro;
import script.systemCalls.Trap_AddReplacement;
import script.systemCalls.Trap_ApplyMatcher;
import script.systemCalls.Trap_CloseFile;
import script.systemCalls.Trap_ConfirmDialog;
import script.systemCalls.Trap_CopyFile;
import script.systemCalls.Trap_Cos;
import script.systemCalls.Trap_CreateGSLArchive;
import script.systemCalls.Trap_Echo;
import script.systemCalls.Trap_Eval;
import script.systemCalls.Trap_F2S;
import script.systemCalls.Trap_Fail;
import script.systemCalls.Trap_FileDialog;
import script.systemCalls.Trap_Find;
import script.systemCalls.Trap_FindAll;
import script.systemCalls.Trap_FormatFloat;
import script.systemCalls.Trap_GetMatcher;
import script.systemCalls.Trap_GetWatchTime;
import script.systemCalls.Trap_Include;
import script.systemCalls.Trap_InputDialog;
import script.systemCalls.Trap_Matches;
import script.systemCalls.Trap_OpenFileRead;
import script.systemCalls.Trap_OpenFileWrite;
import script.systemCalls.Trap_OptionDialog;
import script.systemCalls.Trap_Pow;
import script.systemCalls.Trap_Rand;
import script.systemCalls.Trap_ReadString;
import script.systemCalls.Trap_Replace;
import script.systemCalls.Trap_ReplaceAll;
import script.systemCalls.Trap_ResetWatch;
import script.systemCalls.Trap_SetScriptPath;
import script.systemCalls.Trap_Sin;
import script.systemCalls.Trap_Size;
import script.systemCalls.Trap_Sleep;
import script.systemCalls.Trap_StartWatch;
import script.systemCalls.Trap_StopWatch;
import script.systemCalls.Trap_StrLen;
import script.systemCalls.Trap_StrPos;
import script.systemCalls.Trap_SubStr;
import script.systemCalls.Trap_ToCase;
import script.systemCalls.Trap_Warn;
import script.systemCalls.Trap_Write;

public class TrapHandler {
    private HashMap<String, Trap> traps = new HashMap();
    Script owner;

    public TrapHandler(Script owner) {
        this.owner = owner;
        this.traps.put("echo", new Trap_Echo(owner));
        this.traps.put("warn", new Trap_Warn(owner));
        this.traps.put("fail", new Trap_Fail(owner));
        this.traps.put("formatFloat", new Trap_FormatFloat(owner));
        this.traps.put("strlen", new Trap_StrLen(owner));
        this.traps.put("substr", new Trap_SubStr(owner));
        this.traps.put("strpos", new Trap_StrPos(owner));
        this.traps.put("toCase", new Trap_ToCase(owner));
        this.traps.put("sleep", new Trap_Sleep(owner));
        this.traps.put("size", new Trap_Size(owner));
        this.traps.put("include", new Trap_Include(owner));
        this.traps.put("getNewMatcher", new Trap_GetMatcher(owner));
        this.traps.put("addMacro", new Trap_AddMacro(owner));
        this.traps.put("addReplacement", new Trap_AddReplacement(owner));
        this.traps.put("applyMatcher", new Trap_ApplyMatcher(owner));
        this.traps.put("eval", new Trap_Eval(owner));
        this.traps.put("getWatchTime", new Trap_GetWatchTime(owner));
        this.traps.put("startWatch", new Trap_StartWatch(owner));
        this.traps.put("stopWatch", new Trap_StopWatch(owner));
        this.traps.put("resetWatch", new Trap_ResetWatch(owner));
        this.traps.put("sin", new Trap_Sin(owner));
        this.traps.put("pow", new Trap_Pow(owner));
        this.traps.put("cos", new Trap_Cos(owner));
        this.traps.put("rand", new Trap_Rand(owner));
        this.traps.put("fileDialog", new Trap_FileDialog(owner));
        this.traps.put("confirmDialog", new Trap_ConfirmDialog(owner));
        this.traps.put("inputDialog", new Trap_InputDialog(owner));
        this.traps.put("optionDialog", new Trap_OptionDialog(owner));
        this.traps.put("openFileRead", new Trap_OpenFileRead(owner));
        this.traps.put("openFileWrite", new Trap_OpenFileWrite(owner));
        this.traps.put("closeFile", new Trap_CloseFile(owner));
        this.traps.put("readString", new Trap_ReadString(owner));
        this.traps.put("write", new Trap_Write(owner));
        this.traps.put("replace", new Trap_Replace(owner));
        this.traps.put("replaceAll", new Trap_ReplaceAll(owner));
        this.traps.put("find", new Trap_Find(owner));
        this.traps.put("findAll", new Trap_FindAll(owner));
        this.traps.put("matches", new Trap_Matches(owner));
        this.traps.put("setScriptPath", new Trap_SetScriptPath(owner));
        this.traps.put("createGSLArchive", new Trap_CreateGSLArchive(owner));
        this.traps.put("addFileToGSLArchive", new Trap_AddFileToGSLArchive(owner));
        new script.systemCalls.Trap_CopyFile(owner, this, "copyFile");
        new script.systemCalls.Trap_F2S(owner, this);
    }

    public Trap getTrap(String trapName) throws ParseError {
        if (this.traps.get(trapName) == null) {
            throw new ParseError("Definition of unknown native (" + trapName + ")");
        }
        return this.traps.get(trapName);
    }

    public void resetTraps() {
        for (String s : this.traps.keySet()) {
            this.traps.get(s).reset();
        }
    }

    public void finishTraps() {
        for (String s : this.traps.keySet()) {
            this.traps.get(s).finish();
        }
    }

    public void addTrap(Trap t, String trapName) {
        this.traps.put(trapName, t);
    }
}

