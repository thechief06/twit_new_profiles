/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.pajo;

import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.WordConditionEnum;
import com.miya.twit.enums.WordType;
import java.util.List;

/**
 *
 * @author SBT
 */
public class ParsedWords {

    private String word;
    private WordType wordType;
    private Polarity polarType;
    private String polarWord;
    private List<String> suffixList;
    private WordConditionEnum wordCondition;


    public WordType getWordType() {
        return wordType;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }

    public Polarity getPolarType() {
        return polarType;
    }

    public void setPolarType(Polarity polarType) {
        this.polarType = polarType;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPolarWord() {
        return polarWord;
    }

    public void setPolarWord(String polarWord) {
        this.polarWord = polarWord;
    }

    public List<String> getSuffixList() {
        return suffixList;
    }

    public void setSuffixList(List<String> suffixList) {
        this.suffixList = suffixList;
    }

    public WordConditionEnum getWordCondition() {
        return wordCondition;
    }

    public void setWordCondition(WordConditionEnum wordCondition) {
        this.wordCondition = wordCondition;
    }

}
