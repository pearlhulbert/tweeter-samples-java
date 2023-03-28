package edu.byu.cs.tweeter.server.dao.dynamo.domain;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class DynamoFollow {
    private String follower_handle;

    private String follower_name;
    private String followee_handle;

    private String followee_name;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoFollowDAO.IndexName)
    @DynamoDbAttribute("follower_handle")
    public String get_follower_handle() {
        return follower_handle;
    }

    public void set_follower_handle(String followerHandle) {
        this.follower_handle = followerHandle;
    }

    public String get_follower_name() {
        return follower_name;
    }

    public void set_follower_name(String followerName) {
        this.follower_name = followerName;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoFollowDAO.IndexName)
    @DynamoDbAttribute("followee_handle")
    public String get_followee_handle() {
        return followee_handle;
    }

    public void set_followee_handle(String followeeHandle) {
        this.followee_handle = followeeHandle;
    }

    public String get_followee_name() {
        return followee_name;
    }

    public void set_followee_name(String followeeName) {
        this.followee_name = followeeName;
    }


    @Override
    public String toString() {
        return "Follow{" +
                "followerHandle='" + follower_handle + '\'' +
                ", followerName='" + follower_name + '\'' +
                ", followeeHandle=" + followee_handle +
                ", followeeName=" + followee_name +
                '}';
    }
}