/*
 * Decompiled with CFR 0_119.
 */
package script.names;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRfinalTokens.FunctionDefinition;
import script.LRfinalTokens.StructDefinition;
import script.LRhighTokens.DummyTypeDefinition;
import script.LRhighTokens.TypeDefinition;
import script.LRhighTokens.TypeName;
import script.LRhighTokens.VariableDef;
import script.LRterminals.ExternalIdentifyer;
import script.LRterminals.Identifyer;
import script.ParseError;
import script.SourceObject;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.dataObjects.DataType;
import script.names.LocalNameStack;
import script.names.StandardVarSpace;
import script.names.VarHandle;
import script.names.VarSpace;

public class NameResolver {
    private LocalNameStack localNameStack = new LocalNameStack();
    private HashMap<String, HashMap<String, FunctionDefinition>> functionList = null;
    private HashMap<String, TypeDefinition> typeDefs = null;
    private LinkedList<ContainerObject> namespaces = null;
    private StandardVarSpace globalNames = null;
    private StandardVarSpace externalNames = new StandardVarSpace();
    private boolean createArrayIndex = false;

    public NameResolver() {
        this.reset();
    }

    public void reset() {
        this.functionList = new HashMap();
        this.globalNames = new StandardVarSpace();
        this.localNameStack = new LocalNameStack();
        this.typeDefs = new HashMap();
        this.namespaces = new LinkedList();
    }

    public void addExternalVar(String name, DataObject content) {
        try {
            this.externalNames.put("@" + name, content);
        }
        catch (InternalScriptError e) {
            throw new Error(e);
        }
    }

    public void addGlobal(String name, DataObject content) throws InternalScriptError {
        if (this.globalNames.hasName(name)) {
            throw new InternalScriptError("Warning: Double definition of the global variable " + name);
        }
        this.globalNames.put(name, content);
    }

    public void addLocal(int num, DataObject content, DataType type) throws InternalScriptError {
        if (num == -1) {
            throw new InternalScriptError("Trying to add a local with no index!");
        }
        this.localNameStack.addLocal(num, content, type);
    }

    public void pushNamespace(ContainerObject o) {
        this.namespaces.addFirst(o);
    }

    public void popNamespace() {
        this.namespaces.poll();
    }

    public ContainerObject peekNamespace() {
        return this.namespaces.peek();
    }

    public void popBig() {
        this.localNameStack.popBig();
    }

    public void pushLocalNew(int size) {
        this.localNameStack.pushSpaceNew(size);
    }

    public TypeDefinition getUserDef(String name) {
        return this.typeDefs.get(name);
    }

    public HashSet<String> getUserDefTypes() {
        HashSet<String> result = new HashSet<String>();
        result.addAll((Collection)this.typeDefs.keySet());
        return result;
    }

    public VarHandle resolveLocal(int num) {
        return this.localNameStack.getHandle(num);
    }

    public VarHandle resolveName(String name) throws InternalScriptError {
        if (this.globalNames.hasName(name)) {
            return new VarHandle(this.globalNames, name);
        }
        for (ContainerObject o : this.namespaces) {
            if (!o.getVarSpace().hasName(name)) continue;
            return new VarHandle(o.getVarSpace(), name);
        }
        throw new InternalScriptError("Cannot resolve the name \"" + name + "\"");
    }

    public String getSignature(LinkedList<DataObject> params) throws EvaluationError, InternalScriptError {
        String output = "(";
        for (DataObject r : params) {
            if (!r.getType().isValidAsParameter()) {
                throw new InternalScriptError(String.valueOf(String.valueOf(r)) + " is no valid parameter, type is " + r.getType());
            }
            output = String.valueOf(output) + r.getContentDataType().toString();
            if (params.getLast() == r) continue;
            output = String.valueOf(output) + ",";
        }
        output = String.valueOf(output) + ")";
        return output;
    }

    public FunctionDefinition resolveFunction(String name, LinkedList<DataObject> params) throws EvaluationError, InternalScriptError {
        if (!this.functionList.containsKey(name)) {
            throw new InternalScriptError("Cannot resolve function call name \"" + name + "\": No function with that name exists. Maybe you forgot including the file where the function is defined?");
        }
        HashMap<String, FunctionDefinition> f = this.functionList.get(name);
        if (!f.containsKey(this.getSignature(params))) {
            String fail;
            LinkedList<Iterator<String>> possibleFuncs = new LinkedList<Iterator<String>>();
            for (String ff : f.keySet()) {
                Iterator<String> fd = f.get(ff);
                if (fd.parameters.size() != params.size()) continue;
                ListIterator<VariableDef> iter1 = fd.parameters.listIterator();
                ListIterator<DataObject> iter2 = params.listIterator();
                boolean possible = true;
                while (iter2.hasNext() && iter1.hasNext()) {
                    DataType defType = iter1.next().type.getType();
                    DataObject r = iter2.next();
                    try {
                        r.implicitCastTo(defType);
                        continue;
                    }
                    catch (InternalScriptError e) {
                        possible = false;
                        break;
                    }
                }
                if (!possible) continue;
                possibleFuncs.add(fd);
            }
            if (possibleFuncs.size() == 1) {
                return (FunctionDefinition)possibleFuncs.getFirst();
            }
            if (possibleFuncs.size() > 1) {
                fail = "";
                for (FunctionDefinition s : possibleFuncs) {
                    try {
                        fail = String.valueOf(fail) + "\n" + s.getSignature();
                        continue;
                    }
                    catch (ParseError e) {
                        e.printStackTrace();
                    }
                }
                throw new InternalScriptError("Cannot resolve function call name \"" + name + "\": Function name exists, but not with the exact signature: " + this.getSignature(params) + "\nInstead, more functions exist onto which the given parameters could be cast to. Specify one of them by casting explicitly!" + "\nFollowing matching signatures exist:" + fail);
            }
            fail = "";
            for (String s : this.functionList.get(name).keySet()) {
                fail = String.valueOf(fail) + "\n" + s;
            }
            throw new InternalScriptError("Cannot resolve function call name \"" + name + "\": Function name exists, but not with this signature: " + this.getSignature(params) + "\nFollowing signatures exist:" + fail);
        }
        return f.get(this.getSignature(params));
    }

    public void addFunction(FunctionDefinition f) throws ParseError {
        HashMap<String, FunctionDefinition> h;
        if (!this.functionList.containsKey(f.functionName.toString())) {
            this.functionList.put(f.functionName.toString(), new HashMap());
        }
        if ((h = this.functionList.get(f.functionName.toString())).containsKey(f.getSignature())) {
            throw new ParseError("Duplicate function definition: " + f.functionName + f.getSignature());
        }
        h.put(f.getSignature(), f);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void addTypeDefinition(String name, TypeDefinition d) throws ParseError {
        if (this.typeDefs.containsKey(name)) {
            if (d instanceof DummyTypeDefinition) {
                return;
            }
            if (!(this.typeDefs.get(name) instanceof DummyTypeDefinition)) throw new ParseError("Duplicate type definition for type \"" + name + "\"\nFirst definition: " + this.typeDefs.get(name).getSource() + " line " + this.typeDefs.get(name).getLine() + "\"\nRedefinition: " + d.getSource() + " line " + d.getLine());
            this.typeDefs.put(name, d);
            return;
        } else {
            this.typeDefs.put(name, d);
        }
    }

    public DataType stringToTypeName(String input) throws InternalScriptError {
        DataType result = null;
        if (this.typeDefs.containsKey(input)) {
            result = new DataType(this.typeDefs.get(input));
        } else if ((input = input.toLowerCase()).equalsIgnoreCase("string")) {
            result = new DataType(DataType.types.STRING);
        } else if (input.equalsIgnoreCase("array")) {
            result = new DataType(DataType.types.ARRAY);
        } else if (input.equalsIgnoreCase("bool")) {
            result = new DataType(DataType.types.BOOL);
        } else if (input.equalsIgnoreCase("int")) {
            result = new DataType(DataType.types.INT);
        } else if (input.equalsIgnoreCase("float")) {
            result = new DataType(DataType.types.FLOAT);
        } else if (input.equalsIgnoreCase("void")) {
            result = new DataType(DataType.types.VOID);
        } else if (input.equalsIgnoreCase("object")) {
            result = new DataType(DataType.types.OBJECT);
        } else if (input.equalsIgnoreCase("var")) {
            result = new DataType(DataType.types.VAR);
        } else if (input.equalsIgnoreCase("struct")) {
            result = new DataType(StructDefinition.getMotherStruct());
        } else {
            if (input.equalsIgnoreCase("container")) {
                throw new Error("Type \"container\" is not supported yet!");
            }
            throw new InternalScriptError("strToType error: The datatype with the name \"" + input + "\" doesn't exist");
        }
        return result;
    }

    public boolean wantCreateArrayIndex() {
        return this.createArrayIndex;
    }

    public void setCreateArrayIndex(boolean createArrayIndex) {
        this.createArrayIndex = createArrayIndex;
    }

    public VarHandle resolveExternalName(ExternalIdentifyer identifyer) throws InternalScriptError {
        if (!this.externalNames.hasName(identifyer.getCode())) {
            throw new InternalScriptError("Cannot resolve the external name \"" + identifyer.getCode() + "\"");
        }
        return new VarHandle(this.externalNames, identifyer.getCode());
    }
}

