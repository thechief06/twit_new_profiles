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
public enum Polarity {

    VeryNegative,
    Negative,
    SomeWhatNegative,
    Neutral,
    SomeWhatPositive,
    Positive,
    VeryPositive;

    public static int getPolarityValue(Polarity pl) {
        switch (pl) {
            case VeryPositive:
                return 3;
            case Positive:
                return 2;
            case SomeWhatPositive:
                return 1;
            case Neutral:
                return 0;
            case SomeWhatNegative:
                return -1;
            case Negative:
                return -2;
            case VeryNegative:
                return 3;
            default:
                return 99;
        }
    }

    public static Polarity getPolarity(int plValue) {
        switch (plValue) {
            case 3:
                return VeryPositive;
            case 2:
                return Positive;
            case 1:
                return SomeWhatPositive;
            case -1:
                return SomeWhatNegative;
            case -2:
                return Negative;
            case -3:
                return VeryNegative;
            default:
                return Neutral;
        }
    }

    public static Polarity getReversePolarity(Polarity pl) {
        switch (pl) {
            case VeryPositive:
                return VeryNegative;
            case Positive:
                return Negative;
            case SomeWhatPositive:
                return SomeWhatNegative;
            case Neutral:
                return Neutral;
            case SomeWhatNegative:
                return SomeWhatPositive;
            case Negative:
                return Positive;
            case VeryNegative:
                return VeryPositive;
            default:
                return Neutral;
        }
    }

}
