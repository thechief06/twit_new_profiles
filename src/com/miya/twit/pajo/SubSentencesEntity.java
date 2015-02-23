/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.pajo;

import java.util.List;

/**
 *
 * @author SBT
 */
public class SubSentencesEntity {

    private String subSentence;
    private int sentencePolarity;
    private List<TwoWordsEntity> twoWordEntity;

    public String getSubSentence() {
        return subSentence;
    }

    public void setSubSentence(String subSentence) {
        this.subSentence = subSentence;
    }

    public int getSentencePolarity() {
        return sentencePolarity;
    }

    public void setSentencePolarity(int sentencePolarity) {
        this.sentencePolarity = sentencePolarity;
    }

    public List<TwoWordsEntity> getTwoWordEntity() {
        return twoWordEntity;
    }

    public void setTwoWordEntity(List<TwoWordsEntity> twoWordEntity) {
        this.twoWordEntity = twoWordEntity;
    }


}
