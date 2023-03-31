package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.LameStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFeed;
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

public class DynamoFeedDAO implements FeedDAO {
    private static final String TABLE_NAME = "feed";

    private static final String AliasAttr = "alias";
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
    public DataPage<DynamoFeed> getFeed(String userAlias, int pageSize, String lastDate) {
        DynamoDbTable<DynamoFeed> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoFeed.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastDate)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AliasAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(DateAttr, AttributeValue.builder().n(lastDate).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<DynamoFeed> result = new DataPage<DynamoFeed>();

        PageIterable<DynamoFeed> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<DynamoFeed> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    @Override
    public void updateFeed(String alias, Status postedStatus) {
        DynamoDbTable<DynamoFeed> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoFeed.class));

        Long date = Long.valueOf(postedStatus.getDate());
        DynamoStatus dynamoStatus = new DynamoStatus(postedStatus.getPost(), postedStatus.getUser().getAlias(), date, postedStatus.getMentions(), postedStatus.getUrls());
        DynamoFeed dynamoFeed = new DynamoFeed(alias, date, dynamoStatus);

        table.putItem(dynamoFeed);
    }

    DynamoStatus getStatus(String userAlias, Long date) {
        DynamoDbTable<DynamoFeed> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoFeed.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .sortValue(date)
                .build();

        DynamoFeed feed = table.getItem(key);
        return feed.getStatus();
    }

    Status dynamoStatusToStatus(DynamoStatus dynamoStatus, DAOFactory daoFactory) {
        DynamoUser dynamoUser = daoFactory.getUserDAO().getUser(dynamoStatus.getUserAlias());
        User user = daoFactory.getUserDAO().dynamoUserToUser(dynamoUser);
        return new Status(dynamoStatus.getPost(), user, dynamoStatus.getDate().toString(), dynamoStatus.getMentions(), dynamoStatus.getUrls());
    }

    @Override
    public List<Status> dataPageToFeed(DataPage<DynamoFeed> dataPage, DAOFactory daoFactory) {
        List<Status> feed = new ArrayList<>();
        for(DynamoFeed dynamoFeed : dataPage.getValues()) {
            DynamoStatus dyanmoStatus = getStatus(dynamoFeed.getStatus().getUserAlias(), dynamoFeed.getDate());
            Status status = dynamoStatusToStatus(dyanmoStatus, daoFactory);
            feed.add(status);
        }
        return feed;
    }

}
