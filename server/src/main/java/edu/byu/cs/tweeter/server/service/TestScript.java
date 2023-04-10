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
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
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

        // How many follower users to add
        // We recommend you test this with a smaller number first, to make sure it works for you
        int NUM_USERS = 10000;


        udao.addUser("@p", "Pearl", "Hulbert", "p", "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/IMG_9488.jpg");
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String firstName = "Gal";
            String lastName = " " + i;
            String alias = "@gal" + i;
            String url = "https://image-tweeter-bucket.s3.us-west-2.amazonaws.com/baby-grand-brown.jpg";

            User user = new User(firstName, lastName, alias, url);
            users.add(user);
        }

        if (users.size() > 0) {
            udao.addUserBatch(users);
            //fdao.addFollowerBatch(users, "@p", "Pearl Hulbert");
        }

    }

}