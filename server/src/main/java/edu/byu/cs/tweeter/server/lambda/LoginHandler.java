package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {

    public static void main(String args[]) {
//        DAOFactory daoFactory = new DynamoFactory();
//        daoFactory.getUserDAO().addUser("@p", "Pearl", "Hulbert", "p", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginHandler loginHandler = new LoginHandler();
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        System.out.println(loginResponse.getUser().getFirstName());
    }

    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        DAOFactory daoFactory = new DynamoFactory();
        UserService userService = new UserService(daoFactory);
        return userService.login(loginRequest);
    }
}
