package edu.byu.cs.tweeter.server.dao.dynamo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoUserDAO implements UserDAO {

    private static final String TABLE_NAME = "user";

    private DynamoDbTable<DynamoUser> table = enhancedClient.table(TABLE_NAME,
            TableSchema.fromBean(DynamoUser.class));

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    @Override
    public void addUser(String alias, String firstName, String lastName, String password, String url){
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));

        String salt = generateSalt();
        DynamoUser user = new DynamoUser();
        user.setAlias(alias);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setSecurePassword(createSecurePassword(password, salt));
        user.setImageUrl(url);
        user.setSalt(salt);
        user.setFollowerCount(0);
        user.setFollowingCount(0);
        userTable.putItem(user);
    }

    @Override
    public void updateFolloweeCount(String alias, Integer count) {
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        DynamoUser user = userTable.getItem(key);
        user.setFollowingCount(count);
        userTable.putItem(user);
    }

    @Override
    public void updateFollowerCount(String alias, Integer count) {
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        DynamoUser user = userTable.getItem(key);
        user.setFollowerCount(count);
        userTable.putItem(user);
    }

    @Override
    public int getFollowerCount(String alias) {
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        DynamoUser user = userTable.getItem(key);
        return user.getFollowerCount();
    }

    @Override
    public int getFollowingCount(String alias) {
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        DynamoUser user = userTable.getItem(key);
        return user.getFollowingCount();
    }




    @Override
    public DynamoUser getUser(String alias) {
        DynamoDbTable<DynamoUser> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        return userTable.getItem(key);
    }

    @Override
    public User dynamoUserToUser(DynamoUser dynamoUser) {
        return new User(dynamoUser.getFirstName(), dynamoUser.getLastName(), dynamoUser.getAlias(), dynamoUser.getImageUrl());
    }

    public String hashPassword(String password, String salt){

        String hashedPassword = createSecurePassword(password, salt);
        return hashedPassword;

    }

    private static String createSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    private static String generateSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            String saltString = Base64.getEncoder().encodeToString(salt);
            return saltString;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }

    private static void verify(boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }


    public DynamoUserDAO() {
    }


    @Override
    public User login(String username, String password, AuthToken authToken) {
        DynamoUser user = getUser(username);
        if (user == null) {
            return null;
        }
        String hashedPassword = hashPassword(password, user.getSalt());
        if (hashedPassword.equals(user.getSecurePassword())) {
            return dynamoUserToUser(user);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    @Override
    public RegisterResponse register(String firstName, String lastName, String alias, String password, String imageUrl, AuthToken authToken) {
        DynamoUser user = getUser(alias);
        if (user != null) {
            return new RegisterResponse("User already exists");
        }
        addUser(alias, firstName, lastName, password, imageUrl);
        User newUser = new User(firstName, lastName, alias, imageUrl);
        return new RegisterResponse(newUser, authToken);
    }

    @Override
    public void addUserBatch(List<User> users) {
        List<DynamoUser> batchToWrite = new ArrayList<>();
        for (User u : users) {
            String salt = generateSalt();
            String password = createSecurePassword("pass", salt);
            DynamoUser dto = new DynamoUser(u.getAlias(), u.getFirstName(), u.getLastName(), password, u.getImageUrl());
            dto.setSalt(salt);
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }

    private void writeChunkOfUserDTOs(List<DynamoUser> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<DynamoUser> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoUser.class));
        WriteBatch.Builder<DynamoUser> writeBuilder = WriteBatch.builder(DynamoUser.class).mappedTableResource(table);
        for (DynamoUser item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
