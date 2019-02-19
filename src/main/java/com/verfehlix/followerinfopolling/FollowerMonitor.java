package com.verfehlix.followerinfopolling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.User;

@Component
public class FollowerMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(FollowerMonitor.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    private String userName;

    private TwitterProfileConnector myProfileConnector;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor --> sets username (twitter handle) based on application properties / arguments
     * and initializes a twitter profile connector for that handle
     * @param userName
     * @throws TwitterException
     */
    @Autowired
    public FollowerMonitor(@Value("${twitter.userName}") String userName) throws TwitterException {
        this.userName = userName;
        this.myProfileConnector = new TwitterProfileConnector(this.userName);
    }

    /**
     * Scheduled function that gets executed every 2 minutes (120.000 ms)
     * Fetches follower information and saves results into a database
     * @throws TwitterException
     */
    @Scheduled(fixedRate = 120000)
    public void monitorFollowers() throws TwitterException {
        // get current timestamp
        Date date = new Date();
        String timestamp = DATE_FORMAT.format(date);

        // get follower list from twitter
        List<User> followers = myProfileConnector.fetchFollowers();

        // log info
        LOG.info("{}: User {} has {} followers.", timestamp, this.userName, followers.size());

        // write to DB
        writeFollowerInfoToDatabase(date, followers);
    }

    /**
     * Function that writes given follower information (in the form of a list containing all handles
     * of the followers) into a database
     * @param date
     * @param followers
     */
    private void writeFollowerInfoToDatabase(Date date, List<User> followers) {
        // get follower count
        int followerCount = followers.size();

        // convert list of Twitter users -> list of usernames -> json array of usernames
        List<String> followerNames = followers.stream().map(User::getScreenName).collect(Collectors.toList());
        String followerNamesJsonArray = new Gson().toJson(followerNames);

        // write info to DB
        jdbcTemplate.update(
            "INSERT INTO follower_info (timestamp, follower_count, follower_list) VALUES (?, ?, ?::JSON)",
            new java.sql.Timestamp(date.getTime()),
            followerCount,
            followerNamesJsonArray
        );

        // Log success
        String timestamp = DATE_FORMAT.format(date);
        LOG.info("{}: Write to DB successful.", timestamp);
    }
}
