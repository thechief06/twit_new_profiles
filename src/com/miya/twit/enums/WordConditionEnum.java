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
public enum WordConditionEnum {

    none,
    sız,
    ma;

    public static int getWordConditionEnumValue(WordConditionEnum wce) {
        switch (wce) {
            case none:
                return 0;
            case sız:
                return 1;
            case ma:
                return 2;
            default:
                return 99;
        }
    }

    public static WordConditionEnum getWordCondition(int wceValue) {
        switch (wceValue) {
            case 0:
                return none;
            case 1:
                return sız;
            case 2:
                return ma;
            default:
                return sız;
        }
    }
}
