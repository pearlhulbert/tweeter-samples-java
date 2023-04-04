package edu.byu.cs.tweeter.server.service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        udao.addUser("@p", "Pearl", "Hulbert", "p", "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/IMG_9488.jpg");


        for(int i = 0; i < 20; i++){
            String alias = "@daffy" + i;
            String firstName = "Daffy" + i;
            String lastName = "Duck" + i;
            String url2 = "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/baby-grand-brown.jpg";
            udao.addUser(alias, firstName, lastName, "password" + i, url2);
            fdao.follow("@p", "Pearl Hulbert", alias, firstName + " " + lastName);
        }

        for (int i = 0; i < 20; ++i) {
            String alias = "@minnie" + i;
            String firstName = "Minnie" + i;
            String lastName = "Mouse" + i;
            String url2 = "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/play-bass.png";
            udao.addUser(alias, firstName, lastName, "password" + i, url2);
            fdao.follow(alias, firstName + " " + lastName, "@p", "Pearl Hulbert");
        }

        for(int i = 0; i < 20; i++){
            String alias = "@daffy" + i;
            String firstName = "Daffy" + i;
            String lastName = "Duck" + i;
            User user3 = udao.dynamoUserToUser(udao.getUser(alias));
            storyDAO.postStatus(new Status("This is a test status", user3, String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()), new ArrayList<>(), new ArrayList<>()));
        }

        for (int i = 0; i < 20; ++i) {
            User user3 = udao.dynamoUserToUser(udao.getUser("@p"));
            storyDAO.postStatus(new Status("This is test status " + i, user3, String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()), new ArrayList<>(), new ArrayList<>()));
        }

        for (int i = 0; i < 20; ++i) {
            String alias = "@minnie" + i;
            String firstName = "Minnie" + i;
            String lastName = "Mouse" + i;
            User user3 = udao.dynamoUserToUser(udao.getUser(alias));
            feedDAO.updateFeed("@p", new Status("This is a test status", user3, String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()), new ArrayList<>(), new ArrayList<>()));
        }

    }

}