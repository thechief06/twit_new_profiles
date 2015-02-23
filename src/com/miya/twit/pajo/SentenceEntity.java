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
public class SentenceEntity {

    private List<String> sentenceList;
    private List<String> topHashTag;
    private List<String> topMention;
    private List<String> topUrl;
    private List<ParsedWords> listParsedWords;

    public List<String> getSentenceList() {
        return sentenceList;
    }

    public void setSentenceList(List<String> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public List<String> getTopHashTag() {
        return topHashTag;
    }

    public void setTopHashTag(List<String> topHashTag) {
        this.topHashTag = topHashTag;
    }

    public List<String> getTopMention() {
        return topMention;
    }

    public void setTopMention(List<String> topMention) {
        this.topMention = topMention;
    }

    public List<String> getTopUrl() {
        return topUrl;
    }

    public void setTopUrl(List<String> topUrl) {
        this.topUrl = topUrl;
    }

    public List<ParsedWords> getListParsedWords() {
        return listParsedWords;
    }

    public void setListParsedWords(List<ParsedWords> listParsedWords) {
        this.listParsedWords = listParsedWords;
    }
}
