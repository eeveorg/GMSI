/*
 * Decompiled with CFR 0_119.
 */
package script.LRterminals;

import script.EvaluationError;
import script.LRhighTokens.Expression;
import script.ParseError;
import script.Script;
import script.Token;
import script.dataObjects.ContainerObject;
import script.dataObjects.DataObject;
import script.names.NameResolver;

public class WordThis
extends Expression {
    public WordThis(Token old) {
        super(old);
    }

    @Override
    public Token unwrap() throws ParseError {
        return this;
    }

    @Override
    public DataObject eval() throws EvaluationError {
        super.eval();
        ContainerObject o = this.owner.getNameResolver().peekNamespace();
        if (o == null) {
            throw new EvaluationError(this, "Cannot use this, as long as no namespace is opened");
        }
        return o;
    }
}

