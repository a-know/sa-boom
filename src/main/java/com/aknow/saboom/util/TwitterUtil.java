package com.aknow.saboom.util;

import com.aknow.saboom.model.User;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterUtil {

	@SuppressWarnings("static-method")
    public void doTweet(String tweetContents, User user){
        Twitter twitter = new TwitterFactory().getInstance(new AccessToken(user.getTwitterAccessToken(), user.getTwitterAccessTokenSecret()));

        try {
            twitter.updateStatus(tweetContents);
        } catch (TwitterException e) {

            e.printStackTrace();
        }
    }
}
