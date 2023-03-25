package edu.byu.cs.tweeter.server.dao.dynamo;

import java.security.Timestamp;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoStoryDAO implements StoryDAO {

    private static final String TABLE_NAME = "Story";

    private DynamoDbTable<DynamoStatus> table = enhancedClient.table(TABLE_NAME,
            TableSchema.fromBean(DynamoStatus.class));

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


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
