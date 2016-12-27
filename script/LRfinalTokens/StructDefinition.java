/*
 * Decompiled with CFR 0_119.
 */
package script.LRfinalTokens;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.AssignDefStatement;
import script.LRfinalTokens.ConstructorExpression;
import script.LRfinalTokens.DefStatement;
import script.LRfinalTokens.ScriptBracketStatement;
import script.LRhighTokens.Expression;
import script.LRhighTokens.SimpleTypeList;
import script.LRhighTokens.Statement;
import script.LRhighTokens.TypeDefinition;
import script.LRhighTokens.TypeName;
import script.LRterminals.Identifyer;
import script.LRterminals.SimpleTypeName;
import script.LRterminals.WordExtends;
import script.LRterminals.WordTypedef;
import script.ParseError;
import script.Script;
import script.SourceObject;
import script.Token;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.dataObjects.NullObject;
import script.dataObjects.StructObject;
import script.dataObjects.VoidObject;
import script.names.LocalNameMapper;
import script.names.NameResolver;

public class StructDefinition
extends Statement
implements TypeDefinition {
    private static StructDefinition motherOfAllStructs = new StructDefinition();
    public LinkedList<Statement> children = null;
    private LinkedList<DataType> derivedFrom = null;
    private HashMap<String, DataType> typeMap = null;
    private String name = null;
    private SimpleTypeList extendedFrom = null;

    public HashMap<String, DataType> getTypeMap() throws InternalScriptError {
        if (this.typeMap == null) {
            this.typeMap = this.assembleTypeMap();
        }
        return this.typeMap;
    }

    private void checkStruct(SimpleTypeName struct) throws ParseError {
        if (!struct.getCode().equalsIgnoreCase("struct")) {
            throw new ParseError("wrong use of a struct definition typedef. The word after the type name must be struct!");
        }
    }

    public StructDefinition(WordTypedef r, SimpleTypeName t, SimpleTypeName struct, ScriptBracketStatement s) throws ParseError {
        super(r, r + struct + " " + t + s);
        this.checkStruct(struct);
        this.name = t.toString();
        this.children = s.getList();
        this.derivedFrom = new LinkedList();
        this.derivedFrom.add(new DataType(motherOfAllStructs));
        this.validateChildren();
        this.owner.getNameResolver().addTypeDefinition(this.name, this);
    }

    public StructDefinition(WordTypedef r, SimpleTypeName t, SimpleTypeName struct, WordExtends w, SimpleTypeList extendedFrom, ScriptBracketStatement ss) throws ParseError {
        super(r, r + struct + " " + t + ss);
        this.checkStruct(struct);
        this.name = t.toString();
        this.children = ss.getList();
        this.extendedFrom = extendedFrom;
        this.validateChildren();
        this.owner.getNameResolver().addTypeDefinition(this.name, this);
    }

    private StructDefinition() {
        super("Struct");
        this.name = "Struct";
        this.children = new LinkedList();
        this.derivedFrom = new LinkedList();
    }

    public static StructDefinition getMotherStruct() {
        return motherOfAllStructs;
    }

    private void validateChildren() throws ParseError {
        LocalNameMapper.pushMapper();
        HashMap<String, Statement> h = new HashMap<String, Statement>();
        String name = "";
        for (Statement s : this.children) {
            if ((s = (Statement)s.unwrap()) instanceof AssignDefStatement) {
                name = ((AssignDefStatement)s).name.getCode();
                Expression rValue = ((AssignDefStatement)s).rValue;
                if (rValue.unwrap() instanceof ConstructorExpression && ((ConstructorExpression)rValue.unwrap()).typeName.equals(this.name)) {
                    throw new ParseError("Malformed struct definition! Struct <" + this.name + "> contains its own constructor, this will lead to endless instanciations!");
                }
            } else if (s instanceof DefStatement) {
                name = ((DefStatement)s).name.getCode();
            } else {
                throw new ParseError("Malformed struct definition! Struct <" + this.name + "> contains the statement \"" + s.getCode() + "\" which is not a valid declaration or assignment declaration");
            }
            if (h.containsKey(name)) {
                throw new ParseError("Malformed struct definition! Struct <" + this.name + "> contains a duplicate declaration of member <" + name + ">:\n" + ((Statement)h.get(name)).getSourceObject() + ", line " + ((Statement)h.get(name)).getLine() + "\t--> " + h.get(name) + "\n" + s.getSourceObject() + ", line " + s.getLine() + "\t--> " + s);
            }
            h.put(name, s);
        }
        LocalNameMapper.popMapper();
    }

    private HashMap<String, DataType> assembleTypeMap() throws InternalScriptError {
        HashMap<String, DataType> result = new HashMap<String, DataType>();
        for (Statement s : this.children) {
            if (s instanceof AssignDefStatement) {
                AssignDefStatement a = (AssignDefStatement)s;
                result.put(a.name.toString(), a.type.getType());
                continue;
            }
            if (s instanceof DefStatement) {
                DefStatement a = (DefStatement)s;
                result.put(a.name.toString(), a.type.getType());
                continue;
            }
            throw new Error("WTF!" + s.getClass().getSimpleName());
        }
        return result;
    }

    public StructDefinition(Script owner, String name) {
        super(owner, name);
    }

    @Override
    public Token unwrap() throws ParseError {
        ListIterator<Statement> iter = this.children.listIterator();
        LocalNameMapper.pushMapper();
        while (iter.hasNext()) {
            iter.set((Statement)iter.next().unwrap());
        }
        LocalNameMapper.popMapper();
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        if (this.extendedFrom != null) {
            try {
                this.derivedFrom = new LinkedList();
                for (SimpleTypeName currentType : this.extendedFrom.getList()) {
                    TypeDefinition s;
                    block11 : {
                        s = this.owner.getNameResolver().getUserDef(currentType.getCode());
                        if (s == null) {
                            throw new EvaluationError(this, "Extends Error: The struct <" + this.name + "> is derived from <" + currentType.getCode() + "> which is not a valid struct type!");
                        }
                        if (s instanceof StructDefinition) {
                            this.children.addAll(((StructDefinition)s).children);
                        } else {
                            DataType d = s.getParentTypes().get(0);
                            do {
                                if (d.getDefinition() instanceof StructDefinition) {
                                    this.children.addAll(((StructDefinition)d.getDefinition()).children);
                                    break block11;
                                }
                                if (d.getDefinition().getParentTypes().get(0) == null) break;
                                d = d.getDefinition().getParentTypes().get(0);
                            } while (true);
                            throw new EvaluationError(this, "Extends Error: The struct <" + this.name + "> is derived from <" + currentType.getCode() + "> which is not a valid struct type!");
                        }
                    }
                    this.derivedFrom.add(new DataType(s));
                }
            }
            catch (InternalScriptError e1) {
                throw new EvaluationError(this, e1.getMessage());
            }
            try {
                this.validateChildren();
            }
            catch (ParseError e) {
                throw new EvaluationError(this, e.getMessage());
            }
        }
        return voidResult;
    }

    @Override
    public DataObject getDefaultInstance() {
        return new NullObject(new DataType(this));
    }

    @Override
    public DataObject getNewInstance() throws InternalScriptError {
        return new StructObject(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDerivedFrom(DataType d) throws InternalScriptError {
        for (DataType cur : this.derivedFrom) {
            if (cur.equals(d)) {
                return true;
            }
            if (!cur.isDerivedFrom(d)) continue;
            return true;
        }
        return false;
    }

    @Override
    public List<DataType> getParentTypes() {
        return this.derivedFrom;
    }

    @Override
    public SourceObject getSource() {
        return this.source;
    }
}

