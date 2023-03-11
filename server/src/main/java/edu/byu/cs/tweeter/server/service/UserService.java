package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

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

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
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

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
