package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoAuthToken;

public interface AuthtokenDAO {
    boolean isValidToken(AuthToken authToken);
    void addToken(AuthToken token, String alias);
    AuthToken createAuthToken(String username);
    DynamoAuthToken getAuthToken(AuthToken token);
    void logout(AuthToken token);
}
