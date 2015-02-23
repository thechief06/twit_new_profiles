package com.miya.twit.search;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author ertugrula
 */
public class SearchTweetPkk {

    public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            List<Tweet> tweets = null;
            List<Tweet> tweets2 = new ArrayList<Tweet>();
            while (true) {
                Query query = new Query("pkk");
                QueryResult result;
                result = twitter.search(query);
                tweets = result.getTweets();
                for (Tweet tweet : tweets) {
                    tweets2.add(tweet);
                   // System.out.println(tweet.getFromUser() + " - " + tweet.getText() + " /  " + tweet.getLocation() + " / " + tweet.getIsoLanguageCode());
                }
                if (tweets2.size() % 1000 == 0) {
                    System.out.println("******************************************************************************************************************************");
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}
