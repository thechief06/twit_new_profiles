/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.pajo;

import com.miya.twit.enums.TwoWordConditionEnum;
import java.util.List;

/**
 *
 * @author SBT
 */
public class TwoWordsEntity {

    private String twoWord;
    private int twoWordPolarity;
    private TwoWordConditionEnum twoWordCondition;
    private List<ParsedWords> passedWord;

    public String getTwoWord() {
        return twoWord;
    }

    public void setTwoWord(String twoWord) {
        this.twoWord = twoWord;
    }

    public int getTwoWordPolarity() {
        return twoWordPolarity;
    }

    public void setTwoWordPolarity(int twoWordPolarity) {
        this.twoWordPolarity = twoWordPolarity;
    }

    public List<ParsedWords> getPassedWord() {
        return passedWord;
    }

    public void setPassedWord(List<ParsedWords> passedWord) {
        this.passedWord = passedWord;
    }

    public TwoWordConditionEnum getTwoWordCondition() {
        return twoWordCondition;
    }

    public void setTwoWordCondition(TwoWordConditionEnum twoWordCondition) {
        this.twoWordCondition = twoWordCondition;
    }
}
