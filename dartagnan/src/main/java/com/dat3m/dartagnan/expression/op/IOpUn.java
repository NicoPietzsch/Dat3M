package com.dat3m.dartagnan.expression.op;

import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

import static com.dat3m.dartagnan.GlobalSettings.*;
import java.math.BigInteger;

public enum IOpUn {
    MINUS,
    BV2UINT, BV2INT,
    INT2BV1, INT2BV8, INT2BV16, INT2BV32, INT2BV64,
    TRUNC6432, TRUNC6416, TRUNC648, TRUNC641, TRUNC3216, TRUNC328, TRUNC321, TRUNC168, TRUNC161, TRUNC81,
    ZEXT18, ZEXT116, ZEXT132, ZEXT164, ZEXT816, ZEXT832, ZEXT864, ZEXT1632, ZEXT1664, ZEXT3264,
    SEXT18, SEXT116, SEXT132, SEXT164, SEXT816, SEXT832, SEXT864, SEXT1632, SEXT1664, SEXT3264,
    CTLZ;

    @Override
    public String toString() {
        switch (this) {
            case MINUS:
                return "-";
            case CTLZ:
                return "ctlz ";
            default:
                return "";
        }
    }

    public Formula encode(Formula e, FormulaManager m) {
        if (e instanceof IntegerFormula) {
            IntegerFormulaManager imgr = m.getIntegerFormulaManager();
            IntegerFormula i = (IntegerFormula) e;
            switch (this) {
                case MINUS:
                    return imgr.subtract(imgr.makeNumber(BigInteger.ZERO), i);
                case BV2UINT:
                case BV2INT:
                    return e;
                // ============ INT2BV ============
                case INT2BV1:
                    return m.getBitvectorFormulaManager().makeBitvector(1, i);
                case INT2BV8:
                    return m.getBitvectorFormulaManager().makeBitvector(8, i);
                case INT2BV16:
                    return m.getBitvectorFormulaManager().makeBitvector(16, i);
                case INT2BV32:
                    return m.getBitvectorFormulaManager().makeBitvector(32, i);
                case INT2BV64:
                    return m.getBitvectorFormulaManager().makeBitvector(64, i);
                // ============ TRUNC ============
                case TRUNC6432:
                case TRUNC6416:
                case TRUNC3216:
                case TRUNC648:
                case TRUNC328:
                case TRUNC168:
                case TRUNC641:
                case TRUNC321:
                case TRUNC161:
                case TRUNC81:
                // ============ ZEXT ============
                case ZEXT18:
                case ZEXT116:
                case ZEXT132:
                case ZEXT164:
                case ZEXT816:
                case ZEXT832:
                case ZEXT864:
                case ZEXT1632:
                case ZEXT1664:
                case ZEXT3264:
                // ============ SEXT ============
                case SEXT18:
                case SEXT116:
                case SEXT132:
                case SEXT164:
                case SEXT816:
                case SEXT832:
                case SEXT864:
                case SEXT1632:
                case SEXT1664:
                case SEXT3264:
                    return e;
                default:
                    // TODO add support for CTLZ. Right now we assume constant propagation gor rid
                    // of such instructions
                    throw new UnsupportedOperationException("Encoding of IOpUn operation " + this + " not supported on integer formulas.");
            }
        } else {
            BitvectorFormulaManager bvmgr = m.getBitvectorFormulaManager();
            BitvectorFormula bv = (BitvectorFormula) e;
            switch (this) {
                case MINUS:
                    return bvmgr.negate(bv);
                case BV2UINT:
                    return getArchPrecision() > -1 ? bvmgr.extend(bv, getArchPrecision() - bvmgr.getLength(bv), false)
                            : bvmgr.toIntegerFormula(bv, false);
                case BV2INT:
                    return getArchPrecision() > -1 ? bvmgr.extend(bv, getArchPrecision() - bvmgr.getLength(bv), true)
                            : bvmgr.toIntegerFormula(bv, true);
                // ============ INT2BV ============
                case INT2BV1:
                case INT2BV8:
                case INT2BV16:
                case INT2BV32:
                case INT2BV64:
                    return e;
                // ============ TRUNC ============
                case TRUNC6432:
                    return bvmgr.extract(bv, 31, 0);
                case TRUNC6416:
                case TRUNC3216:
                    return bvmgr.extract(bv, 15, 0);
                case TRUNC648:
                case TRUNC328:
                case TRUNC168:
                    return bvmgr.extract(bv, 7, 0);
                case TRUNC641:
                case TRUNC321:
                case TRUNC161:
                case TRUNC81:
                    return bvmgr.extract(bv, 1, 0);
                // ============ ZEXT ============
                case ZEXT18:
                    return bvmgr.extend(bv, 7, false);
                case ZEXT116:
                    return bvmgr.extend(bv, 15, false);
                case ZEXT132:
                    return bvmgr.extend(bv, 31, false);
                case ZEXT164:
                    return bvmgr.extend(bv, 63, false);
                case ZEXT816:
                    return bvmgr.extend(bv, 8, false);
                case ZEXT832:
                    return bvmgr.extend(bv, 24, false);
                case ZEXT864:
                    return bvmgr.extend(bv, 56, false);
                case ZEXT1632:
                    return bvmgr.extend(bv, 16, false);
                case ZEXT1664:
                    return bvmgr.extend(bv, 48, false);
                case ZEXT3264:
                    return bvmgr.extend(bv, 32, false);
                // ============ SEXT ============
                case SEXT18:
                    return bvmgr.extend(bv, 7, true);
                case SEXT116:
                    return bvmgr.extend(bv, 15, true);
                case SEXT132:
                    return bvmgr.extend(bv, 31, true);
                case SEXT164:
                    return bvmgr.extend(bv, 63, true);
                case SEXT816:
                    return bvmgr.extend(bv, 8, true);
                case SEXT832:
                    return bvmgr.extend(bv, 24, true);
                case SEXT864:
                    return bvmgr.extend(bv, 56, true);
                case SEXT1632:
                    return bvmgr.extend(bv, 16, true);
                case SEXT1664:
                    return bvmgr.extend(bv, 48, true);
                case SEXT3264:
                    return bvmgr.extend(bv, 32, true);
                case CTLZ:
                    // enc = extract(bv, 63, 63) == 1 ? 0 : (extract(bv, 62, 62) == 1 ? 1 : extract ... extract(bv, 0, 0) ? 63 : 64)
                    int bvLength = bvmgr.getLength(bv);
                    BitvectorFormula bv1 = bvmgr.makeBitvector(1, 1);
                    BitvectorFormula enc = bvmgr.makeBitvector(bvLength, bvLength);
                    for(int i = bvmgr.getLength(bv) - 1; i >= 0; i--) {
                        BitvectorFormula bvi = bvmgr.makeBitvector(bvLength, i);
                        BitvectorFormula bvbit = bvmgr.extract(bv, bvLength - (i + 1), bvLength - (i + 1));
                        enc = m.getBooleanFormulaManager().ifThenElse(bvmgr.equal(bvbit, bv1), bvi, enc);
                    }
                    return enc;
                default:
                    throw new UnsupportedOperationException("Encoding of IOpUn operation " + this + " not supported on bitvector formulas.");
            }
        }
    }
}
