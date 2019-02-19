package com.verfehlix.followerinfopolling;


import java.util.ArrayList;

import twitter4j.*;

public class TwitterProfileConnector {

    Twitter twitter;
    String userName;

    /**
     * Constructor: initializes a twitter4j instance & checks if the given user exists
     * @param userName
     * @throws TwitterException
     */
    public TwitterProfileConnector(String userName) throws TwitterException {
        this.twitter = TwitterFactory.getSingleton();
        this.userName = userName;

        // check if user exists on twitter
        twitter.showUser(this.userName);
    }

    /**
     * Function that fetches all followers of the set user and returns their handles in a list
     * Uses cursoring (see https://developer.twitter.com/en/docs/basics/cursoring.html)
     * @return List that contains the handles of the followers of the set user
     * @throws TwitterException
     */
    public ArrayList<User> fetchFollowers() throws TwitterException {

        // initialize empty list that will contain all follower handles
        ArrayList<User> followerList = new ArrayList<>();

        // initialize cursor for api calls (-1 is the starting value)
        long cursor = -1;

        // as long as there are more cursors available, call twitter api to fetch next batch of followers
        while (cursor != 0) {

            // count (i.e. how many should be fetched max.)
            int count = 200;

            // skipStatus (i.e. statuses will not be included)
            boolean skipStatus = true;

            // includeUserEntities (i.e. entities will not be included)
            boolean includeUserEntities = false;

            // call twitter API
            PagableResponseList<User> followers = this.twitter.getFollowersList(this.userName, cursor, count, skipStatus, includeUserEntities);

            // add this batch of followers to gathered list
            followerList.addAll(followers);

            // get next cursor
            cursor = followers.getNextCursor();
        }

        return followerList;
    }
}
