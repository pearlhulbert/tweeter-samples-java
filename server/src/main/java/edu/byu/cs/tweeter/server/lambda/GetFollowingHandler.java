package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {

    public static void main(String args[]) {
//        DAOFactory daoFactory = new DynamoFactory();
//        daoFactory.getUserDAO().addUser("@p", "Pearl", "Hulbert", "p", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginHandler loginHandler = new LoginHandler();
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        AuthToken authToken = loginResponse.getAuthToken();
        FollowingRequest request = new FollowingRequest(authToken, "@p", 10, null);
        GetFollowingHandler handler = new GetFollowingHandler();
        FollowingResponse response = handler.handleRequest(request, null);
        FollowerRequest followerRequest = new FollowerRequest(authToken, "@p", 10, null);
        GetFollowersHandler followerHandler = new GetFollowersHandler();
        followerHandler.handleRequest(followerRequest, null);
    }
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public FollowingResponse handleRequest(FollowingRequest request, Context context) {
        FollowService service = new FollowService(new DynamoFactory());
        return service.getFollowees(request);
    }
}
