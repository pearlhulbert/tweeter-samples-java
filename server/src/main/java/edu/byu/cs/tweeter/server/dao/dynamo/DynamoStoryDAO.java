package edu.byu.cs.tweeter.server.dao.dynamo;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoStoryDAO implements StoryDAO {

    private static final String TABLE_NAME = "story-2";

    private static final String UserAliasAttr = "userAlias";
    private static final String DateAttr = "date";

    private DynamoDbTable<DynamoStatus> table = enhancedClient.table(TABLE_NAME,
            TableSchema.fromBean(DynamoStatus.class));

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
    public DataPage<DynamoStatus> getStory(String userAlias, int pageSize, String lastDate) {
        DynamoDbTable<DynamoStatus> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastDate)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(DateAttr, AttributeValue.builder().n(lastDate).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<DynamoStatus> result = new DataPage<DynamoStatus>();

        PageIterable<DynamoStatus> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<DynamoStatus> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    DynamoStatus getStatus(String userAlias, Long date) {
        DynamoDbTable<DynamoStatus> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .sortValue(date)
                .build();

        return table.getItem(key);
    }

    Status dynamoStatusToStatus(DynamoStatus dynamoStatus, DAOFactory daoFactory) {
        DynamoUser dynamoUser = daoFactory.getUserDAO().getUser(dynamoStatus.getUserAlias());
        User user = daoFactory.getUserDAO().dynamoUserToUser(dynamoUser);
        return new Status(dynamoStatus.getPost(), user, dynamoStatus.getDate().toString(), dynamoStatus.getMentions(), dynamoStatus.getUrls());
    }

    @Override
    public List<Status> dataPageToStory(DataPage<DynamoStatus> dataPage, DAOFactory daoFactory) {
        List<Status> story = new ArrayList<>();
        for(DynamoStatus dynamoFeed : dataPage.getValues()) {
            DynamoStatus dyanmoStatus = getStatus(dynamoFeed.getUserAlias(), dynamoFeed.getDate());
            Status status = dynamoStatusToStatus(dyanmoStatus, daoFactory);
            story.add(status);
        }
        return story;
    }

    @Override
    public boolean postStatus(Status status) {
        Long date = Long.valueOf(status.getDate());
        try {
            DynamoStatus dynamoStatus = new DynamoStatus(status.getPost(), status.getUser().getAlias(), date, status.getUrls(), status.getMentions());
            table.putItem(dynamoStatus);
            return true;
        } catch (Exception e) {
            System.err.println("unable to post status " + e.getMessage());
            return false;
        }
    }

}
