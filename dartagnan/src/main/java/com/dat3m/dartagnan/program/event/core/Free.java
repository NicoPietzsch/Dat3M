package com.dat3m.dartagnan.program.event.core;

import com.dat3m.dartagnan.expression.ExprInterface;
import com.dat3m.dartagnan.expression.IExpr;

public class Free extends Store{

    public Free(IExpr address, ExprInterface value, String mo) {
        super(address, value, mo);
    }
    
    protected Free(Free other){
        super(other);
        this.value = other.value;
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
