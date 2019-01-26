package com.verfehlix.pollerman;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.User;

@Component
public class FollowerMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(FollowerMonitor.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private String userName;

    private TwitterProfileConnector myProfileConnector;

    @Autowired
    public FollowerMonitor(@Value("${twitter.userName}") String userName) throws TwitterException {
        this.userName = userName;
        this.myProfileConnector = new TwitterProfileConnector(this.userName);

    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() throws TwitterException {
        ArrayList<User> followers = myProfileConnector.fetchFollowers();
        LOG.info("{}: User {} has {} followers.", DATE_FORMAT.format(new Date()), this.userName, followers.size());
    }
}
