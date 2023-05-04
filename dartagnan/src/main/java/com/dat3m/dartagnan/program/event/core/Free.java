package com.dat3m.dartagnan.program.event.core;

import com.dat3m.dartagnan.expression.IExpr;
import com.dat3m.dartagnan.expression.IValue;

public class Free extends Store{

    public Free(IExpr address) {
        super(address, IValue.ZERO, "");
    }
    
    protected Free(Free other){
        super(other);
    }

    @Override
    public String toString() {
        return "free(*)" + address + ")";
    }

    @Override
    public Free getCopy() {
        return new Free(this);
    }
}
