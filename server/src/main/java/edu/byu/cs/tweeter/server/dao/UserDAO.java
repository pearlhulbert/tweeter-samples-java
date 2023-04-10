package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;

public interface UserDAO {

    User login(String username, String password, AuthToken authToken);
    RegisterResponse register(String firstName, String lastName, String alias, String password, String imageUrl, AuthToken authToken);
    void addUser(String alias, String firstName, String lastName, String password, String url);
    DynamoUser getUser(String alias);
    User dynamoUserToUser(DynamoUser dynamoUser);
    int getFollowingCount(String alias);
    int getFollowerCount(String alias);
    void updateFolloweeCount(String alias, Integer count);
    void updateFollowerCount(String alias, Integer count);
    void addUserBatch(List<User> users);
}
