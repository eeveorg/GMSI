/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.LinkedList;
import java.util.List;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.SimpleTypeList;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeDefinition;
import script.LRhighTokens.TypeName;
import script.LRterminals.Semicolon;
import script.LRterminals.SimpleTypeName;
import script.LRterminals.WordExtends;
import script.LRterminals.WordExtern;
import script.LRterminals.WordTypedef;
import script.ParseError;
import script.Script;
import script.SourceObject;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.VoidObject;
import script.names.NameResolver;

public class TypedefStatement
extends Statement
implements TypeDefinition {
    protected String name = null;
    protected TypeName derived = null;
    protected SimpleTypeName type;

    public TypedefStatement(WordTypedef r, SimpleTypeName t, WordExtends w, TypeName extendedFrom, Semicolon s) throws ParseError {
        super(r, r + " " + t + " " + w + " " + extendedFrom + s);
        this.derived = extendedFrom;
        this.name = t.toString();
        this.type = t;
        this.owner.getNameResolver().addTypeDefinition(t.getCode(), this);
    }

    public TypedefStatement(WordTypedef r, SimpleTypeList t, WordExtern w, Semicolon s) throws ParseError {
        super(r, r + " " + t + " " + w + " " + s);
    }

    public TypedefStatement(Token t, String code) {
        super(t, code);
    }

    protected TypedefStatement(String s) {
        super(s);
    }

    @Override
    public Token unwrap() throws ParseError {
        if (this.derived != null) {
            this.derived = (TypeName)this.derived.unwrap();
        }
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        return voidResult;
    }

    @Override
    public DataObject getDefaultInstance() throws InternalScriptError {
        DataType t = this.derived.getType();
        DataObject o = t.getDefaultInstance();
        o.setType(t);
        return o;
    }

    @Override
    public DataObject getNewInstance() throws InternalScriptError {
        DataType t = this.derived.getType();
        DataObject o = t.getNewInstance();
        o.setType(this.type.getType());
        return o;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDerivedFrom(DataType d) throws InternalScriptError {
        DataType t = this.derived.getType();
        if (t.equals(d)) {
            return true;
        }
        return t.isDerivedFrom(d);
    }

    @Override
    public List<DataType> getParentTypes() throws InternalScriptError {
        LinkedList<DataType> result = new LinkedList<DataType>();
        result.add(this.derived.getType());
        return result;
    }

    @Override
    public SourceObject getSource() {
        return this.source;
    }
}

