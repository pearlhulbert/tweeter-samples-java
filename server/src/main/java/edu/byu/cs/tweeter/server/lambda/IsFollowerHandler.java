package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {

    public static void main(String args[]) {
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginHandler loginHandler = new LoginHandler();
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        DAOFactory daoFactory = new DynamoFactory();
        DynamoUser dUser1 = daoFactory.getUserDAO().getUser("@p");
        User user1 = daoFactory.getUserDAO().dynamoUserToUser(dUser1);
        DynamoUser dUser2 = daoFactory.getUserDAO().getUser("@user1");
        User user2 = daoFactory.getUserDAO().dynamoUserToUser(dUser2);
        IsFollowerRequest isFollowerRequest = new IsFollowerRequest(user1, loginResponse.getAuthToken(), user2);
        IsFollowerHandler isFollowerHandler = new IsFollowerHandler();
        IsFollowerResponse isFollowerResponse = isFollowerHandler.handleRequest(isFollowerRequest, null);
        System.out.println("follower: " + isFollowerResponse.isIsFollower());
    }
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest request, Context context) {
        FollowService service = new FollowService(new DynamoFactory());
        return service.isFollowing(request);
    }

}
