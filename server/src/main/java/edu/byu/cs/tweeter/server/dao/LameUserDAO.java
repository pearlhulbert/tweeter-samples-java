package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class LameUserDAO {

    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    public User dummyUser() {
        return getFakeData().getFirstUser();
    }

    public AuthToken dummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    public User specificUser(String alias) {
        return getFakeData().findUserByAlias(alias);
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse(dummyUser(), dummyAuthToken());
        return response;
    }

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse(dummyUser(), dummyAuthToken());
        return response;
    }

    public UserResponse getUser(UserRequest request) {
        String alias = request.getAlias();
        UserResponse response = new UserResponse(specificUser(alias));
        return response;
    }

    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse();
    }
}
