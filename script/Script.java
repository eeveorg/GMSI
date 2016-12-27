/*
 * Decompiled with CFR 0_119.
 */
package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import script.EvaluationError;
import script.EvaluationResult;
import script.InternalScriptError;
import script.ParseError;
import script.ParsedCode;
import script.PreprocessedCode;
import script.ScannedCode;
import script.SourceObject;
import script.SyntaxError;
import script.TokenizedCode;
import script.dataObjects.DataObject;
import script.dataObjects.StringObject;
import script.names.NameResolver;
import script.parse.ErrorMessageTable;
import script.parse.Grammar;
import script.parse.ParseGenerator;
import script.parse.Parser;
import script.parse.SyntaxTable;
import script.patterns.PatternHandler;
import script.systemCalls.EchoTarget;
import script.systemCalls.TrapHandler;
import script.systemCalls.Trap_Echo;
import script.systemCalls.Trap_Warn;

public class Script {
    private final String version = "1.16";
    private NameResolver useResolver = new NameResolver();
    private TrapHandler trapHandler;
    private PatternHandler patternHandler;
    private int evalCount;
    private long overallEvalCount;
    private int maxEval;
    private StringBuilder scriptReplacement;
    private Parser parser;
    private LinkedList<File> currentPath;
    private LinkedList<File> scriptLookupPaths;
    private File[] execOnStartup;
    private String currentLocation;
    private EvaluationResult result;
    boolean wantAbort;

    public String getCurrentLocation() {
        return this.currentLocation;
    }

    public void resetScriptReplacement() {
        this.scriptReplacement = new StringBuilder(16);
    }

    public String getScriptReplacement() {
        return this.scriptReplacement.toString();
    }

    public void addScriptReplacement(String toAdd) {
        this.scriptReplacement.append(toAdd);
    }

    public Script(File[] execOnStartup) throws Exception {
        this.trapHandler = new TrapHandler(this);
        this.patternHandler = new PatternHandler(this);
        this.evalCount = 0;
        this.overallEvalCount = 0;
        this.maxEval = 200000;
        this.scriptReplacement = null;
        this.parser = null;
        this.currentPath = new LinkedList();
        this.scriptLookupPaths = new LinkedList();
        this.currentLocation = "";
        this.result = new EvaluationResult();
        this.execOnStartup = execOnStartup;
        this.getNameResolver().addExternalVar("scriptVersion", new StringObject("1.16"));
        this.pushCurrentPath(new File(""));
    }

    public void addScriptPath(File path) {
        this.scriptLookupPaths.add(path);
    }

    public LinkedList<File> getScriptPaths() {
        return this.scriptLookupPaths;
    }

    public TrapHandler getTrapHandler() {
        return this.trapHandler;
    }

    public NameResolver getNameResolver() {
        return this.useResolver;
    }

    public void setMaxEval(int newValue) {
        this.maxEval = newValue;
    }

    public int getEvalCount() throws EvaluationError {
        return this.evalCount;
    }

    public void checkEvalCount() throws InternalScriptError {
        ++this.evalCount;
        ++this.overallEvalCount;
        if (this.evalCount > this.maxEval) {
            throw new InternalScriptError("More than " + this.maxEval + " evaluations! Evaluation stopped. Check your script for infinit loops or looping recursions.");
        }
    }

    public void resetEvalCount() {
        this.evalCount = 0;
    }

    public void setEchoTarget(EchoTarget e) {
        Trap_Echo.targ = e;
        Trap_Warn.targ = e;
    }

    public void execCode(String code, String source) throws IOException, SyntaxError, ParseError, EvaluationError {
        this.parseCode(code, new SourceObject(source)).eval();
    }

    public ParsedCode parseFile(File f) throws IOException, SyntaxError, ParseError, EvaluationError {
        try {
            String s;
            BufferedReader r = new BufferedReader(new FileReader(f));
            StringBuilder b = new StringBuilder();
            while ((s = r.readLine()) != null) {
                b.append(s);
                b.append("\n");
            }
            r.close();
            return this.parseCode(b.toString(), new SourceObject(f.getName()));
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Could not open script file \"" + f.getName() + "\", file does not exist!");
        }
    }

    public void execFile(File f, boolean setPath) throws IOException, SyntaxError, ParseError, EvaluationError {
        if (setPath) {
            this.pushCurrentPath(f.getAbsoluteFile().getParentFile());
        }
        this.parseFile(f).eval();
        if (setPath) {
            this.popCurrentPath();
        }
    }

    public ParsedCode parseCode(String input, SourceObject s) throws SyntaxError, ParseError {
        PreprocessedCode c1 = new PreprocessedCode(input, s);
        TokenizedCode c2 = new TokenizedCode(this, c1);
        ScannedCode c3 = new ScannedCode(this, c2);
        try {
            Parser p = this.getParser();
            ParsedCode c4 = new ParsedCode(p, c3);
            return c4;
        }
        catch (ParseGenerator.ParserGenerationError e) {
            throw new ParseError(e.getMessage());
        }
        catch (IOException e) {
            throw new ParseError(e.getMessage());
        }
    }

    public void reset() throws Exception {
        this.result.clearFlags();
        this.evalCount = 0;
        this.resetScriptReplacement();
        this.useResolver.reset();
        this.trapHandler.resetTraps();
        System.gc();
        if (this.execOnStartup != null) {
            File[] arrfile = this.execOnStartup;
            int n = arrfile.length;
            int n2 = 0;
            while (n2 < n) {
                File f = arrfile[n2];
                this.execFile(f, true);
                ++n2;
            }
        }
    }

    private Parser getParser() throws ParseGenerator.ParserGenerationError, IOException {
        if (this.parser == null) {
            SyntaxTable s = new SyntaxTable(new Grammar());
            s.fromFile(new File("misc/syntax.slr"));
            ErrorMessageTable e = new ErrorMessageTable(new File("misc/parseErrors.err"));
            this.parser = new Parser(s, e);
        }
        return this.parser;
    }

    public File getCurrentPath() {
        return this.currentPath.peek();
    }

    public void updateCurrentPath(File path) {
        this.currentPath.set(0, path);
    }

    public void pushCurrentPath(File currentPath) {
        this.currentPath.addFirst(currentPath);
    }

    public void popCurrentPath() {
        this.currentPath.poll();
    }

    public String getVersion() {
        return "1.16";
    }

    public PatternHandler getPatternHandler() {
        return this.patternHandler;
    }

    public EvaluationResult getEvalResult() {
        return this.result;
    }

    public boolean wantAbort() {
        return this.wantAbort;
    }

    public void abort() {
        this.wantAbort = true;
    }

    void finished() {
        this.wantAbort = false;
    }
}

