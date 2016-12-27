/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.Set;
import script.EvaluationError;
import script.EvaluationResult;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.LRhighTokens.Expression;
import script.LRhighTokens.Name;
import script.LRhighTokens.Statement;
import script.LRhighTokens.VariableDef;
import script.LRterminals.Arrow;
import script.LRterminals.CloseBracket;
import script.LRterminals.Colon;
import script.LRterminals.Identifyer;
import script.LRterminals.OpenBracket;
import script.LRterminals.WordFor;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.ArrayObject;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.IntObject;
import script.dataObjects.NullObject;
import script.dataObjects.StringObject;
import script.dataObjects.StructObject;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;
import script.names.NameResolver;
import script.names.VarHandle;

public class ForEachStatement
extends Statement {
    Expression iterateOver;
    Name iterator;
    Name iteratorResult;
    VariableDef defIterator;
    VariableDef defIterResult;
    Statement thenStatement;
    EvaluationResult skipListener;

    public ForEachStatement(WordFor w, OpenBracket bb, Name n, Colon c, Expression e, CloseBracket b, Statement s) {
        super(w, w + bb + n + c + e + b + s);
        this.iterator = n;
        this.iterateOver = e;
        this.defIterator = null;
        this.defIterResult = null;
        this.thenStatement = s;
        this.skipListener = this.owner.getEvalResult();
    }

    public ForEachStatement(WordFor w, OpenBracket bb, VariableDef d, Colon c, Expression e, CloseBracket b, Statement s) {
        super(w, w + bb + d + c + e + b + s);
        this.iterator = new Name(d.name);
        this.iterateOver = e;
        this.defIterator = d;
        this.thenStatement = s;
        this.defIterResult = null;
        this.skipListener = this.owner.getEvalResult();
    }

    public ForEachStatement(WordFor w, OpenBracket bb, VariableDef d, Arrow a, VariableDef d2, Colon c, Expression e, CloseBracket b, Statement s) {
        super(w, w + bb + d + c + e + b + s);
        this.iterator = new Name(d.name);
        this.iteratorResult = new Name(d2.name);
        this.iterateOver = e;
        this.defIterator = d;
        this.defIterResult = d2;
        this.thenStatement = s;
        this.skipListener = this.owner.getEvalResult();
    }

    @Override
    public Token unwrap() throws ParseError {
        LocalNameMapper.getMapper().pushSpace();
        this.iterateOver = (Expression)this.iterateOver.unwrap();
        this.iterator = (Name)this.iterator.unwrap();
        if (this.defIterator != null) {
            this.defIterator.unwrap();
            this.defIterator.name.localIndex = LocalNameMapper.getMapper().registerLocal(this.defIterator.name.toString());
        }
        if (this.defIterResult != null) {
            this.defIterResult.unwrap();
            this.defIterResult.name.localIndex = LocalNameMapper.getMapper().registerLocal(this.defIterResult.name.toString());
            this.iteratorResult = (Name)this.iteratorResult.unwrap();
        }
        this.thenStatement = (Statement)this.thenStatement.unwrap();
        LocalNameMapper.getMapper().popSpace();
        return this;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        iterOverResult = this.iterateOver.eval();
        iterResult = null;
        if (this.defIterator != null) {
            try {
                this.owner.getNameResolver().addLocal(this.defIterator.name.localIndex, this.defIterator.getType().getDefaultInstance(), this.defIterator.getType());
            }
            catch (InternalScriptError e) {
                throw new EvaluationError(this, e.getMessage());
            }
            if (this.defIterResult != null) {
                try {
                    this.owner.getNameResolver().addLocal(this.defIterResult.name.localIndex, this.defIterResult.getType().getDefaultInstance(), this.defIterResult.getType());
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this, e.getMessage());
                }
                iterResult = this.iteratorResult.resolve();
            }
        }
        iter = this.iterator.resolve();
        try {
            val = iter.getValue();
        }
        catch (InternalScriptError e) {
            throw new EvaluationError(this, e.getMessage());
        }
        iterClass = String.class;
        iterContainerResult = null;
        if (iterOverResult instanceof StringObject) {
            if (!(val instanceof StringObject)) {
                throw new EvaluationError(this.iterator, "The iterator in a for each loop must be a string when iterating over a comma separated string, but its type is " + val.getType());
            }
            if (this.defIterResult != null) {
                throw new EvaluationError(this.iterator, "Cannot use an extended for each loop to loop over a comma seperated list!");
            }
            listStr = ((StringObject)iterOverResult).getValue();
            l = listStr.split(",");
            if (l.length == 1 && l[0].equals("")) {
                l = new Object[]{};
            }
        } else if (iterOverResult instanceof ArrayObject) {
            iterContainerResult = (ContainerObject)iterOverResult;
            if (val instanceof StringObject) {
                l = ((ArrayObject)iterOverResult).getStrKeys().toArray();
            } else {
                if (val instanceof IntObject == false) throw new EvaluationError(this.iterator, "The iterator in a for each loop must be a string or an int when iterating over an array, but its type is " + val.getType());
                l = ((ArrayObject)iterOverResult).getIntKeys().toArray();
                iterClass = Integer.class;
            }
        } else if (iterOverResult instanceof StructObject) {
            iterContainerResult = (ContainerObject)iterOverResult;
            if (!(val instanceof StringObject)) {
                throw new EvaluationError(this.iterator, "The iterator in a for each loop must be a string when iterating over a struct, but its type is " + val.getType());
            }
            l = ((StructObject)iterOverResult).getKeys().toArray();
        } else {
            if (iterOverResult instanceof NullObject == false) throw new EvaluationError(this.iterateOver, "The variable over which a foreach loop can iterate must be either an array, a struct or a comma separated string, but it is a " + iterOverResult.getType());
            l = new Object[]{};
        }
        var11_14 = l;
        var10_15 = var11_14.length;
        var9_16 = 0;
        while (var9_16 < var10_15) {
            o = var11_14[var9_16];
            if (iterClass == String.class) {
                try {
                    iter.setValue(new StringObject((String)o));
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this.iterator, e.getMessage());
                }
                try {
                    if (iterResult == null) ** GOTO lbl84
                    iterResult.setValue(iterContainerResult.getEntry((String)o).getValue());
                }
                catch (ImplicitCastError e) {
                    throw new EvaluationError(this.defIterResult, "An extended for-each iterator couldn't be filled because the types did not match:\n" + e.getMessage());
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this.iterator, e.getMessage());
                }
            } else {
                try {
                    iter.setValue(new IntObject((Integer)o));
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this.iterator, e.getMessage());
                }
                try {
                    if (iterResult != null) {
                        iterResult.setValue(((ArrayObject)iterContainerResult).getEntry((Integer)o).getValue());
                    }
                }
                catch (ImplicitCastError e) {
                    throw new EvaluationError(this.defIterResult, "An extended for-each iterator couldn't be filled because the types did not match:\n" + e.getMessage());
                }
                catch (InternalScriptError e) {
                    throw new EvaluationError(this.iterator, e.getMessage());
                }
            }
lbl84: // 3 sources:
            this.thenStatement.eval();
            if (this.skipListener.wantBreak()) {
                this.skipListener.clearFlags();
                return ForEachStatement.voidResult;
            }
            if (this.skipListener.wantReturn()) {
                return ForEachStatement.voidResult;
            }
            if (this.skipListener.wantContinue()) {
                this.skipListener.clearFlags();
            }
            ++var9_16;
        }
        return ForEachStatement.voidResult;
    }
}

