package edu.byu.cs.tweeter.server.service;
import java.sql.Timestamp;
import java.time.LocalDate;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


public class TestScript {

    private static DynamoDbClient client;

    private static DynamoDbEnhancedClient enhancedClient;

    public static void main(String[] args) {
        client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build();
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        DAOFactory factory = new DynamoFactory();
        FollowDAO fdao = factory.getFollowDAO();
        UserDAO udao = factory.getUserDAO();
        FeedDAO feedDAO = factory.getFeedDAO();
        StoryDAO storyDAO = factory.getStoryDAO();

//        udao.addUser("@user1", "User", "1", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        udao.addUser("@user2", "User", "2", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
//        udao.addUser("@p", "Pearl", "Hulbert", "p", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        fdao.follow("@user1", "User 1", "@p", "Pearl Hulbert");
        fdao.follow("@user2", "User 2", "@p", "Pearl Hulbert");
        fdao.follow("@p", "Pearl Hulbert", "@user1", "User 1");
        udao.updateFollowerCount("@p", 1);
        udao.updateFolloweeCount("@p", 1);
        udao.updateFollowerCount("@user1", 1);
        udao.updateFolloweeCount("@user2", 1);
        User user = new User ("User", "1", "@user1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Timestamp date = new Timestamp(System.currentTimeMillis());
        String dateStr = String.valueOf(date.getTime());
        feedDAO.updateFeed("@user1", new Status("This is a test status", user, dateStr, null, null));
        storyDAO.postStatus(new Status("This is a test status", user, dateStr, null, null));
    }





}