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
public class TweetSentimentEntity {

    private String tweet;
    private int tweetPolarityValue;
    private List<SubSentencesEntity> subSentencesEntities;
    private List<String> topHashTag;
    private List<String> topMention;
    private List<String> topUrl;

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public int getTweetPolarityValue() {
        return tweetPolarityValue;
    }

    public void setTweetPolarityValue(int tweetPolarityValue) {
        this.tweetPolarityValue = tweetPolarityValue;
    }

    public List<SubSentencesEntity> getSubSentencesEntities() {
        return subSentencesEntities;
    }

    public void setSubSentencesEntities(List<SubSentencesEntity> subSentencesEntities) {
        this.subSentencesEntities = subSentencesEntities;
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

}
