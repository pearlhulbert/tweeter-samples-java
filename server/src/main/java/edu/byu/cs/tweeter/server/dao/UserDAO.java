package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public interface UserDAO {

    LoginResponse login(String username, String password, AuthToken authToken);
    void addUser(String alias, String firstName, String lastName, String password, String url);
}
