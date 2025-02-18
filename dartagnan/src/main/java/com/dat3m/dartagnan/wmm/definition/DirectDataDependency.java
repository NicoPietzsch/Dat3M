package com.dat3m.dartagnan.wmm.definition;

import com.dat3m.dartagnan.encoding.EncodingContext;
import com.dat3m.dartagnan.wmm.Definition;
import com.dat3m.dartagnan.wmm.Relation;

public class DirectDataDependency extends Definition {

    public DirectDataDependency(Relation r0) {
        super(r0);
    }

    @Override
    public <T> T accept(Visitor<? extends T> v) {
        return v.visitInternalDataDependency(definedRelation);
    }

    @Override
    public EncodingContext.EdgeEncoder getEdgeVariableEncoder(EncodingContext c) {
        return tuple -> c.dependency(tuple.getFirst(), tuple.getSecond());
    }
}
