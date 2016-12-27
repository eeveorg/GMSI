/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import script.EvaluationError;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRfinalTokens.FunctionDefinition;
import script.LRhighTokens.Signature;
import script.LRhighTokens.TypeName;
import script.LRterminals.CloseBracket;
import script.LRterminals.Identifyer;
import script.LRterminals.OpenBracket;
import script.LRterminals.Semicolon;
import script.LRterminals.SimpleTypeName;
import script.LRterminals.WordNative;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.systemCalls.Trap;
import script.systemCalls.TrapHandler;

public class NativeFunctionDefinition
extends FunctionDefinition {
    private Trap trap;

    public NativeFunctionDefinition(WordNative n, TypeName r, Identifyer f, OpenBracket o, Signature s, CloseBracket c, Semicolon se) throws ParseError {
        super(r, f, s);
        this.trap = this.owner.getTrapHandler().getTrap(f.getCode());
    }

    public NativeFunctionDefinition(WordNative n, TypeName r, SimpleTypeName f, OpenBracket o, Signature s, CloseBracket c, Semicolon se) throws ParseError {
        super(r, new Identifyer(f), s);
        this.trap = this.owner.getTrapHandler().getTrap(f.getCode());
    }

    public NativeFunctionDefinition(WordNative n, TypeName r, Identifyer f, OpenBracket o, CloseBracket c, Semicolon se) throws ParseError {
        super(r, f, null);
        this.trap = this.owner.getTrapHandler().getTrap(f.getCode());
    }

    @Override
    public DataObject call() throws EvaluationError {
        DataObject r;
        try {
            r = this.trap.apply();
        }
        catch (InternalScriptError e1) {
            if (e1 instanceof EvaluationError) {
                throw (EvaluationError)e1;
            }
            throw new EvaluationError(this, e1.getMessage());
        }
        try {
            return r.implicitCastTo(this.returnType.getType());
        }
        catch (ImplicitCastError e) {
            throw new EvaluationError(this, "Type Safety Error: Function " + this.functionName + " returned <" + r.getType() + "> but should return <" + this.returnType + ">");
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
    }
}

