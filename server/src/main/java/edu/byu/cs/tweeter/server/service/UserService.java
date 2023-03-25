package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.LameUserDAO;

public class UserService {

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return getUserDAO().login(request);
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
            throw new RuntimeException("[Bad Request] Missing a image");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        return getUserDAO().register(registerRequest);
    }

    public UserResponse getUser(UserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a user alias");
        }
        return getUserDAO().getUser(request);
    }

    public LogoutResponse logout(LogoutRequest request) {
        return getUserDAO().logout(request);
    }

    LameUserDAO getUserDAO() {
        return new LameUserDAO();
    }
}
