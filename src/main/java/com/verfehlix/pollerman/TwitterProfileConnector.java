package com.verfehlix.pollerman;


import java.util.ArrayList;

import twitter4j.*;

public class TwitterProfileConnector {

    Twitter twitter;
    String userName;

    public TwitterProfileConnector(String userName) throws TwitterException {
        this.twitter = TwitterFactory.getSingleton();
        this.userName = userName;

        // check if user exists on twitter
        twitter.showUser(this.userName);
    }

    public ArrayList<User> fetchFollowers() throws TwitterException {

        ArrayList<User> followerList = new ArrayList<>();

        long cursor = -1;

        while (cursor != 0) {
            PagableResponseList<User> followers = this.twitter.getFollowersList(this.userName, cursor, 200, true, true);

            followerList.addAll(followers);

            cursor = followers.getNextCursor();
        }

        return followerList;
    }
}
