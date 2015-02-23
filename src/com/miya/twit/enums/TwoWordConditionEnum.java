/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.enums;

/**
 *
 * @author SBT
 */
public enum TwoWordConditionEnum {

    none,
    naNe,
    hemHem,
    degil,
    cokAz,
    cokAz_,
    ikileme,
    expression;

    public static int getTwoWordConditionEnumValue(TwoWordConditionEnum twce) {
        switch (twce) {
            case none:
                return 0;
            case naNe:
                return 1;
            case hemHem:
                return 2;
            case degil:
                return 3;
            case cokAz:
                return 4;
            case cokAz_:
                return 5;
            case ikileme:
                return 6;
            case expression:
                return 7;
            default:
                return 99;
        }
    }

    public static TwoWordConditionEnum getTwoWordConditionEnum(int twceValue) {
        switch (twceValue) {
            case 0:
                return none;
            case 1:
                return naNe;
            case 2:
                return hemHem;
            case 3:
                return degil;
            case 4:
                return cokAz;
            case 5:
                return cokAz_;
            case 6:
                return ikileme;
            case 7:
                return expression;
            default:
                return naNe;
        }
    }
}
