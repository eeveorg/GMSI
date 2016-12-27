/*
 * Decompiled with CFR 0_119.
 */
package script.LRhighTokens;

import java.util.LinkedList;
import script.EvaluationError;
import script.InternalScriptError;
import script.LRhighTokens.TypeList;
import script.LRhighTokens.TypeName;
import script.LRterminals.CloseSharpBracket;
import script.LRterminals.OpenSharpBracket;
import script.ParseError;
import script.Token;
import script.dataObjects.DataType;

public class GenericTypeName
extends TypeName {
    private TypeList l;

    public GenericTypeName(TypeName r, OpenSharpBracket bo, TypeList l, CloseSharpBracket bc) throws EvaluationError {
        super(r, r + bo + l + bc);
        if (!r.getCode().equalsIgnoreCase("array")) {
            throw new EvaluationError(this, "Only array is allowed for generics at the moment!");
        }
        this.l = l;
    }

    @Override
    public DataType getType() throws InternalScriptError {
        LinkedList<DataType> content = new LinkedList<DataType>();
        for (TypeName n : this.l.getList()) {
            content.add(n.getType());
        }
        return new DataType(content);
    }

    @Override
    public Token unwrap() throws ParseError {
        this.l.unwrap();
        return this;
    }
}

