package com.miya.twit.keywrd;

import com.miya.twit.pajo.TweetSentimentEntity;
import java.util.Date;

/**
 *
 * @author SBT
 */
public class TwitProfileWrapper {

    private long userId;
    private String username;
    private String screenName;
    private String text;
    private String userDescription;
    private String userLocation;
    private String userTimeZone;
    private String userLanguage;
    private String userProfileImageUrl;
    private Date textCreatedDate;
    private String profileLang;
    private TweetSentimentEntity tweetSentimentEntity;

    public String wrapValues() {
        return username + " " + text + " " + userDescription + " " + userLocation;
    }

    public TwitProfileWrapper(long userId, String username, String screenName, String text, String userDescription, String userLocation, String userTimeZone, String userLanguage, String profileLang, TweetSentimentEntity tweetSentimentEntity) {
        this.userId = userId;
        this.username = username;
        this.screenName = screenName;
        this.text = text;
        this.userDescription = userDescription;
        this.userLocation = userLocation;
        this.userTimeZone = userTimeZone;
        this.userLanguage = userLanguage;
        this.profileLang = profileLang;
        this.tweetSentimentEntity = tweetSentimentEntity;
    }

    public TwitProfileWrapper() {
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserTimeZone() {
        return userTimeZone;
    }

    public void setUserTimeZone(String userTimeZone) {
        this.userTimeZone = userTimeZone;
    }

    public String getProfileLang() {
        return profileLang;
    }

    public void setProfileLang(String profileLang) {
        this.profileLang = profileLang;
    }

    public Date getTextCreatedDate() {
        return textCreatedDate;
    }

    public void setTextCreatedDate(Date textCreatedDate) {
        this.textCreatedDate = textCreatedDate;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public TweetSentimentEntity getTweetSentimentEntity() {
        return tweetSentimentEntity;
    }

    public void setTweetSentimentEntity(TweetSentimentEntity tweetSentimentEntity) {
        this.tweetSentimentEntity = tweetSentimentEntity;
    }
}
