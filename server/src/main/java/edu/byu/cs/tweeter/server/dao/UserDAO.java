package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public interface UserDAO {

    LoginResponse login(String username, String password, AuthToken authToken);
    RegisterResponse register(String firstName, String lastName, String alias, String password, String imageUrl, AuthToken authToken);
    void addUser(String alias, String firstName, String lastName, String password, String url);
}
