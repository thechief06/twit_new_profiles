package com.miya.twit.search;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public final class SendDirectMessage {

    public static void main(String[] args) {
        
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            DirectMessage message = twitter.sendDirectMessage("HasanBuyukadam", "Merhaba");
            System.out.println("Direct message successfully sent to " + message.getRecipientScreenName());
            //System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to send a direct message: " + te.getMessage());
            System.exit(-1);
        }
    }
}