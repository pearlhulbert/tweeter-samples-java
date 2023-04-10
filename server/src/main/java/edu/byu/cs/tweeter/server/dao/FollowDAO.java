package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public interface FollowDAO {

    void follow(String followerHandle, String followerName, String followeeHandle, String followeeName);
    DataPage<DynamoFollow> getFollowees(String followerHandle, int pageSize, String lastFollowee);
    DataPage<DynamoFollow> getFollowers(String followeeHandle, int pageSize, String lastFollower);
    boolean isFollowing(String followerHandle, String followeeHandle);
    void unFollow(String followerHandle, String followeeHandle);
    DynamoFollow getFollow(String followerHandle, String followeeHandle);
    List<String> getFollowerHandles(String alias);
    void addFollowerBatch(List<User> followers, String followeeHandle, String followeeName);
}
