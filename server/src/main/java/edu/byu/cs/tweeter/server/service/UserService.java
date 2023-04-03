package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.LameUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public class UserService {

    private DAOFactory daoFactory;

    public UserService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public UserService() {
        this.daoFactory = null;
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        AuthToken token = daoFactory.getAuthtokenDAO().createAuthToken(request.getUsername());
        if (token == null) {
            throw new RuntimeException("[Internal Server Error] Could not create auth token");
        }
        daoFactory.getAuthtokenDAO().addToken(token, request.getUsername());
        LoginResponse response = daoFactory.getUserDAO().login(request.getUsername(), request.getPassword(), token);
        System.out.println(response.getMessage());
        return response;
    }


    public RegisterResponse register(RegisterRequest registerRequest) {
        if(registerRequest.getAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(registerRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        else if (registerRequest.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        }
        else if (registerRequest.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        }
        else if (registerRequest.getImageUrl() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }
        AuthToken token = daoFactory.getAuthtokenDAO().createAuthToken(registerRequest.getAlias());
        if (token == null) {
            throw new RuntimeException("[Internal Server Error] Could not create auth token");
        }
        daoFactory.getAuthtokenDAO().addToken(token, registerRequest.getAlias());
        String link = daoFactory.getImageDAO().addImage(registerRequest.getAlias(), registerRequest.getImageUrl());
        registerRequest.setImageUrl(link);
        return daoFactory.getUserDAO().register(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getAlias(), registerRequest.getPassword(), registerRequest.getImageUrl(), token);
    }

    public UserResponse getUser(UserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a user alias");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new UserResponse("Invalid auth token, user no longer active");
        }
        DynamoUser dynamoUser = daoFactory.getUserDAO().getUser(request.getAlias());
        User user = daoFactory.getUserDAO().dynamoUserToUser(dynamoUser);
        return new UserResponse(user);
    }

    public LogoutResponse logout(LogoutRequest request) {
        return getUserDAO().logout(request);
    }

    LameUserDAO getUserDAO() {
        return new LameUserDAO();
    }
}
