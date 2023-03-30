package edu.byu.cs.tweeter.server.dao.dynamo;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;


public class DynamoFollowDAO implements FollowDAO {

        private static final String TableName = "follows";
        public static final String IndexName = "follows_index";

        private static final String FollowerHandleAttr = "follower_handle";
        private static final String FollowerNameAttr = "follower_name";
        private static final String FolloweeHandleAttr = "followee_handle";
        private static final String FolloweeNameAttr = "followee_name";

        // DynamoDB client
        private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build();

        private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        private static boolean isNonEmptyString(String value) {
                return (value != null && value.length() > 0);
        }

        @Override
        public void follow(String followerHandle, String followerName, String followeeHandle, String followeeName) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(followerHandle).sortValue(followeeHandle)
                        .build();

                // load it if it exists
                DynamoFollow follow = table.getItem(key);
                if(follow != null) {
                        table.updateItem(follow);
                } else {
                        DynamoFollow newFollow = new DynamoFollow();
                        newFollow.set_follower_handle(followerHandle);
                        newFollow.set_follower_name(followerName);
                        newFollow.set_followee_handle(followeeHandle);
                        newFollow.set_followee_name(followeeName);
                        table.putItem(newFollow);
                }
        }

        public void updateFollowerName(String follwerHandle, String followeeHandle, String followerName) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(follwerHandle).sortValue(followeeHandle)
                        .build();
                DynamoFollow follow = table.getItem(key);
                follow.set_follower_name(followerName);
                table.updateItem(follow);
        }

        public void updateFolloweeName(String follwerHandle, String followeeHandle, String followeeName) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(follwerHandle).sortValue(followeeHandle)
                        .build();
                DynamoFollow follow = table.getItem(key);
                follow.set_followee_name(followeeName);
                table.updateItem(follow);
        }


        @Override
        public DynamoFollow getFollow(String followerHandle, String followeeHandle) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(followerHandle).sortValue(followeeHandle)
                        .build();
                return table.getItem(key);
        }

        @Override
        public void unFollow(String followerHandle, String followeeHandle) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(followerHandle).sortValue(followeeHandle)
                        .build();
                table.deleteItem(key);
        }

        @Override
        public DataPage<DynamoFollow> getFollowees(String followerHandle, int pageSize, String lastFollowee) {
                DynamoDbTable<DynamoFollow> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class));
                Key key = Key.builder()
                        .partitionValue(followerHandle)
                        .build();

                QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                        .queryConditional(QueryConditional.keyEqualTo(key))
                        .limit(pageSize);

                if(isNonEmptyString(lastFollowee)) {
                        // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
                        Map<String, AttributeValue> startKey = new HashMap<>();
                        startKey.put(FollowerHandleAttr, AttributeValue.builder().s(followerHandle).build());
                        startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(lastFollowee).build());

                        requestBuilder.exclusiveStartKey(startKey);
                }

                QueryEnhancedRequest request = requestBuilder.build();

                DataPage<DynamoFollow> result = new DataPage<DynamoFollow>();

                PageIterable<DynamoFollow> pages = table.query(request);
                pages.stream()
                        .limit(1)
                        .forEach((Page<DynamoFollow> page) -> {
                                result.setHasMorePages(page.lastEvaluatedKey() != null);
                                page.items().forEach(visit -> result.getValues().add(visit));
                        });

                return result;
        }

        @Override
        public DataPage<DynamoFollow> getFollowers(String followeeHandle, int pageSize, String lastFollower) {
                DynamoDbIndex<DynamoFollow> index = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFollow.class)).index(IndexName);
                Key key = Key.builder()
                        .partitionValue(followeeHandle)
                        .build();

                QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                        .queryConditional(QueryConditional.keyEqualTo(key))
                        .limit(pageSize);

                if(isNonEmptyString(lastFollower)) {
                        Map<String, AttributeValue> startKey = new HashMap<>();
                        startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(followeeHandle).build());
                        startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastFollower).build());

                        requestBuilder.exclusiveStartKey(startKey);
                }

                QueryEnhancedRequest request = requestBuilder.build();

                DataPage<DynamoFollow> result = new DataPage<DynamoFollow>();

                SdkIterable<Page<DynamoFollow>> sdkIterable = index.query(request);
                PageIterable<DynamoFollow> pages = PageIterable.create(sdkIterable);
                pages.stream()
                        .limit(1)
                        .forEach((Page<DynamoFollow> page) -> {
                                result.setHasMorePages(page.lastEvaluatedKey() != null);
                                page.items().forEach(visit -> result.getValues().add(visit));
                        });

                return result;
        }

        @Override
        public List<User> dataPageToFollowees(DataPage<DynamoFollow> dataPage, DAOFactory daoFactory) {
                List<User> followers = new ArrayList<>();
                for(DynamoFollow dynamoFollow : dataPage.getValues()) {
                        DynamoUser dyanmoUser = daoFactory.getUserDAO().getUser(dynamoFollow.get_followee_handle());
                        User user = daoFactory.getUserDAO().dynamoUserToUser(dyanmoUser);
                        followers.add(user);
                }
                return followers;
        }

        @Override
        public List<User> dataPageToFollowers(DataPage<DynamoFollow> dataPage, DAOFactory daoFactory) {
                List<User> followers = new ArrayList<>();
                for(DynamoFollow dynamoFollow : dataPage.getValues()) {
                        DynamoUser dyanmoUser = daoFactory.getUserDAO().getUser(dynamoFollow.get_follower_handle());
                        User user = daoFactory.getUserDAO().dynamoUserToUser(dyanmoUser);
                        followers.add(user);
                }
                return followers;
        }

        @Override
        public boolean isFollowing(String followerHandle, String followeeHandle) {
                DynamoFollow follow = getFollow(followerHandle, followeeHandle);
                if (follow == null) {
                        return false;
                }
                return true;
        }


}