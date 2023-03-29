package edu.byu.cs.tweeter.server.dao.dynamo;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
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

    private static final String TABLE_NAME = "Story";

    private static final String UserAliasAttr = "userAlias";
    private static final String PostAttr = "post";
    private static final String DateAttr = "date";
    private static final String UrlsAttr = "urls";
    private static final String MentionsAttr = "mentions";

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


    public DataPage<DynamoStatus> getStory(String userAlias, int pageSize, String lastUserAlias) {
        DynamoDbTable<DynamoStatus> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(userAlias).build());
            //startKey.put(DAttr, AttributeValue.builder().s(lastFollowee).build());

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
