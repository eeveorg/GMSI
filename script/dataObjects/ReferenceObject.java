/*
 * Decompiled with CFR 0_119.
 */
package script.dataObjects;

import script.EvaluationError;
import script.ImplicitCastError;
import script.InternalScriptError;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.VarHandle;

public class ReferenceObject
extends DataObject {
    protected final VarHandle pointTo;

    public ReferenceObject(VarHandle p) throws EvaluationError, InternalScriptError {
        this.pointTo = p;
        this.type = DataType.getReferenceType(this.pointTo.getValue().getType());
    }

    private ReferenceObject(DataType t, VarHandle p) throws InternalScriptError {
        this.pointTo = p;
        this.type = DataType.getReferenceType(t);
    }

    @Override
    public DataObject implicitCastTo(DataType d) throws InternalScriptError {
        if (d.getBasicType() == DataType.types.VAR) {
            return this;
        }
        if (this.getType().isDerivedFrom(d)) {
            return this;
        }
        DataType tis = this.type;
        DataType other = d;
        int numDerefs = 0;
        if (d.getBasicType() == DataType.types.PTR) {
            block2 : do {
                tis = tis.getReferencedType();
                other = other.getReferencedType();
                ++numDerefs;
                if (other.getBasicType() == DataType.types.VOID) {
                    return new ReferenceObject(d.getReferencedType(), this.pointTo);
                }
                if (tis.getBasicType() != DataType.types.VOID) continue;
                VarHandle ptr = this.pointTo;
                try {
                    DataObject o = ptr.getValue();
                    int i = 0;
                    while (i < numDerefs - 1) {
                        if (o.getType().getBasicType() != DataType.types.PTR) break block2;
                        o = ptr.getValue();
                        ptr = ((ReferenceObject)o).pointTo;
                        ++i;
                    }
                    if (!o.getType().equals(other)) {
                        throw new ImplicitCastError("Implicit cast error: Cannot cast from " + this.type + " to " + d + " because the void pointer did not point to a variable of type " + other + " but to a variable of type " + o.getType());
                    }
                }
                catch (EvaluationError e) {
                    throw new InternalScriptError(e.getMessage());
                }
                return new ReferenceObject(d.getReferencedType(), this.pointTo);
            } while (tis.getBasicType() == DataType.types.PTR && other.getBasicType() == DataType.types.PTR);
        }
        throw new ImplicitCastError("Implicit cast error: Cannot cast implicitly from " + this.type + " to " + d);
    }

    public VarHandle getReference() {
        return this.pointTo;
    }

    public DataObject derefer() throws EvaluationError, InternalScriptError {
        DataType t = this.pointTo.getValue().getType();
        return t.getNewInstance(this.pointTo.getValue());
    }

    public String toString() {
        block4 : {
            if (this.pointTo != null) break block4;
            return "Pointer->(null)";
        }
        try {
            return "Pointer->(" + this.pointTo.getValue() + ")";
        }
        catch (EvaluationError e) {
            return "Pointer->(null)";
        }
        catch (InternalScriptError e) {
            return "Pointer->(null)";
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof ReferenceObject)) {
            return false;
        }
        ReferenceObject s = (ReferenceObject)o;
        return s.pointTo.equals(this.pointTo);
    }

    @Override
    public boolean compare(DataObject o, int operator) throws InternalScriptError {
        if (!(o instanceof ReferenceObject)) {
            throw new InternalScriptError("Cannot compare " + this.getType() + " with " + o.getType());
        }
        switch (operator) {
            case 0: {
                return o.equals(this);
            }
            case 1: {
                return !o.equals(this);
            }
        }
        throw new InternalScriptError("Evaluation Error: Cannot compare pointers with " + operator);
    }
}

